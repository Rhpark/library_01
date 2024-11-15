package kr.open.rhpark.library.system.service.access.telephony.usim

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.os.Build
import android.telephony.CellInfo
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.SignalStrength
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.util.SparseArray
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.access.telephony.base.BaseSubscriptionState
import kr.open.rhpark.library.system.service.access.telephony.base.CommonTelephonyCallback
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentSignalStrength
import java.util.concurrent.Executor

public class UsimStateInfo(
    context: Context,
    telephonyManager: TelephonyManager,
    subscriptionManager: SubscriptionManager,
    private val euiccManager: EuiccManager,
) : BaseSubscriptionState(
    context,
    listOf(
        READ_PHONE_STATE,
        READ_PHONE_NUMBERS,
        ACCESS_COARSE_LOCATION
    ),
    subscriptionManager,
    telephonyManager
) {

    private val uSimList = SparseArray<TelephonyManager>()
    private val telephonyCallbackList =  SparseArray<CommonTelephonyCallback>()
    private var uSimSlotMaximumCount:Int = 0

    init {
        updateUSimTelephonyManagerList()
        updateMaximumActiveSimSlotCount()
    }

    public fun isDualSim(): Boolean = uSimSlotMaximumCount == 2
    public fun isSingleSim(): Boolean = uSimSlotMaximumCount == 1
    public fun isMultiSim(): Boolean = uSimSlotMaximumCount > 1

    public fun updateMaximumActiveSimSlotCount() {
        uSimSlotMaximumCount = subscriptionManager.activeSubscriptionInfoCountMax
    }

    public fun getMaximumUSimCount():Int = uSimSlotMaximumCount

    @RequiresPermission(READ_PHONE_STATE)
    protected fun getActiveSimCount():Int = subscriptionManager.activeSubscriptionInfoCount


    @RequiresPermission(READ_PHONE_STATE)
    protected fun getActiveSimSlotIndexList() :List<Int> = getActiveSubscriptionInfoList().map { it->it.simSlotIndex }.toList()

    /**
     * tested single sim, dual sim(pSim + eSim)
     * return
     * @see TelephonyManager.SIM_STATE_%%
     * dualsim(psim,psin),(psim,esim)
     */
    private fun getMaximumActiveSimStatus(isAbleEsim:Boolean, isRegisterESim:Boolean, slotIndex:Int) :Int{

        val status = telephonyManager.getSimState(slotIndex)

        return if(isAbleEsim && slotIndex == 0 && status == TelephonyManager.SIM_STATE_UNKNOWN) {
            Logx.p("SimSlot 0, may be pSim is not ready")
            TelephonyManager.SIM_STATE_NOT_READY
        } else if (!isRegisterESim) {
            Logx.p("SimSlot $slotIndex, may be eSim is not register")
            TelephonyManager.SIM_STATE_UNKNOWN
        } else status
    }

    public fun getActiveSimStatus(subscriptionInfo: SubscriptionInfo) :Int{

        val simSlotIndex = subscriptionInfo.simSlotIndex
        val status = telephonyManager.getSimState(subscriptionInfo.simSlotIndex)
        return if(isMultiSim() && !subscriptionInfo.isEmbedded && simSlotIndex == 0 && status == TelephonyManager.SIM_STATE_UNKNOWN) {
            TelephonyManager.SIM_STATE_NOT_READY
        } else {
            status
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    public fun getMcc(slotIndex:Int) :String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.mccString

    @RequiresPermission(READ_PHONE_STATE)
    public fun getMnc(slotIndex:Int) :String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.mncString

    @RequiresPermission(READ_PHONE_STATE)
    public fun isNetworkRoaming(slotIndex:Int) : Boolean {
        getActiveSubscriptionInfoSimSlot(slotIndex)?.let {
            return subscriptionManager.isNetworkRoaming(it.subscriptionId)
        }
        return false
    }

    @RequiresPermission(READ_PHONE_STATE)
    public fun getDisplayName(slotIndex: Int):String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.displayName?.toString()

    @RequiresPermission(READ_PHONE_STATE)
    public fun getCountryIso(slotIndex: Int):String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.countryIso

    public fun getTelephonyManagerFromUSim(slotIndex: Int): TelephonyManager? = uSimList[slotIndex]

    /**
     * Target SDK verion  < 29 READ_PHONE_STATE
     * else READ_PHONE_NUMBERS or READ_SMS
     */
    @RequiresPermission(anyOf = [READ_PHONE_STATE, READ_PHONE_NUMBERS])
    public fun getPhoneNumber(slotIndex: Int):String? =
        getTelephonyManagerFromUSim(slotIndex)?.line1Number /* line1Number Required SDK Version 1 ~ 33 */
            ?: getActiveSubscriptionInfoSimSlot(slotIndex)?.number /* number Required SDK Version 30 ~ 33 */

    /**
     * return
     * @see TelephonyManager.NETWORK_TYPE_%%
     */
    @RequiresPermission(READ_PHONE_STATE)
    public fun getNetworkTypeFromData(slotIndex: Int):Int? = getTelephonyManagerFromUSim(slotIndex)?.dataNetworkType

    /**
     * return
     * @see TelephonyManager.NETWORK_TYPE_%%
     */
    @RequiresPermission(READ_PHONE_STATE)
    public fun getNetworkTypeFromVoice(slotIndex: Int):Int? = getTelephonyManagerFromUSim(slotIndex)?.voiceNetworkType


    @RequiresPermission(READ_PHONE_STATE)
    public fun subIdToSimSlotIndex(currentSubId:Int):Int? = getActiveSubscriptionInfoSubId(currentSubId)?.simSlotIndex

    /**
     * Required Permission
     * <permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     */
    @RequiresPermission(anyOf = [ACCESS_FINE_LOCATION , ACCESS_COARSE_LOCATION])
    public fun getCellInfo(slotIndex:Int): CellInfo? = getTelephonyManagerFromUSim(slotIndex)?.allCellInfo?.get(slotIndex)

    @RequiresPermission(anyOf = [READ_PHONE_STATE ,ACCESS_COARSE_LOCATION])
    public fun getServiceState(slotIndex: Int): ServiceState? = getTelephonyManagerFromUSim(slotIndex)?.serviceState

    public fun getSignalStrength(slotIndex:Int): SignalStrength? =getTelephonyManagerFromUSim(slotIndex)?.signalStrength

    /**
     * return
     * @see TelephonyManager.PHONE_TYPE_%%
     */
    public fun getPhoneType(slotIndex: Int) :Int?= getTelephonyManagerFromUSim(slotIndex)?.phoneType

    /**
     * Can add ESIM
     */
    public fun isAbleEsim(): Boolean = (euiccManager.euiccInfo != null && euiccManager.isEnabled)

    /**
     * Used for multisim with PSIM(Slot index is 0)
     * ESIM Added ?
     */
    public fun isRegisterESim(eSimSlotIndex: Int): Boolean =
        !(isAbleEsim() && eSimSlotIndex != 0 && subscriptionManager.accessibleSubscriptionInfoList == null)

    public fun getMaximumActiveSimStatus(slotIndex: Int): Int =
        getMaximumActiveSimStatus(isAbleEsim(), isRegisterESim(slotIndex), slotIndex)

    @RequiresPermission(READ_PHONE_STATE)
    public fun updateUSimTelephonyManagerList() {
        Logx.d("USimInfo","activeSubscriptionInfoList size ${getActiveSubscriptionInfoList().size}")
        getActiveSubscriptionInfoList().forEach {
            Logx.d("USimInfo","SubID ${it.toString()}\n")
            uSimList[it.simSlotIndex] = telephonyManager.createForSubscriptionId(it.subscriptionId)
            telephonyCallbackList[it.simSlotIndex] = CommonTelephonyCallback()
        }
    }

    /**
     * SDK_INT >= Build.VERSION_CODES.S
     */
    @RequiresApi(Build.VERSION_CODES.S)
    public fun registerCallBack(simSlotIndex: Int, executor: Executor) {
        val tm = uSimList[simSlotIndex]
        val callback = telephonyCallbackList[simSlotIndex]
        if (callback == null || tm == null) {
            throw Exception("telephonyCallbackList[$simSlotIndex] is null")
        }
        tm.registerTelephonyCallback(executor, callback.baseTelephonyCallback)
    }

    /**
     * SDK_INT < Build.VERSION_CODES.S
     */
    public fun registerListen(simSlotIndex: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Logx.w("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }
        val tm =uSimList[simSlotIndex]
        val callback =telephonyCallbackList[simSlotIndex]
        if(callback == null || tm == null) {
            throw Exception("telephonyCallbackList[$simSlotIndex] is null")
        }
        tm.listen(callback.basePhoneStateListener,
            PhoneStateListener.LISTEN_CELL_LOCATION or
                    PhoneStateListener.LISTEN_DATA_ACTIVITY or
                    PhoneStateListener.LISTEN_DATA_CONNECTION_STATE or
                    PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or
                    PhoneStateListener.LISTEN_CALL_STATE or
                    PhoneStateListener.LISTEN_ACTIVE_DATA_SUBSCRIPTION_ID_CHANGE or
                    PhoneStateListener.LISTEN_CELL_INFO)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    public fun unregisterCallBack(simSlotIndex: Int) {
        val tm = uSimList[simSlotIndex]
        val callback = telephonyCallbackList[simSlotIndex]
        if (callback == null || tm == null) {
            throw Exception("telephonyCallbackList[$simSlotIndex] is null")
        }
        try {
            tm.unregisterTelephonyCallback(callback.baseTelephonyCallback)
        }catch (e:Exception) {
            e.printStackTrace()
        }
    }

    public fun unregisterListen(simSlotIndex: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            throw Exception("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }
        val tm =uSimList[simSlotIndex]
        val callback =telephonyCallbackList[simSlotIndex]
        if(callback == null || tm == null) {
            throw Exception("telephonyCallbackList[$simSlotIndex] is null")
        }
        try {
            tm.listen(callback.basePhoneStateListener,PhoneStateListener.LISTEN_NONE)
        }catch (e:Exception) {
            e.printStackTrace()
        }
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnActiveDataSubId(simSlotIndex: Int, onActiveDataSubId: (subId: Int) -> Unit) {
        telephonyCallbackList[simSlotIndex]?.let { it.setOnActiveDataSubId(onActiveDataSubId) }
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnDataConnectionState(simSlotIndex: Int, onDataConnectionState: (state: Int, networkType: Int) -> Unit) {
        telephonyCallbackList[simSlotIndex]?.let { it.setOnDataConnectionState(onDataConnectionState) }
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnCellInfo(simSlotIndex: Int, onCellInfo: (currentCellInfo: CurrentCellInfo?) -> Unit) {
        telephonyCallbackList[simSlotIndex]?.let { it.setOnCellInfo(onCellInfo) }
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnSignalStrength(simSlotIndex: Int, onSignalStrength: (currentSignalStrength: CurrentSignalStrength?) -> Unit) {
        telephonyCallbackList[simSlotIndex]?.let { it.setOnSignalStrength(onSignalStrength) }
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnServiceState(simSlotIndex: Int, onServiceState: (currentServiceState: CurrentServiceState?) -> Unit) {
        telephonyCallbackList[simSlotIndex]?.let { it.setOnServiceState(onServiceState) }
    }

}
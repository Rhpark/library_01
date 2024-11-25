package kr.open.rhpark.library.system.service.access.internet.usim

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
import android.telephony.TelephonyDisplayInfo
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.util.SparseArray
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.util.forEach
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.access.internet.base.BaseSubscriptionState
import kr.open.rhpark.library.system.service.access.internet.telephony.calback.CommonTelephonyCallback
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentSignalStrength
import kr.open.rhpark.library.system.service.access.internet.telephony.data.state.TelephonyNetworkState
import java.util.concurrent.Executor

public open class UsimStateInfo(
    context: Context,
    telephonyManager: TelephonyManager,
    subscriptionManager: SubscriptionManager,
    public val euiccManager: EuiccManager,
) : BaseSubscriptionState(
    context,
    listOf(READ_PHONE_STATE, READ_PHONE_NUMBERS, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
    subscriptionManager,
    telephonyManager
) {

    private val uSimTelepnohyManagerList = SparseArray<TelephonyManager>()
    private val uSimTelephonyCallbackList =  SparseArray<CommonTelephonyCallback>()
    private val isRegistered =  SparseArray<Boolean>()

    init {
        updateUSimTelephonyManagerList()
    }

    public fun isDualSim(): Boolean = getMaximumUSimCount() == 2
    public fun isSingleSim(): Boolean = getMaximumUSimCount() == 1
    public fun isMultiSim(): Boolean = getMaximumUSimCount() > 1

    public fun getMaximumUSimCount(): Int = subscriptionManager.activeSubscriptionInfoCountMax

    @RequiresPermission(READ_PHONE_STATE)
    public fun getActiveSimCount():Int = subscriptionManager.activeSubscriptionInfoCount


    @RequiresPermission(READ_PHONE_STATE)
    public fun getActiveSimSlotIndexList() :List<Int> = getActiveSubscriptionInfoList().map { it->it.simSlotIndex }.toList()

    /**
     * tested single sim, dual sim(pSim + eSim)
     * return
     * @see TelephonyManager.SIM_STATE_%%
     * dualsim(psim,psin),(psim,esim)
     */
    private fun getMaximumActiveSimStatus(isAbleEsim:Boolean, isRegisterESim:Boolean, slotIndex:Int) :Int{

        val status = telephonyManager.getSimState(slotIndex)

        return if(isAbleEsim && slotIndex == 0 && status == TelephonyManager.SIM_STATE_UNKNOWN) {
            Logx.w("SimSlot 0, may be pSim is not ready")
            TelephonyManager.SIM_STATE_NOT_READY
        } else if (!isRegisterESim) {
            Logx.w("SimSlot $slotIndex, may be eSim is not register")
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
    public fun getMcc(slotIndex:Int) :String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.let {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) it.mcc.toString()
        else it.mccString
    }

    @RequiresPermission(READ_PHONE_STATE)
    public fun getMnc(slotIndex:Int) :String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.let {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) it.mnc.toString()
        else it.mncString
    }

    @RequiresPermission(READ_PHONE_STATE)
    public fun isNetworkRoaming(slotIndex:Int) : Boolean {
        return getActiveSubscriptionInfoSimSlot(slotIndex)?.let {
            subscriptionManager.isNetworkRoaming(it.subscriptionId)
        }?: false
    }

    @RequiresPermission(READ_PHONE_STATE)
    public fun getDisplayName(slotIndex: Int):String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.displayName?.toString()

    @RequiresPermission(READ_PHONE_STATE)
    public fun getCountryIso(slotIndex: Int):String? = getActiveSubscriptionInfoSimSlot(slotIndex)?.countryIso

    public fun getTelephonyManagerFromUSim(slotIndex: Int): TelephonyManager? = uSimTelepnohyManagerList[slotIndex]

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
            uSimTelepnohyManagerList[it.simSlotIndex] = telephonyManager.createForSubscriptionId(it.subscriptionId)
            uSimTelephonyCallbackList[it.simSlotIndex] = CommonTelephonyCallback(uSimTelepnohyManagerList[it.simSlotIndex])
        }
    }

    /**
     * SDK_INT >= Build.VERSION_CODES.S
     */
    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(READ_PHONE_STATE)
    public fun registerCallBack(simSlotIndex: Int,
                                executor: Executor,
                                isGpsOn: Boolean,
                                onActiveDataSubId: ((subId: Int) -> Unit)? = null,
                                onDataConnectionState: ((state: Int, networkType: Int) -> Unit)? = null,
                                onCellInfo: ((currentCellInfo: CurrentCellInfo) -> Unit)? = null,
                                onSignalStrength: ((currentSignalStrength: CurrentSignalStrength) -> Unit)? = null,
                                onServiceState: ((currentServiceState: CurrentServiceState) -> Unit)? = null,
                                onCallState: ((callState: Int, phoneNumber: String?) -> Unit)? = null,
                                onDisplayInfo: ((telephonyDisplayInfo: TelephonyDisplayInfo) -> Unit)? = null,
                                onTelephonyNetworkState: ((telephonyNetworkState: TelephonyNetworkState) -> Unit)? = null) {

        val tm = uSimTelepnohyManagerList[simSlotIndex] ?: throw IllegalStateException("TelephonyManager [$simSlotIndex] is null")
        val callback = uSimTelephonyCallbackList[simSlotIndex] ?: throw IllegalStateException("telephonyCallbackList [$simSlotIndex] is null")

        unregisterCallBack(simSlotIndex)

        if(isGpsOn) {
            tm.registerTelephonyCallback(executor, callback.baseGpsTelephonyCallback)
        } else {
            tm.registerTelephonyCallback(executor, callback.baseTelephonyCallback)
        }

        setOnActiveDataSubId(simSlotIndex, onActiveDataSubId)
        setOnDataConnectionState(simSlotIndex, onDataConnectionState)
        setOnCellInfo(simSlotIndex, onCellInfo)
        setOnSignalStrength(simSlotIndex, onSignalStrength)
        setOnServiceState(simSlotIndex, onServiceState)
        setOnCallState(simSlotIndex, onCallState)
        setOnDisplayState(simSlotIndex, onDisplayInfo)
        setOnTelephonyNetworkType(simSlotIndex, onTelephonyNetworkState)
        isRegistered[simSlotIndex] = true
    }

    /**
     * SDK_INT < Build.VERSION_CODES.S
     */
    @RequiresPermission(READ_PHONE_STATE)
    public fun registerListen(simSlotIndex: Int, isGpsOn:Boolean,
                              onActiveDataSubId: ((subId: Int) -> Unit)? = null,
                              onDataConnectionState: ((state: Int, networkType: Int) -> Unit)? = null,
                              onCellInfo: ((currentCellInfo: CurrentCellInfo) -> Unit)? = null,
                              onSignalStrength: ((currentSignalStrength: CurrentSignalStrength) -> Unit)? = null,
                              onServiceState: ((currentServiceState: CurrentServiceState) -> Unit)? = null,
                              onCallState: ((callState: Int, phoneNumber: String?) -> Unit)? = null) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Logx.w("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }
        val tm =uSimTelepnohyManagerList[simSlotIndex]
        val callback =uSimTelephonyCallbackList[simSlotIndex]
        if(callback == null || tm == null) {
            throw Exception("telephonyCallbackList[$simSlotIndex] is null")
        }

        val event = if(isGpsOn) {
            PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or
                    PhoneStateListener.LISTEN_DATA_CONNECTION_STATE or
                    PhoneStateListener.LISTEN_DATA_ACTIVITY or
                    PhoneStateListener.LISTEN_SERVICE_STATE or
                    PhoneStateListener.LISTEN_CALL_STATE or
                    PhoneStateListener.LISTEN_CELL_INFO or
                    PhoneStateListener.LISTEN_ACTIVE_DATA_SUBSCRIPTION_ID_CHANGE
        } else {
            PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or
                    PhoneStateListener.LISTEN_DATA_CONNECTION_STATE or
                    PhoneStateListener.LISTEN_DATA_ACTIVITY or
                    PhoneStateListener.LISTEN_SERVICE_STATE or
                    PhoneStateListener.LISTEN_CALL_STATE or
                    PhoneStateListener.LISTEN_ACTIVE_DATA_SUBSCRIPTION_ID_CHANGE
        }
        tm.listen(callback.basePhoneStateListener, event)


        setOnActiveDataSubId(simSlotIndex, onActiveDataSubId)
        setOnDataConnectionState(simSlotIndex, onDataConnectionState)
        setOnCellInfo(simSlotIndex, onCellInfo)
        setOnSignalStrength(simSlotIndex, onSignalStrength)
        setOnServiceState(simSlotIndex, onServiceState)
        setOnCallState(simSlotIndex, onCallState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    public fun unregisterCallBack(simSlotIndex: Int) {
        val tm = uSimTelepnohyManagerList[simSlotIndex]
        val callback = uSimTelephonyCallbackList[simSlotIndex]
        if (callback == null || tm == null) {
            throw Exception("telephonyCallbackList[$simSlotIndex] is null")
        }
        try {
            telephonyManager.unregisterTelephonyCallback(callback.baseTelephonyCallback)
        } catch (e: SecurityException) {
            Logx.w("Permission issue during unregistering callback", e)
        } catch (e: IllegalArgumentException) {
            Logx.w("Invalid callback provided", e)
        }

        try {
            telephonyManager.unregisterTelephonyCallback(callback.baseGpsTelephonyCallback)
        } catch (e: SecurityException) {
            Logx.w("Permission issue during unregistering callback", e)
        } catch (e: IllegalArgumentException) {
            Logx.w("Invalid callback provided", e)
        }

        isRegistered[simSlotIndex] = false
    }

    /**
     * SDK_INT < Build.VERSION_CODES.S
     */
    public fun unregisterListen(simSlotIndex: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            throw Exception("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }
        val tm =uSimTelepnohyManagerList[simSlotIndex]
        val callback =uSimTelephonyCallbackList[simSlotIndex]
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
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnActiveDataSubId(simSlotIndex: Int, onActiveDataSubId: ((subId: Int) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnActiveDataSubId(onActiveDataSubId) }
            ?: throw Exception("setOnActiveDataSubId telephonyCallbackList[$simSlotIndex] is null")
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnDataConnectionState(simSlotIndex: Int, onDataConnectionState: ((state: Int, networkType: Int) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnDataConnectionState(onDataConnectionState) }
            ?: throw Exception("setOnDataConnectionState telephonyCallbackList[$simSlotIndex] is null")
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnCellInfo(simSlotIndex: Int, onCellInfo: ((currentCellInfo: CurrentCellInfo) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnCellInfo(onCellInfo) }
            ?: throw Exception("setOnCellInfo telephonyCallbackList[$simSlotIndex] is null")
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnSignalStrength(simSlotIndex: Int, onSignalStrength: ((currentSignalStrength: CurrentSignalStrength) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnSignalStrength(onSignalStrength) }
            ?: throw Exception("setOnSignalStrength telephonyCallbackList[$simSlotIndex] is null")
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnServiceState(simSlotIndex: Int, onServiceState: ((currentServiceState: CurrentServiceState) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnServiceState(onServiceState) }
            ?: throw Exception("setOnServiceState telephonyCallbackList[$simSlotIndex] is null")
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnCallState(simSlotIndex: Int, onCallState:  ((callState: Int, phoneNumber: String?) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnCallState(onCallState) }
            ?: throw Exception("setOnCallState telephonyCallbackList[$simSlotIndex] is null")
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnDisplayState(simSlotIndex: Int, onDisplay: ((telephonyDisplayInfo: TelephonyDisplayInfo) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnDisplay(onDisplay) }
            ?: throw Exception("setOnDisplayState telephonyCallbackList[$simSlotIndex] is null")
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    public fun setOnTelephonyNetworkType(simSlotIndex: Int, onTelephonyNetworkType: ((telephonyNetworkState: TelephonyNetworkState) -> Unit)? = null) {
        uSimTelephonyCallbackList[simSlotIndex]?.let { it.setOnTelephonyNetworkType(onTelephonyNetworkType) }
            ?: throw Exception("setOnTelephonyNetworkType telephonyCallbackList[$simSlotIndex] is null")
    }

    public fun isRegistered(simSlotIndex: Int):Boolean = isRegistered[simSlotIndex]?:false

    public override fun onDestroy() {
        super.onDestroy()
        uSimTelephonyCallbackList.forEach { key, value ->
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                unregisterCallBack(key)
            } else {
                unregisterListen(key)
            }
        }
    }
}
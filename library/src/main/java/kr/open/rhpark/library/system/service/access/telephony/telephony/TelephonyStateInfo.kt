package kr.open.rhpark.library.system.service.access.telephony.telephony

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.access.telephony.base.BaseSubscriptionState
import kr.open.rhpark.library.system.service.access.telephony.base.CommonTelephonyCallback
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentSignalStrength
import java.util.concurrent.Executor


public class TelephonyStateInfo(
    context: Context,
    telephonyManager: TelephonyManager,
    subscriptionManager: SubscriptionManager,
) : BaseSubscriptionState(
    context,
    listOf(READ_PHONE_STATE, READ_PHONE_NUMBERS, ACCESS_FINE_LOCATION),
    subscriptionManager,
    telephonyManager
) {

    private val telephonyCallback: CommonTelephonyCallback = CommonTelephonyCallback()

    private var isRegistered = false

    /**
     * return
     * @see TelephonyManager.SIM_STATE_%%
     */
    public fun getStatusForDefaultUSim(): Int = telephonyManager.simState

    @RequiresPermission(READ_PHONE_STATE)
    public fun getMccFromDefaultUSimString(): String? = getDefaultSubscriptionInfoSubId()?.let {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) it.mcc.toString()
        else it.mccString
    }


    @RequiresPermission(READ_PHONE_STATE)
    public fun getMncFromDefaultUSimString(): String? = getDefaultSubscriptionInfoSubId()?.let {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) it.mnc.toString()
        else it.mncString
    }

    @RequiresPermission(anyOf = [READ_PHONE_STATE, READ_PHONE_NUMBERS])
    public fun getPhoneNumberFromDefaultUSim(): String? =
        telephonyManager?.line1Number /*Required SDK Version 1 ~ 33 */
            ?: getDefaultSubscriptionInfoSubId()?.number /* number Required SDK Version 30 ~ */

    @RequiresPermission(READ_PHONE_STATE)
    public fun getDisplayNameFromDefaultUSim(): String? = getDefaultSubscriptionInfoSubId()?.displayName?.toString()

    @RequiresPermission(READ_PHONE_STATE)
    public fun getCountryIsoFromDefaultUSim(): String? = getDefaultSubscriptionInfoSimSlot()?.countryIso

    @RequiresPermission(READ_PHONE_STATE)
    public fun isNetworkRoamingFromDefaultUSim(): Boolean =
        getDefaultSubId()?.let { subscriptionManager.isNetworkRoaming(it) }
            ?: false

    /**
     * SDK_INT >= Build.VERSION_CODES.S
     */
    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(READ_PHONE_STATE)
    public fun registerCallBack(executor: Executor, isGpsOn: Boolean) {
        unregisterCallBack()
        if(isGpsOn) {
            telephonyManager.registerTelephonyCallback(executor, telephonyCallback.baseGpsTelephonyCallback)
        } else {
            telephonyManager.registerTelephonyCallback(executor, telephonyCallback.baseTelephonyCallback)
        }
        isRegistered = true
    }

    /**
     * SDK_INT < Build.VERSION_CODES.S
     */
    @RequiresPermission(READ_PHONE_STATE)
    public fun registerListen(isGpsOn:Boolean) {
        unregisterListen()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            throw UnsupportedOperationException("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }
        getDefaultSubId()?.let {
            telephonyManager.createForSubscriptionId(it)
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
        telephonyManager.listen(telephonyCallback.basePhoneStateListener,event)


        isRegistered = true
        Logx.d("isRegistered $isRegistered")
    }

    /**
     * SDK_INT >= Build.VERSION_CODES.S
     */
    @RequiresApi(Build.VERSION_CODES.S)
    public fun unregisterCallBack() {
        try {
            telephonyManager.unregisterTelephonyCallback(telephonyCallback.baseTelephonyCallback)
        } catch (e:Exception) {
            Logx.w("Failed to unregister listener", e)
        }

        try {
            telephonyManager.unregisterTelephonyCallback(telephonyCallback.baseGpsTelephonyCallback)
        } catch (e:Exception) {
            Logx.w("Failed to unregister listener", e)
        }
        isRegistered = false
    }

    /**
     * SDK_INT < Build.VERSION_CODES.S
     */
    public fun unregisterListen() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            throw UnsupportedOperationException("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }

        telephonyManager.listen(telephonyCallback.basePhoneStateListener, PhoneStateListener.LISTEN_NONE)
        isRegistered = false
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnActiveDataSubId(onActiveDataSubId: (subId: Int) -> Unit) {
        checkIfRegistered()
        this.telephonyCallback.setOnActiveDataSubId(onActiveDataSubId)
        this.telephonyCallback.setOnActiveDataSubId(null)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnDataConnectionState(onDataConnectionState: (state: Int, networkType: Int) -> Unit) {
        checkIfRegistered()
        this.telephonyCallback.setOnDataConnectionState(onDataConnectionState)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnCellInfo(onCellInfo: (currentCellInfo: CurrentCellInfo?) -> Unit) {
        checkIfRegistered()
        this.telephonyCallback.setOnCellInfo(onCellInfo)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnSignalStrength(onSignalStrength: (currentSignalStrength: CurrentSignalStrength?) -> Unit) {
        checkIfRegistered()
        this.telephonyCallback.setOnSignalStrength(onSignalStrength)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or TelephonyStateInfo.registerListen() first before using it.
     * **/
    @RequiresPermission(READ_PHONE_STATE)
    public fun setOnServiceState(onServiceState: (currentServiceState: CurrentServiceState?) -> Unit) {
        checkIfRegistered()
        this.telephonyCallback.setOnServiceState(onServiceState)
    }

    private fun checkIfRegistered() {
        if (!isRegistered) {
            throw IllegalStateException("You must call registerCallBack() or registerListen() first")
        }
    }
}
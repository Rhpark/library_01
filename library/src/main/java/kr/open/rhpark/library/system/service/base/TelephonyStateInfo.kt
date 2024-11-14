package kr.open.rhpark.library.system.service.base

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
import kr.open.rhpark.library.system.service.base.telephony_subscription.BaseSubscriptionState
import kr.open.rhpark.library.system.service.base.telephony_subscription.telephony.callback.CommonTelephonyCallback
import kr.open.rhpark.library.system.service.base.telephony_subscription.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.base.telephony_subscription.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.base.telephony_subscription.telephony.data.current.CurrentSignalStrength
import java.util.concurrent.Executor

public class TelephonyStateInfo(
    context: Context,
    telephonyManager: TelephonyManager,
    subscriptionManager: SubscriptionManager,
) : BaseSubscriptionState(
    context,
    listOf(
        READ_PHONE_STATE,
        READ_PHONE_NUMBERS,
        ACCESS_FINE_LOCATION
    ),
    subscriptionManager,
    telephonyManager
) {

    private val telephonyCallback: CommonTelephonyCallback = CommonTelephonyCallback()

    /**
     * return
     * @see TelephonyManager.SIM_STATE_%%
     */
    public fun getStatusForDefaultUSim(): Int = telephonyManager.simState

    /**
     * Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
     *
     */
    @RequiresPermission(READ_PHONE_STATE)
    public fun getMncFromDefaultUSim(): Int = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        getDefaultSubscriptionInfoSubId()?.let{ it.mnc  } ?: 0
    } else 0

    @RequiresPermission(READ_PHONE_STATE)
    public fun getMccFromDefaultUSimString(): String? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            getDefaultSubscriptionInfoSubId()?.let { it.mcc.toString() }?:null
        } else getDefaultSubscriptionInfoSubId()?.let { it.mccString } ?: null

    @RequiresPermission(READ_PHONE_STATE)
    public fun getMncFromDefaultUSimString(): String? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            getDefaultSubscriptionInfoSubId()?.let { it.mnc.toString() } ?: null
        } else getDefaultSubscriptionInfoSubId()?.let { it.mncString } ?: null

    @RequiresPermission(anyOf = [READ_PHONE_STATE, READ_PHONE_NUMBERS])
    public fun getPhoneNumberFromDefaultUSim(): String? =
        telephonyManager?.line1Number /* line1Number Required SDK Version 1 ~ 33 */
            ?: getDefaultSubscriptionInfoSubId()?.number /* number Required SDK Version 30 ~ 33 */

    @RequiresPermission(READ_PHONE_STATE)
    public fun getDisplayNameFromDefaultUSim(): String? =
        getDefaultSubscriptionInfoSubId()?.displayName?.toString()

    @RequiresPermission(READ_PHONE_STATE)
    public fun getCountryIsoFromDefaultUSim(): String? =
        getDefaultSubscriptionInfoSimSlot()?.countryIso

    public fun isNetworkRoamingFromDefaultUSim(): Boolean =
        getDefaultSubId()?.let { subscriptionManager.isNetworkRoaming(it) }
            ?: throw NoSuchMethodError("Can not read uSim Chip")


    /**
     * SDK_INT >= Build.VERSION_CODES.S
     */
    @RequiresApi(Build.VERSION_CODES.S)
    public fun registerCallBack(executor: Executor) {
        telephonyManager.registerTelephonyCallback(executor, telephonyCallback.getBaseTelephonyCallback())
    }

    /**
     * SDK_INT < Build.VERSION_CODES.S
     */
    public fun registerListen() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            throw UnsupportedOperationException("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }
        telephonyManager.listen(telephonyCallback.getBasePhoneStateListener(),
            PhoneStateListener.LISTEN_CELL_LOCATION or
                    PhoneStateListener.LISTEN_DATA_ACTIVITY or
                    PhoneStateListener.LISTEN_DATA_CONNECTION_STATE or
                    PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or
                    PhoneStateListener.LISTEN_CALL_STATE or
                    PhoneStateListener.LISTEN_ACTIVE_DATA_SUBSCRIPTION_ID_CHANGE or
                    PhoneStateListener.LISTEN_CELL_INFO)
    }

    /**
     * SDK_INT >= Build.VERSION_CODES.S
     */
    @RequiresApi(Build.VERSION_CODES.S)
    public fun unregisterCallBack() {
        try {
            telephonyManager.unregisterTelephonyCallback(telephonyCallback.getBaseTelephonyCallback())
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    /**
     * SDK_INT < Build.VERSION_CODES.S
     */
    public fun unregisterListen() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            throw UnsupportedOperationException("SDK_INT >= Build.VERSION_CODES.S Your Version is ${Build.VERSION.SDK_INT}")
        }

        try {
            telephonyManager.listen(
                telephonyCallback.getBasePhoneStateListener(), PhoneStateListener.LISTEN_NONE
            )
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnActiveDataSubId(onActiveDataSubId: (subId: Int) -> Unit) {
        this.telephonyCallback.setOnActiveDataSubId(onActiveDataSubId)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnDataConnectionState(onDataConnectionState: (state: Int, networkType: Int) -> Unit) {
        this.telephonyCallback.setOnDataConnectionState(onDataConnectionState)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnCellInfo(onCellInfo: (currentCellInfo: CurrentCellInfo?) -> Unit) {
        this.telephonyCallback.setOnCellInfo(onCellInfo)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnSignalStrength(onSignalStrength: (currentSignalStrength: CurrentSignalStrength?) -> Unit) {
        this.telephonyCallback.setOnSignalStrength(onSignalStrength)
    }

    /**
     * You must call
     * TelephonyStateInfo.registerCallBack() or
     * TelephonyStateInfo.registerListen()
     * first before using it.
     * **/
    public fun setOnServiceState(onServiceState: (currentServiceState: CurrentServiceState?) -> Unit) {
        this.telephonyCallback.setOnServiceState(onServiceState)
    }

}
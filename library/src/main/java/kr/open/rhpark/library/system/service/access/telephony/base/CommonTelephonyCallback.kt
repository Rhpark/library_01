package kr.open.rhpark.library.system.service.access.telephony.base

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_PHONE_STATE
import android.os.Build
import android.telephony.CellInfo
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.SignalStrength
import android.telephony.TelephonyCallback
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.access.telephony.data.current.CurrentSignalStrength

/**
 * Using for telephonyManager.registerTelephonyCallback or telephonyManager.listen
 *
 *
 * You must call
 * TelephonyStateInfo.registerCallBack() or
 * TelephonyStateInfo.registerListen()
 * first before using it.
 *
 */
public open class CommonTelephonyCallback() {

    /***************************
     * CallBack Listener List  *
     ***************************/
    private var onActiveDataSubId: ((subId: Int) -> Unit)? = null

    private var onDataConnectionState: ((state: Int, networkType: Int) -> Unit)? = null

    private var onCellInfo: ((currentCellInfo: CurrentCellInfo) -> Unit)? = null

    private var onSignalStrength: ((currentSignalStrength: CurrentSignalStrength) -> Unit)? = null

    private var onServiceState: ((currentServiceState: CurrentServiceState) -> Unit)? = null

    private var onCallState: ((callState: Int, phoneNumber: String?) -> Unit)? = null


    @delegate:RequiresApi(Build.VERSION_CODES.S)
    public val baseTelephonyCallback: BaseTelephonyCallback by lazy { BaseTelephonyCallback() }

    @delegate:RequiresApi(Build.VERSION_CODES.S)
    public val baseGpsTelephonyCallback: BaseGpsTelephonyCallback by lazy { BaseGpsTelephonyCallback() }

    public val basePhoneStateListener: BasePhoneStateListener by lazy { BasePhoneStateListener()}


    public fun setOnActiveDataSubId(onActiveDataSubId: ((subId: Int) -> Unit)?) {
        this.onActiveDataSubId = onActiveDataSubId
    }

    public fun setOnDataConnectionState(onDataConnectionState: ((state: Int, networkType: Int) -> Unit)?) {
        this.onDataConnectionState = onDataConnectionState
    }

    public fun setOnCellInfo(onCellInfo: ((currentCellInfo: CurrentCellInfo) -> Unit)?) {
        this.onCellInfo = onCellInfo
    }

    public fun setOnSignalStrength(onSignalStrength: ((currentSignalStrength: CurrentSignalStrength) -> Unit)?) {
        this.onSignalStrength = onSignalStrength
    }

    public fun setOnServiceState(onServiceState: ((currentServiceState: CurrentServiceState) -> Unit)?) {
        this.onServiceState = onServiceState
    }

    public fun setOnCallState(onCallState: ((callState: Int, phoneNumber: String?) -> Unit)?) {
        this.onCallState = onCallState
    }

    /**
     * Using for telephonyManager.registerTelephonyCallback
     */
    @RequiresApi(Build.VERSION_CODES.S)
    public open inner class BaseTelephonyCallback : TelephonyCallback(),
        TelephonyCallback.DataConnectionStateListener,
        TelephonyCallback.ServiceStateListener,
        TelephonyCallback.SignalStrengthsListener,
        TelephonyCallback.CallStateListener,
        TelephonyCallback.ActiveDataSubscriptionIdListener {

        /**
         * Using for telephonyManager.registerTelephonyCallback onDataConnectionStateChanged
         */
        public override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
            onDataConnectionState?.invoke(state, networkType)
        }

        override fun onCallStateChanged(state: Int) {
            onCallState?.invoke(state,null)
        }

        /**
         * Using for telephonyManager.registerTelephonyCallback onServiceStateChanged
         */
        public override fun onServiceStateChanged(serviceState: ServiceState) {
            onServiceState?.invoke(CurrentServiceState(serviceState))
        }

        /**
         * Using for telephonyManager.registerTelephonyCallback onSignalStrengthsChanged
         */
        public override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
            onSignalStrength?.invoke(CurrentSignalStrength(signalStrength))
        }

        /**
         * Using for telephonyManager.registerTelephonyCallback onActiveDataSubscriptionIdChanged
         */
        public override fun onActiveDataSubscriptionIdChanged(subId: Int) {
            onActiveDataSubId?.invoke(subId)
        }
    }

    /**
     * Declared separately as it may not respond depending on GPS status
     * GPS 상태에 따라 응답하지 않을 수 있으므로 별도로 선언.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    public open inner class BaseGpsTelephonyCallback : BaseTelephonyCallback(),
        TelephonyCallback.CellInfoListener {

        /**
         * Using for telephonyManager.registerTelephonyCallback onCellInfoChanged
         */
        @RequiresPermission(allOf = [READ_PHONE_STATE, ACCESS_FINE_LOCATION])
        override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>) {
            onCellInfo?.invoke(CurrentCellInfo(cellInfo))
        }
    }

    /**
     * Required permission
     * default android.Manifest.permission.READ_PHONE_STATE
     * onCellInfoChanged android.Manifest.permission.ACCESS_FINE_LOCATION
     * Check the change active data SubscriptionId (Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
     */
    public open inner class BasePhoneStateListener : PhoneStateListener() {

        /**
         * RequiresPermission android.Manifest.permission.READ_PHONE_STATE
         */
        @RequiresPermission(READ_PHONE_STATE)
        public override fun onActiveDataSubscriptionIdChanged(subId: Int) {
            super.onActiveDataSubscriptionIdChanged(subId)
            onActiveDataSubId?.invoke(subId)
        }

        override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
            super.onDataConnectionStateChanged(state, networkType)
            onDataConnectionState?.invoke(state, networkType)
        }

        //
        public override fun onServiceStateChanged(serviceState: ServiceState?) {
            super.onServiceStateChanged(serviceState)
            serviceState?.let { data -> onServiceState?.invoke(CurrentServiceState(data)) }

        }

        public override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
            super.onSignalStrengthsChanged(signalStrength)
            signalStrength?.let { data -> onSignalStrength?.invoke(CurrentSignalStrength(data)) }
        }

        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            onCallState?.invoke(state, phoneNumber)
        }

        /**
         * Requires Permission
         * Manifest.permission.READ_PHONE_STATE,
         * Manifest.permission.ACCESS_FINE_LOCATION
         *
         * Using for telephonyManager.registerTelephonyListen onCellInfoChanged
         */
        @RequiresPermission(allOf = [READ_PHONE_STATE, ACCESS_FINE_LOCATION])
        public override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
            super.onCellInfoChanged(cellInfo)
            cellInfo?.let { data-> onCellInfo?.invoke(CurrentCellInfo(data)) }
        }
    }
}

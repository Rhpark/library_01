package kr.open.rhpark.app.activity.telephony

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyDisplayInfo
import androidx.annotation.RequiresPermission
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityTelephonyBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentSignalStrength
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class TelephonyActivity :
    BaseBindingActivity<ActivityTelephonyBinding>(R.layout.activity_telephony) {

    private fun getTelephonyStateInfo() = systemServiceManagerInfo.telephonyStateInfo

    private var onActiveDataSubId: ((subId: Int) -> Unit)? = { subId -> binding.tvActiveDataSubId.text = "subId = $subId\n\n" }

    private var onDataConnectionState: ((state: Int, networkType: Int) -> Unit)? =
        { state, networkType -> binding.tvSimState.text = "state = $state, networkType = $networkType\n\n, isNr = ${getTelephonyStateInfo().isNrConnected()}" }

    private var onCellInfo: ((currentCellInfo: CurrentCellInfo) -> Unit)? =
        { currentCellInfo -> binding.tvCellInfo.text = "currentCellInfo = $currentCellInfo\n\n" }

    private var onSignalStrength: ((currentSignalStrength: CurrentSignalStrength) -> Unit)? =
        { currentSignalStrength -> binding.tvSignalStrength.text = "currentSignalStrength = $currentSignalStrength\n\n" }

    private var onServiceState: ((currentServiceState: CurrentServiceState) -> Unit)? =
        { currentServiceState -> binding.tvServiceState.text = "currentServiceState = $currentServiceState\n\n" }

    private var onCallState: ((callState: Int, phoneNumber: String?) -> Unit)? =
        { callState,phoneNumber -> binding.tvCallState.text = "callState = $callState, phoneNumber $phoneNumber \n\n" }

    private var onDisplayInfo: ((telephonyDisplayInfo: TelephonyDisplayInfo) -> Unit)? =
        {  t-> binding.tvDisplayInfo.text = "DisplayInfo $t, isNr = ${getTelephonyStateInfo().isNrConnected()}" }

    private fun getGpsStateInfo() = systemServiceManagerInfo.locationStateInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(listOf(ACCESS_FINE_LOCATION, READ_PHONE_STATE, READ_PHONE_NUMBERS)) { grantedPermissions, deniedPermissions ->
            Logx.d("grantedPermissions $grantedPermissions, \n deniedPermissions $deniedPermissions")
            if (deniedPermissions.isEmpty()) {
                Logx.d("getTelephonyStateInfo().isCanReadSimInfo() ${getTelephonyStateInfo().isCanReadSimInfo()}")
                if(!getTelephonyStateInfo().isCanReadSimInfo()) {
                    toast.showMsgShort("Can not using TelephonyManager,\nplease check the SIM chip is inserted")
                    return@requestPermissions
                }

                lifecycleScope.launch {
                    getTelephonyStateInfo().run {
                        binding.tvMccMnc.text = "Mcc Mnc = ${getMccFromDefaultUSimString()},${getMncFromDefaultUSimString()}\n\n"
                        binding.tvPhoneNumber.text = "PhoneNumber = ${getPhoneNumberFromDefaultUSim()}\n\n"
                        binding.tvDisplayName.text = "DisplayName = ${getDisplayNameFromDefaultUSim()}\n\n"
                        binding.tvCountryIso.text = "CountryIso = ${getDisplayNameFromDefaultUSim()}\n\n"
                        binding.tvNetworkRoaming.text = "NetworkRoaming = ${isNetworkRoamingFromDefaultUSim()}\n\n"
                    }
                    registers()
                }
            } else {
                toast.showMsgShort("Permission denied $deniedPermissions")
            }
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registers() {
        registerTelephony(getGpsStateInfo().isGpsEnabled())
        registerLocationState()
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerTelephony(gpsState:Boolean) {
        getTelephonyStateInfo().run {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                registerCallBack(applicationContext.mainExecutor, gpsState,
                    onActiveDataSubId =  onActiveDataSubId, onDataConnectionState =  onDataConnectionState, onCellInfo = onCellInfo,
                    onSignalStrength = onSignalStrength, onServiceState =  onServiceState, onCallState = onCallState, onDisplayInfo = onDisplayInfo)
            } else {
                registerListen(gpsState,
                    onActiveDataSubId =  onActiveDataSubId, onDataConnectionState =  onDataConnectionState, onCellInfo = onCellInfo,
                    onSignalStrength = onSignalStrength, onServiceState =  onServiceState, onCallState = onCallState, onDisplayInfo = onDisplayInfo)
            }
        }
    }

    /**
     * This is needed because of TelephonyCallback.CellInfoListener(Telephony.registerCallBack)
     * or
     * PhoneStateListener.LISTEN_CELL_INFO(Telephony.registerListen).
     */
    @RequiresPermission(READ_PHONE_STATE)
    private fun registerLocationState() {
        getGpsStateInfo().registerGpsState{ isEnabled -> registerTelephony(isEnabled) }
    }

    private fun unregisterLocationState() { getGpsStateInfo().unregisterGpsState() }

    override fun onDestroy() {
        super.onDestroy()
        getTelephonyStateInfo().onDestroy()
        unregisterLocationState()
    }
}
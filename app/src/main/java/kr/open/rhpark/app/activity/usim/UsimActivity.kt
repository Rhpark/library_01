package kr.open.rhpark.app.activity.usim

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityUsimBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.access.internet.telephony.data.current.CurrentSignalStrength
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class UsimActivity : BaseBindingActivity<ActivityUsimBinding>(R.layout.activity_usim) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(listOf(ACCESS_FINE_LOCATION, READ_PHONE_STATE, READ_PHONE_NUMBERS)) { grantedPermissions, deniedPermissions ->
            Logx.d("grantedPermissions $grantedPermissions, \n deniedPermissions $deniedPermissions")
            if (deniedPermissions.isEmpty()) {
                if(!getUsimStateInfo().isCanReadSimInfo()) {
                    toast.showMsgShort("Can not using TelephonyManager,\nplease check the SIM chip is inserted")
                    return@requestPermissions
                }
                lifecycleScope.launch {
                    updateSimInfo()
                    registers()
                }
            } else { toast.showMsgShort("Permission denied $deniedPermissions") }
        }
    }

    private fun getUsimStateInfo() = systemServiceManagerInfo.usimStateInfo

    private fun getGpsStateInfo() = systemServiceManagerInfo.locationStateInfo

    @RequiresPermission(READ_PHONE_STATE)
    private fun registers() {
        registerTelephony(getGpsStateInfo().isGpsEnabled())
        registerLocationState()
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerTelephony(gpsState:Boolean) {
        getUsimStateInfo().run {
            val activeUsimList = getActiveSimSlotIndexList()
            activeUsimList.forEach { simId->

                val onActiveDataSubId: ((subId: Int) -> Unit)? =
                    { subId:Int -> binding.tvActiveDataSubId.text="simId $simId\n  subId $subId\n\n"; updateSimInfo() }
                val onDataConnectionState: ((state: Int, networkType: Int) -> Unit)? =
                    { state:Int,networkType:Int -> binding.tvSimState.text="simId $simId\n  state $state, networkType $networkType]\n\n"; updateSimInfo();}
                val onCellInfo: ((currentCellInfo: CurrentCellInfo) -> Unit)? =
                    { currentCellInfo: CurrentCellInfo -> binding.tvCellInfo.text="simId $simId\n  currentCellInfo $currentCellInfo\n\n"}
                val onSignalStrength: ((currentSignalStrength: CurrentSignalStrength) -> Unit)? =
                    { currentSignalStrength: CurrentSignalStrength -> binding.tvSignalStrength.text="simId $simId\n  currentSignalStrength $currentSignalStrength\n\n"}
                val onServiceState: ((currentServiceState: CurrentServiceState) -> Unit)? =
                    { currentServiceState: CurrentServiceState -> binding.tvServiceState.text="simId $simId\n  currentServiceState $currentServiceState\n\n"}
                val onCallState: ((callState: Int, phoneNumber: String?) -> Unit)? =
                    { callState:Int,phoneNumber:String? -> binding.tvCallState.text="simId $simId\n  callState $callState, phoneNumber $phoneNumber\n\n"}

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    registerCallBack(simId, applicationContext.mainExecutor, gpsState, onActiveDataSubId, onDataConnectionState, onCellInfo, onSignalStrength, onServiceState, onCallState)
                } else {
                    registerListen(simId, gpsState, onActiveDataSubId, onDataConnectionState, onCellInfo, onSignalStrength, onServiceState, onCallState)
                }
            }
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun updateSimInfo() {
        getUsimStateInfo().run {
            val activeUsimList = getActiveSimSlotIndexList()
            binding.tvSimCount.text = "Active Sim Count ${activeUsimList.size}"
            binding.tvSimId.text = "Sim Id ${activeUsimList.toList()}"
            binding.tvMaximumUSimCount.text = "MaximumUSimCount ${getUsimStateInfo().getMaximumUSimCount()}"
            binding.tvIsAbleEsim.text = "isAbleEsim ${getUsimStateInfo().isAbleEsim()}"

            var mccmnc = ""
            var phoneNumber = ""
            var state = ""
            var countryIso = ""
            var networkRoaming = ""
            activeUsimList.forEach { simId->
                mccmnc += "[SimSloat $simId, Mcc Mnc ${getMcc(simId)},${getMnc(simId)}] \n "
                phoneNumber +="[SimSloat $simId, PhoneNumber = ${getPhoneNumber(simId)}] \n"
                state += "[SimSloat $simId, Sim State ${getMaximumActiveSimStatus(simId)}] \n"
                countryIso += "[SimSloat $simId, countryIso ${getCountryIso(simId)}] \n"
                networkRoaming += "[SimSloat $simId, networkRoaming ${isNetworkRoaming(simId)}] \n"
            }

            binding.tvMccMnc.text = "MCC/MNC : $mccmnc"
            binding.tvPhoneNumber.text = "phoneNumber : $phoneNumber"
            binding.tvSimState.text = "state $state"
            binding.tvCountryIso.text = "countryIso $countryIso"
            binding.tvNetworkRoaming.text = "networkRoaming $networkRoaming"
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerLocationState() { getGpsStateInfo().registerGpsState{ isEnabled -> registerTelephony(isEnabled) } }

    override fun onDestroy() {
        super.onDestroy()
        getUsimStateInfo().onDestroy()
        getGpsStateInfo().unregisterGpsState()
    }
}
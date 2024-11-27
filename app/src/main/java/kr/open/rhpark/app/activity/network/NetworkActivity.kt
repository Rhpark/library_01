package kr.open.rhpark.app.activity.network

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyDisplayInfo
import androidx.annotation.RequiresPermission
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityNetworkBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.access.network.connectivity.data.NetworkCapabilitiesData
import kr.open.rhpark.library.system.service.access.network.connectivity.data.NetworkLinkPropertiesData
import kr.open.rhpark.library.system.service.access.network.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.system.service.access.network.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.system.service.access.network.telephony.data.current.CurrentSignalStrength
import kr.open.rhpark.library.system.service.access.network.telephony.data.state.TelephonyNetworkState

import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class NetworkActivity : BaseBindingActivity<ActivityNetworkBinding>(R.layout.activity_network) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(getPermissions()) { grantedPermissions, deniedPermissions ->
            if (deniedPermissions.isEmpty()) {
                initNetworkStateInfo()
            } else {
                Logx.d("deniedPermissions $deniedPermissions")
                toast.showMsgShort("deniedPermissions $deniedPermissions ")
            }
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun initNetworkStateInfo() {
        registerNetworkCallback()
        registerDefaultNetworkCallback()
        registerTelephonyCallback()
        registerLocationState()
        binding.tvTelephonyMaximumActiveSimCount.text = "MaximumUSimCount ${getNetworkStateInfo().getMaximumUSimCount()}\n\n"
        binding.tvTelephonyIsAbleEsim.text = "isAble Esim = ${getNetworkStateInfo().isESimSupport()}\n\n"
    }

    private fun registerNetworkCallback() {
        getNetworkStateInfo().registerNetworkCallback(Handler(Looper.getMainLooper()),
            onNetworkAvailable = { network: Network ->
                binding.tvConnectState.text = "Default NetworkAvailable - network $network, " +
                        "is Wifi Connect ${getNetworkStateInfo().isConnectedWifi()}, isMobileConnect ${getNetworkStateInfo().isConnectedMobile()}\n\n" },
            onNetworkLosing = { network: Network, maxMsToLive: Int ->
                binding.tvConnectState.text = "NetworkLosing - network $network maxMsToLive $maxMsToLive\n\n" },
            onNetworkLost = { network: Network ->
                binding.tvConnectState.text = "NetworkLost - network $network\n\n" },
            onNetworkCapabilitiesChanged = { network: Network, networkCapabilities: NetworkCapabilitiesData ->
                binding.tvCapabilities.text = "networkCapabilities\n ${networkCapabilities.toResString()}" },
            onLinkPropertiesChanged = { network: Network, linkProperties: NetworkLinkPropertiesData ->
                binding.tvLinkProperties.text = "LinkProperties\n ${linkProperties.toResString()}"},
            onBlockedStatusChanged = { network: Network, blocked: Boolean ->
                Logx.d("onBlockedStatusChanged network $network blocked $blocked")
            }
        )
    }

    private fun registerDefaultNetworkCallback() {
        getNetworkStateInfo().registerDefaultNetworkCallback(Handler(Looper.getMainLooper()),
            onNetworkAvailable = { network: Network ->
                binding.tvConnectDefaultState.text = "Default NetworkAvailable - network $network, " +
                        "is Wifi Connect ${getNetworkStateInfo().isConnectedWifi()}, isMobileConnect ${getNetworkStateInfo().isConnectedMobile()}\n\n" },
            onNetworkLosing = { network: Network, maxMsToLive: Int ->
                binding.tvConnectDefaultState.text = "Default NetworkLosing - network $network maxMsToLive $maxMsToLive\n\n" },
            onNetworkLost = { network: Network ->
                binding.tvConnectDefaultState.text = "Default NetworkLost - network $network\n\n" },
            onNetworkCapabilitiesChanged = { network: Network, networkCapabilities: NetworkCapabilitiesData ->
                binding.tvDefaultCapabilities.text = "Default networkCapabilities\n ${networkCapabilities.toResString()}" },
            onLinkPropertiesChanged = { network: Network, linkProperties: NetworkLinkPropertiesData ->
                binding.tvDefaultLinkProperties.text = "Default LinkProperties\n ${linkProperties.toResString()}"},
            onBlockedStatusChanged = { network: Network, blocked: Boolean ->
                Logx.d("onBlockedStatusChanged $blocked")
            }
        )
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerTelephonyCallback(isGpsOn: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getNetworkStateInfo().registerTelephonyCallBackFromDefaultUSim(
                this@NetworkActivity.mainExecutor, isGpsOn,
                onActiveDataSubId = { subId: Int ->
                    binding.tvTelephonyActiveSubId.text = "\n Telephony\n\n ActiveDataSubId\n $subId\n\n"
                    binding.tvTelephonyActiveSimCount.text = "getActiveSimCount ${getNetworkStateInfo().getActiveSimCount()}"
                    binding.tvTelephonyActiveSimStatus.text = "Sim Status ${getSimStatus()}"
                },
                onDataConnectionState = { state: Int, networkType: Int ->
                    binding.tvTelephonyConnectState.text = "Default ConnectState \n $state , networkType $networkType\n\n"
                },
                onCellInfo = { currentCellInfo: CurrentCellInfo ->
                    binding.tvTelephonyCellInfo.text = "Default CellInfo \n ${currentCellInfo.toResString()}\n"
                },
                onSignalStrength = { currentSignalStrength: CurrentSignalStrength ->
                    binding.tvTelephonySignalStrength.text = "Default SignalStrength\n ${currentSignalStrength.toResString()}\n"
                },
                onServiceState = { currentServiceState: CurrentServiceState ->
                    binding.tvTelephonyServiceState.text = "Default ServiceState\n ${currentServiceState.toResString()}\n\n"
                },
                onCallState = { callState: Int, phoneNumber: String? ->
                    binding.tvTelephonyCallState.text = "Default callState\n $callState, phoneNumber $phoneNumber\n\n"
                },
                onDisplayInfo = { telephonyDisplayInfo: TelephonyDisplayInfo ->
                    binding.tvTelephonyDisplayInfo.text = "Default DisplayInfo\n $telephonyDisplayInfo\n\n"
                },
                onTelephonyNetworkState = { telephonyNetworkState: TelephonyNetworkState ->
                    binding.tvTelephonyNetworkState.text = "Default TelephonyNetworkState\n $telephonyNetworkState\n\n"
                })
        } else {
            getNetworkStateInfo().registerTelephonyListenFromDefaultUSim(isGpsOn,
                onActiveDataSubId = { subId: Int ->
                    binding.tvTelephonyActiveSubId.text = "\n Telephony\n\n ActiveDataSubId\n $subId\n\n"
                    binding.tvTelephonyActiveSimCount.text = "getActiveSimCount ${getNetworkStateInfo().getActiveSimCount()}"
                    binding.tvTelephonyActiveSimStatus.text = "Sim Status ${getSimStatus()}"
                },
                onDataConnectionState = { state: Int, networkType: Int ->
                    binding.tvTelephonyConnectState.text = "Default ConnectState \n $state , networkType $networkType\n\n"
                },
                onCellInfo = { currentCellInfo: CurrentCellInfo ->
                    binding.tvTelephonyCellInfo.text = "Default CellInfo \n ${currentCellInfo.toResString()}\n"
                },
                onSignalStrength = { currentSignalStrength: CurrentSignalStrength ->
                    binding.tvTelephonySignalStrength.text = "Default SignalStrength\n ${currentSignalStrength.toResString()}\n"
                },
                onServiceState = { currentServiceState: CurrentServiceState ->
                    binding.tvTelephonyServiceState.text = "Default ServiceState\n ${currentServiceState.toResString()}\n\n"
                },
                onCallState = { callState: Int, phoneNumber: String? ->
                    binding.tvTelephonyCallState.text = "Default callState\n $callState, phoneNumber $phoneNumber\n\n"
                },
                onDisplayInfo = { telephonyDisplayInfo: TelephonyDisplayInfo ->
                    binding.tvTelephonyDisplayInfo.text = "Default DisplayInfo\n $telephonyDisplayInfo\n\n"
                },
                onTelephonyNetworkState = { telephonyNetworkState: TelephonyNetworkState ->
                    binding.tvTelephonyNetworkState.text = "Default TelephonyNetworkState\n $telephonyNetworkState\n\n"
                })
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerLocationState() { getGpsStateInfo().registerGpsState{ isEnabled -> registerTelephonyCallback(isEnabled) } }

    override fun onDestroy() {
        super.onDestroy()
        getNetworkStateInfo().onDestroy()
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun getSimStatus(): String {
        var simStatus = ""
        getNetworkStateInfo().getActiveSimSlotIndexList().forEachIndexed { index, i ->
            simStatus += "index $index, status $i\n"
        }
        return simStatus
    }

    private fun getPermissions() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            ACCESS_NETWORK_STATE,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
            READ_PHONE_STATE,
            READ_PHONE_NUMBERS
        )
    } else {
        listOf(
            ACCESS_NETWORK_STATE,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
            READ_PHONE_STATE,
        )
    }
    private fun getNetworkStateInfo() = systemServiceManagerInfo.networkInfo
    private fun getGpsStateInfo() = systemServiceManagerInfo.locationStateInfo
}
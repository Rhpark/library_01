package kr.open.rhpark.app.activity.network

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyDisplayInfo
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityNetworkBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.domain.common.systemmanager.info.location.LocationStateEvent
import kr.open.rhpark.library.domain.common.systemmanager.info.network.connectivity.data.NetworkCapabilitiesData
import kr.open.rhpark.library.domain.common.systemmanager.info.network.connectivity.data.NetworkLinkPropertiesData
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.current.CurrentCellInfo
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.current.CurrentServiceState
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.current.CurrentSignalStrength
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.state.TelephonyNetworkState
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getLocationStateInfo
import kr.open.rhpark.library.util.extensions.context.getNetworkStateInfo
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort
import kr.open.rhpark.library.util.extensions.conditional.sdk_version.checkSdkVersion

class NetworkActivity : BaseBindingActivity<ActivityNetworkBinding>(R.layout.activity_network) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(getPermissions()) { requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
            if(deniedPermissions.isEmpty()) {
                initNetworkStateInfo()
            } else {
                toastShowShort("deniedPermissions $deniedPermissions ")
            }
        }
    }

    @SuppressLint("MissingPermission")
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
            onNetworkAvailable = { network: Network ->  updateAvailable("NetworkAvailable ",binding.tvConnectState, network) },
            onNetworkLosing = { network: Network, maxMsToLive: Int ->  updateAvailable("onNetworkLosing ",binding.tvConnectState, network)},
            onNetworkLost = { network: Network -> updateAvailable("onNetworkLost ",binding.tvConnectState, network)},
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
            onNetworkAvailable = { network: Network -> updateAvailable("Default NetworkAvailable ",binding.tvConnectDefaultState, network) },
            onNetworkLosing = { network: Network, maxMsToLive: Int -> updateAvailable("Default NetworkLosing ",binding.tvConnectDefaultState, network) },
            onNetworkLost = { network: Network -> updateAvailable("Default NetworkLost ",binding.tvConnectDefaultState, network) },
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
        Logx.d("isGpsOn $isGpsOn")
        try {
            checkSdkVersion(Build.VERSION_CODES.S,
                positiveWork = {
                    getNetworkStateInfo().registerTelephonyCallBackFromDefaultUSim(
                        this@NetworkActivity.mainExecutor, isGpsOn,
                        onActiveDataSubId = { subId: Int -> updateActiveDataSubId(subId) },
                        onDataConnectionState = { state: Int, networkType: Int -> updateConnectState(state,networkType) },
                        onCellInfo = { currentCellInfo: CurrentCellInfo -> updateCellInfo(currentCellInfo) },
                        onSignalStrength = { currentSignalStrength: CurrentSignalStrength ->updateSignalStrength(currentSignalStrength) },
                        onServiceState = { currentServiceState: CurrentServiceState -> updateServiceState(currentServiceState) },
                        onCallState = { callState: Int, phoneNumber: String? ->updateCallState(callState,phoneNumber) },
                        onDisplayInfo = { telephonyDisplayInfo: TelephonyDisplayInfo -> updateDisplayInfo(telephonyDisplayInfo) },
                        onTelephonyNetworkState = { telephonyNetworkState: TelephonyNetworkState -> updateNetworkState(telephonyNetworkState) })
                },
                negativeWork = {
                    getNetworkStateInfo().registerTelephonyListenFromDefaultUSim(isGpsOn,
                        onActiveDataSubId = { subId: Int -> updateActiveDataSubId(subId) },
                        onDataConnectionState = { state: Int, networkType: Int -> updateConnectState(state,networkType) },
                        onCellInfo = { currentCellInfo: CurrentCellInfo -> updateCellInfo(currentCellInfo) },
                        onSignalStrength = { currentSignalStrength: CurrentSignalStrength ->updateSignalStrength(currentSignalStrength) },
                        onServiceState = { currentServiceState: CurrentServiceState -> updateServiceState(currentServiceState) },
                        onCallState = { callState: Int, phoneNumber: String? ->updateCallState(callState,phoneNumber) },
                        onDisplayInfo = { telephonyDisplayInfo: TelephonyDisplayInfo -> updateDisplayInfo(telephonyDisplayInfo) },
                        onTelephonyNetworkState = { telephonyNetworkState: TelephonyNetworkState -> updateNetworkState(telephonyNetworkState) })
                }
            )
        } catch (e:IllegalArgumentException) {
            e.printStackTrace()
            toastShowShort(e.toString())
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerLocationState() {
        getGpsStateInfo().apply {
            registerLocation()
            lifecycleScope.launch {
                sfUpdate.collect{ type->
                    when(type) {
                        is LocationStateEvent.OnGpsEnabled ->registerTelephonyCallback(type.isEnabled)
                        else -> {}
                    }
                }
            }
        }
    }

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

    private fun getPermissions() = checkSdkVersion(Build.VERSION_CODES.S,
        positiveWork = {
            listOf(
                ACCESS_NETWORK_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION,
                READ_PHONE_STATE, READ_PHONE_NUMBERS
            )
        }, negativeWork = {
            listOf(
                ACCESS_NETWORK_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_PHONE_STATE,
            )
        })


    @RequiresPermission(READ_PHONE_STATE)
    private fun updateActiveDataSubId(subId: Int) {
        binding.tvTelephonyActiveSubId.text = "\n Telephony\n\n ActiveDataSubId\n $subId\n\n"
        binding.tvTelephonyActiveSimCount.text = "getActiveSimCount ${getNetworkStateInfo().getActiveSimCount()}"
        binding.tvTelephonyActiveSimStatus.text = "Sim Status ${getSimStatus()}"
    }

    private fun updateAvailable(msg:String ,tv: TextView, network: Network) {
        tv.text = "$msg network $network, " +
                "is Wifi Connect ${getNetworkStateInfo().isConnectedWifi()}, isMobileConnect ${getNetworkStateInfo().isConnectedMobile()}\n\n"
    }
    private fun updateCellInfo(currentCellInfo: CurrentCellInfo) {
        binding.tvTelephonyCellInfo.text = "Default CellInfo \n ${currentCellInfo.toResString()}\n"
    }

    private fun updateSignalStrength(currentSignalStrength: CurrentSignalStrength) {
        binding.tvTelephonySignalStrength.text = "Default SignalStrength\n ${currentSignalStrength.toResString()}\n"
    }

    private fun updateConnectState(state: Int, networkType: Int) {
        binding.tvTelephonyConnectState.text = "Default ConnectState \n $state , networkType $networkType\n\n"
    }

    private fun updateServiceState(currentServiceState: CurrentServiceState) {
        binding.tvTelephonyServiceState.text = "Default ServiceState\n ${currentServiceState.toResString()}\n\n"
    }

    private fun updateCallState(callState: Int, phoneNumber: String?) {
        binding.tvTelephonyCallState.text = "Default callState\n $callState, phoneNumber $phoneNumber\n\n"
    }

    private fun updateDisplayInfo(telephonyDisplayInfo: TelephonyDisplayInfo) {
        binding.tvTelephonyDisplayInfo.text = "Default DisplayInfo\n $telephonyDisplayInfo\n\n"
    }

    private fun updateNetworkState(telephonyNetworkState: TelephonyNetworkState) {
        binding.tvTelephonyNetworkState.text = "Default TelephonyNetworkState\n $telephonyNetworkState\n\n"
    }
    private fun getNetworkStateInfo() = applicationContext.getNetworkStateInfo()
    private fun getGpsStateInfo() = applicationContext.getLocationStateInfo(lifecycleScope)
}
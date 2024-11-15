package kr.open.rhpark.app.activity.network

import android.Manifest
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityNetworkBinding

import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class NetworkActivity : BaseBindingActivity<ActivityNetworkBinding>(R.layout.activity_network) {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestPermissions(
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                listOf(Manifest.permission.ACCESS_NETWORK_STATE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_PHONE_NUMBERS)
//            } else {
//                listOf(Manifest.permission.ACCESS_NETWORK_STATE,
//                    Manifest.permission.READ_PHONE_STATE)
//            }
//        ) { grantedPermissions, deniedPermissions ->
//            Logx.d("grantedPermissions $grantedPermissions, \n deniedPermissions $deniedPermissions")
////            if(deniedPermissions.isEmpty()) {
//            if(true) {
//                binding.btnSimState.setOnClickListener {
//                    binding.tvSimState.text = getSimStatus() + getConnectCheck()
//                }
//                registerDefaultCallback()
//                systemServiceManagerInfo.onBaseTelephonyCallback.addOnCellInfo(object: TelephonyImp.OnCellInfo{
//                    override fun onCellInfoChanged(currentCellInfo: CurrentCellInfo?) {
//                        lifecycleScope.launch {
//                            currentCellInfo?.let {
//                                val res = "CellInfo = cellDataCdmaList "+it.cellDataCdmaList + "\n\n" +
//                                        "cellDataGsmList "+it.cellDataGsmList + "\n\n" +
//                                        "cellDataLteList "+it.cellDataLteList + "\n\n" +
//                                        "cellDataNrList "+it.cellDataNrList + "\n\n" +
//                                        "cellDataTdscdmaList "+it.cellDataTdscdmaList + "\n\n" +
//                                        "cellDataWcdmaList "+it.cellDataWcdmaList + "\n\n"
//                                Logx.d(res)
//                                binding.tvCellInfo.text = res
//                            }
//                        }
//                    }
//                })
//
//                systemServiceManagerInfo.onBaseTelephonyCallback.addOnServiceState(object : TelephonyImp.OnServiceState {
//                    override fun onServiceStateChanged(currentServiceState: CurrentServiceState?) {
//                        lifecycleScope.launch {
//                            currentServiceState?.let {
//                                val res =  "ServiceState = cellDataNrList "+it.cellDataNrList + "\n\n" +
//                                        "cellDataLteList "+it.cellDataLteList + "\n\n" +
//                                        "cellDataWcdmaList "+it.cellDataWcdmaList + "\n\n" +
//                                        "cellDataCdmaList "+it.cellDataCdmaList + "\n\n" +
//                                        "cellDataGsmList "+it.cellDataGsmList + "\n\n" +
//                                        "cellDataTdscdmaList "+it.cellDataTdscdmaList + "\n\n"
//                                Logx.d(res)
//                                binding.tvCellInfo.text = res
//                            }
//                        }
//                    }
//                })
//
//                systemServiceManagerInfo.onBaseTelephonyCallback.addOnSignalStrength(object : TelephonyImp.OnSignalStrength {
//                    override fun onSignalStrengthChanged(currentSignalStrength: CurrentSignalStrength?) {
//                        lifecycleScope.launch {
//                            currentSignalStrength?.let {
//                                val res = "Signal = cellDataList "+it.cellDataList + "\n\n" +
//                                        "cellDataCdmaList "+it.cellDataCdmaList + "\n\n" +
//                                        "cellDataGsmList "+it.cellDataGsmList + "\n\n" +
//                                        "cellDataLteList "+it.cellDataLteList + "\n\n" +
//                                        "cellDataNrList "+it.cellDataNrList + "\n\n" +
//                                        "cellDataWcdmaList "+it.cellDataWcdmaList + "\n\n" +
//                                        "cellDataTdscdmaList "+it.cellDataTdscdmaList + "\n\n"
//                                Logx.d(res)
//                                binding.tvSignalStrength.text = res
//                            }
//                        }
//                    }
//                })
//
//                systemServiceManagerInfo.onBaseTelephonyCallback.addOnDataConnectionState(object : TelephonyImp.OnDataConnectionState {
//                    override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
//                        lifecycleScope.launch {
//                            var res = "state = $state, networkType = $networkType"
//
//                            when(state) {
//                                TelephonyManager.DATA_UNKNOWN -> res += ", DATA_UNKNOWN"
//                                TelephonyManager.DATA_SUSPENDED -> res += ", DATA_SUSPENDED"
//                                TelephonyManager.DATA_DISCONNECTED -> res += ", DATA_DISCONNECTED"
//                                TelephonyManager.DATA_CONNECTING -> res += ", DATA_CONNECTING"
//                                TelephonyManager.DATA_CONNECTED -> res += ", DATA_CONNECTED"
//                                TelephonyManager.DATA_DISCONNECTING -> res += ", DATA_DISCONNECTING"
//                                TelephonyManager.DATA_HANDOVER_IN_PROGRESS -> res += ", DATA_HANDOVER_IN_PROGRESS"
//                            }
//
//                            res += "\n Network Type "
//                            when(networkType) {
//                                TelephonyManager.NETWORK_TYPE_UNKNOWN -> res += ", NETWORK_TYPE_UNKNOWN"
//                                TelephonyManager.NETWORK_TYPE_GPRS -> res += ", NETWORK_TYPE_GPRS"
//                                TelephonyManager.NETWORK_TYPE_EDGE -> res += ", NETWORK_TYPE_EDGE"
//                                TelephonyManager.NETWORK_TYPE_UMTS -> res += ", NETWORK_TYPE_UMTS"
//                                TelephonyManager.NETWORK_TYPE_CDMA -> res += ", NETWORK_TYPE_CDMA"
//                                TelephonyManager.NETWORK_TYPE_EVDO_0 -> res += ", NETWORK_TYPE_EVDO_0"
//                                TelephonyManager.NETWORK_TYPE_EVDO_A -> res += ", NETWORK_TYPE_EVDO_A"
//                                TelephonyManager.NETWORK_TYPE_1xRTT -> res += ", NETWORK_TYPE_1xRTT"
//                                TelephonyManager.NETWORK_TYPE_HSDPA -> res += ", NETWORK_TYPE_HSDPA"
//                                TelephonyManager.NETWORK_TYPE_HSUPA -> res += ", NETWORK_TYPE_HSUPA"
//                                TelephonyManager.NETWORK_TYPE_HSPA -> res += ", NETWORK_TYPE_HSPA"
//                                TelephonyManager.NETWORK_TYPE_IDEN -> res += ", NETWORK_TYPE_IDEN"
//                                TelephonyManager.NETWORK_TYPE_EVDO_B -> res += ", NETWORK_TYPE_EVDO_B"
//                                TelephonyManager.NETWORK_TYPE_LTE -> res += ", NETWORK_TYPE_LTE"
//                                TelephonyManager.NETWORK_TYPE_EHRPD -> res += ", NETWORK_TYPE_EHRPD"
//                                TelephonyManager.NETWORK_TYPE_HSPAP -> res += ", NETWORK_TYPE_HSPAP"
//                                TelephonyManager.NETWORK_TYPE_IWLAN -> res += ", NETWORK_TYPE_IWLAN"
//                                TelephonyManager.NETWORK_TYPE_TD_SCDMA -> res += ", NETWORK_TYPE_TD_SCDMA"
//                                TelephonyManager.NETWORK_TYPE_GSM -> res += ", NETWORK_TYPE_GSM"
//                            }
//                            binding.tvDataConnectionState.text = res + "\n\n"
//                        }
//                    }
//                })
//                systemServiceManagerInfo.onBaseTelephonyCallback.addOnActiveDataSubId(object: TelephonyImp.OnActiveDataSubId {
//                    override fun onActiveDataSubIdChanged(subId: Int) {
//
//                    }
//                })
//            }
//        }
//    }
//
//    private fun getSimStatus() :String {
//        var res = "Sim Status - "
//        systemServiceManagerInfo.networkInfo.run {
//            if (isSingleSim()) res += " Is Single Sim\n"
//            else if (isDualSim()) res += " Is Able Dual Sim\n"
//            else res += " Is No Sim\n"
//
//            if (ActivityCompat.checkSelfPermission(
//                    this@NetworkActivity, Manifest.permission.READ_PHONE_STATE
//                ) != PackageManager.PERMISSION_GRANTED) {
//                return res
//            }
//            res += " Active Sim Count ${getActiveSimCount()}\n"
//            res += " Mcc FromDefaultUSim ${getMccFromDefaultUSim()}\n"
//            res += " Mnc FromDefaultUSim ${getMncFromDefaultUSim()}\n"
//            res += " PhoneNumber DefaultUSim ${getPhoneNumberDefaultUSim()}\n"
//
//            return res + "\n\n"
//        }
//    }
//
//    private fun getConnectCheck(): String {
//        var res  = "Connect Check - "
//        if(systemServiceManagerInfo.networkInfo.isConnectVPN()) res += "VPN \n"
//        if(systemServiceManagerInfo.networkInfo.isConnectLowPan()) res += "LOW Pan \n"
//        if(systemServiceManagerInfo.networkInfo.isConnectWifiAware()) res += "WifiAware \n"
//        if(systemServiceManagerInfo.networkInfo.isNetworkConnect()) res += "NetworkConnect \n"
//        if(systemServiceManagerInfo.networkInfo.isConnectEthernet()) res += "Ethernet \n"
//        if(systemServiceManagerInfo.networkInfo.isConnectMobile()) res += "Mobile \n"
//        if(systemServiceManagerInfo.networkInfo.isConnectWifi()) res += "Wifi \n"
//        return res + "\n\n"
//    }
//
//    private fun registerDefaultCallback() {
//        systemServiceManagerInfo.networkInfo.registerDefaultNetworkCallback(object :
//            ConnectivityManager.NetworkCallback() {
//
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                binding.tvDefaultConnectState.text = "Default - onAvailable\n\n"
//            }
//
//            override fun onLosing(network: Network, maxMsToLive: Int) {
//                super.onLosing(network, maxMsToLive)
//                binding.tvDefaultConnectState.text = "Default - onLosing\n\n"
//            }
//
//            override fun onLost(network: Network) {
//                binding.tvDefaultConnectState.text = "Default - onLost\n\n"
//                super.onLost(network)
//            }
//
//            override fun onUnavailable() {
//                super.onUnavailable()
//                binding.tvDefaultConnectState.text = "Default - onUnavailable\n\n"
//            }
//
//            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
//                super.onCapabilitiesChanged(network, networkCapabilities)
//                Logx.d("networkCapabilities \n $networkCapabilities\n\n")
////                binding.tvDefaultCellInfo.text =
//            }
//
//            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
//                super.onLinkPropertiesChanged(network, linkProperties)
//                Logx.d("linkProperties \n $linkProperties")
//            }
//
//            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
//                super.onBlockedStatusChanged(network, blocked)
//            }
//        })
//    }
}
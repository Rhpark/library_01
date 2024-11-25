package kr.open.rhpark.library.system.service.access.internet.network.data

import android.net.NetworkCapabilities
import android.net.NetworkSpecifier
import android.net.TransportInfo
import android.os.Build
import android.os.ext.SdkExtensions
import androidx.annotation.RequiresApi
import kr.open.rhpark.library.debug.logcat.Logx

public data class NetworkCapabilitiesData(public val networkCapabilities: NetworkCapabilities) :
    NetworkBase(networkCapabilities) {

    private val transportInfoStr = "TransportInfo: <SSID:"

    public fun getLinkUpstreamBandwidthKbps(): Int = networkCapabilities.getLinkUpstreamBandwidthKbps()

    public fun getLinkDownstreamBandwidthKbps(): Int = networkCapabilities.getLinkDownstreamBandwidthKbps()

    public fun getCapabilities(): IntArray? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        networkCapabilities.capabilities
    } else {
        getCapabilitiesNumber(splitStr("Capabilities: ", " LinkUpBandwidth", "&"))
    }


    private fun getCapabilitiesNumber(capabilitiesStr:List<String>?):IntArray? {

        if(capabilitiesStr == null) return null

        val res = mutableListOf<Int>()
        capabilitiesStr.forEach {
            val data = when(it) {
                "MMS"->     NetworkCapabilities.NET_CAPABILITY_MMS
                "SUPL"->    NetworkCapabilities.NET_CAPABILITY_SUPL
                "DUN"->     NetworkCapabilities.NET_CAPABILITY_DUN
                "FOTA"->    NetworkCapabilities.NET_CAPABILITY_FOTA
                "IMS"->     NetworkCapabilities.NET_CAPABILITY_IMS
                "CBS"->     NetworkCapabilities.NET_CAPABILITY_CBS
                "WIFI_P2P"->    NetworkCapabilities.NET_CAPABILITY_WIFI_P2P
                "IA"->      NetworkCapabilities.NET_CAPABILITY_IA
                "RCS"->     NetworkCapabilities.NET_CAPABILITY_RCS
                "XCAP"->    NetworkCapabilities.NET_CAPABILITY_XCAP
                "EIMS"->    NetworkCapabilities.NET_CAPABILITY_EIMS
                "NOT_METERED"->    NetworkCapabilities.NET_CAPABILITY_NOT_METERED
                "INTERNET"-> NetworkCapabilities.NET_CAPABILITY_INTERNET
                "NOT_RESTRICTED"->  NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED
                "TRUSTED"-> NetworkCapabilities.NET_CAPABILITY_TRUSTED
                "NOT_VPN"-> NetworkCapabilities.NET_CAPABILITY_NOT_VPN
                "VALIDATED"->   NetworkCapabilities.NET_CAPABILITY_VALIDATED
                "CAPTIVE_PORTAL"->  NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL
                "NOT_ROAMING"-> NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING
                "FOREGROUND"->  NetworkCapabilities.NET_CAPABILITY_FOREGROUND
                "NOT_CONGESTED"-> NetworkCapabilities.NET_CAPABILITY_NOT_CONGESTED
                "NOT_SUSPENDED"-> NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED
                "OEM_PAID"->    22 /*NetworkCapabilities.NET_CAPABILITY_OEM_PAID*/
                "MCX"->     NetworkCapabilities.NET_CAPABILITY_MCX
                "PARTIAL_CONNECTIVITY"->    24 /*NetworkCapabilities.NET_CAPABILITY_PARTIAL_CONNECTIVITY*/
                "TEMPORARILY_NOT_METERED"-> NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED
                "OEM_PRIVATE"-> 26 /*NetworkCapabilities.NET_CAPABILITY_OEM_PRIVATE*/
                "VEHICLE_INTERNAL"->    27 /*NetworkCapabilities.NET_CAPABILITY_VEHICLE_INTERNAL*/
                "NOT_VCN_MANAGED"->     28 /* NetworkCapabilities.NET_CAPABILITY_NOT_VCN_MANAGED */
                "ENTERPRISE"-> NetworkCapabilities.NET_CAPABILITY_ENTERPRISE
                "VSIM"->        30 /* NetworkCapabilities.NET_CAPABILITY_VSIM */
                "BIP"->         31 /* NetworkCapabilities.NET_CAPABILITY_BIP */
                "HEAD_UNIT"->   NetworkCapabilities.NET_CAPABILITY_HEAD_UNIT
                "MMTEL"->       NetworkCapabilities.NET_CAPABILITY_MMTEL
                "PRIORITIZE_LATENCY"->  NetworkCapabilities.NET_CAPABILITY_PRIORITIZE_LATENCY
                "PRIORITIZE_BANDWIDTH"->    NetworkCapabilities.NET_CAPABILITY_PRIORITIZE_BANDWIDTH
                "LOCAL_NETWORK"->   NetworkCapabilities.NET_CAPABILITY_LOCAL_NETWORK
                else -> -1
            }
            res.add(data)
        }
        return res.toIntArray()
    }

    public fun getSubscriptionIds(): List<Int>? = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
        if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 12) {
            networkCapabilities.subscriptionIds.toList()
        } else {
            val data = splitStr("SubscriptionIds: {", "}", ",")
            data?.map { it -> it.toInt() }?.toList()
        }
    } else {
        val data = splitStr("SubscriptionIds: {", "}", ",")
        data?.map { it -> it.toInt() }?.toList()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public fun getNetworkSpecifier(): NetworkSpecifier? = networkCapabilities.networkSpecifier

    public fun getSignalStrength(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        networkCapabilities.signalStrength
    } else {
        splitStr("SignalStrength: "," ","")?.get(0)?.toInt() ?: Int.MIN_VALUE
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public fun getOwnerUid(): Int = networkCapabilities.ownerUid

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public fun getEnterpriseIds(): IntArray = networkCapabilities.enterpriseIds

    @RequiresApi(Build.VERSION_CODES.Q)
    public fun getTransportInfo(): TransportInfo? = networkCapabilities.transportInfo

    public fun getBssidInTransportInfo():String? = getDataInTransportInfoStr(", BSSID: ",", ")

    public fun getMacInTransportInfo():String? = getDataInTransportInfoStr( ", MAC: ",", ")
    public fun getIpTransportInfo():String? = getDataInTransportInfoStr( ", IP: ",", ")
    public fun getSecurityTypeTransportInfo():String? = getDataInTransportInfoStr( ", Security type: ",", ")
    public fun getSupplicantStateTransportInfo():String? = getDataInTransportInfoStr( ", Supplicant state: ",", ")
    public fun getWifiStandardTransportInfo():String? = getDataInTransportInfoStr( ", Wi-Fi standard: ",", ")
    public fun getRssiTransportInfo():String? = getDataInTransportInfoStr( ", RSSI: ",", ")
    public fun getLinkSpeedTransportInfo():String? = getDataInTransportInfoStr( ", Link speed: ",", ")
    public fun getTxLinkSpeedTransportInfo():String? = getDataInTransportInfoStr( ", Tx Link speed: ",", ")
    public fun getMaxSupportedTxLinkSpeedTransportInfo():String? = getDataInTransportInfoStr( ", Max Supported Tx Link speed: ",", ")
    public fun getRxLinkSpeedTransportInfo():String? = getDataInTransportInfoStr( ", Rx Link speed: ",", ")
    public fun getMaxRxSupportedLinkSpeedTransportInfo():String? = getDataInTransportInfoStr( ", Max Supported Rx Link speed: ",", ")
    public fun getFrequencyTransportInfo():String? = getDataInTransportInfoStr( ", Frequency: ",", ")
    public fun getNetIdTransportInfo():String? = getDataInTransportInfoStr( ", Net ID: ",", ")
    public fun isMeteredHintTransportInfo():Boolean = getDataInTransportInfoStr( ", Metered hint: ",", ")?.equals("true") ?:false
    public fun getScoreTransportInfo():String? = getDataInTransportInfoStr( ", score: ",", ")
    public fun getSubscriptionIdTransportInfo():String? = getDataInTransportInfoStr( ", SubscriptionId: ",", ")
    public fun getIsPrimaryTransportInfo():String? = getDataInTransportInfoStr( ", IsPrimary: ",", ")
    public fun isUsableTransportInfo():Boolean = getDataInTransportInfoStr( ", isUsable: ",", ")?.equals("true") ?:false
    public fun isCarrierMergedTransportInfo():Boolean = getDataInTransportInfoStr( ", CarrierMerged: ",", ")?.equals("true") ?:false
    public fun isTrustedTransportInfo():Boolean = getDataInTransportInfoStr( ", Trusted: ",", ")?.equals("true") ?:false
    public fun isRestrictedTransportInfo():Boolean = getDataInTransportInfoStr( ", Restricted: ",", ")?.equals("true") ?:false
    public fun isEphemeralTransportInfo():Boolean = getDataInTransportInfoStr( ", Ephemeral: ",", ")?.equals("true") ?:false
    public fun isOemPaidTransportInfo():Boolean = getDataInTransportInfoStr( ", OEM paid: ",", ")?.equals("true") ?:false
    public fun isOemPrivateTransportInfo():Boolean = getDataInTransportInfoStr( ", OEM private: ",", ")?.equals("true") ?:false
    public fun isOsuApTransportInfo():Boolean = getDataInTransportInfoStr( ", OSU AP: ",", ")?.equals("true") ?:false


    private fun getDataInTransportInfoStr(start: String, end: String): String? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            networkCapabilities.transportInfo?.let {
                val str = networkCapabilities.transportInfo.toString()
                return str.split(start,end)
            }
        } else if(isContains(transportInfoStr)) {
            getResStr().split(transportInfoStr)[1]?.split(start,end)
        } else null
}




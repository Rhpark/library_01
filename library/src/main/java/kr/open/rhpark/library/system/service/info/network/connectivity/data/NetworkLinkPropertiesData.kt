package kr.open.rhpark.library.system.service.info.network.connectivity.data

import android.net.IpPrefix
import android.net.LinkAddress
import android.net.LinkProperties
import android.net.ProxyInfo
import android.net.RouteInfo
import android.os.Build
import androidx.annotation.RequiresApi
import java.net.InetAddress

public data class NetworkLinkPropertiesData(public val linkProperties: LinkProperties) :
    NetworkBase(linkProperties) {

    public fun getLinkAddresses(): MutableList<LinkAddress> = linkProperties.linkAddresses

    public fun getMtu(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        linkProperties.mtu
    } else {
        splitStr("MTU: ", " ")?.let { it[0].toInt() } ?: 0
    }

    public fun getRoutes(): MutableList<RouteInfo> = linkProperties.routes

    public fun getDomains(): String? = linkProperties.domains

    public fun getDnsServer(): MutableList<InetAddress> = linkProperties.dnsServers

    public fun getDhcpServerAddress(): InetAddress? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        linkProperties.dhcpServerAddress
    } else {
        InetAddress.getByName(splitStr("ServerAddress: "," ")?.get(0)?.toString())
    }

    public fun getHttpProxy(): ProxyInfo? = linkProperties.httpProxy

    public fun getInterfaceName(): String? = linkProperties.interfaceName

    public fun isPrivateDnsActive(): Boolean = linkProperties.isPrivateDnsActive

    public fun getPrivateDnsServerName() :String? = linkProperties.privateDnsServerName

    @RequiresApi(Build.VERSION_CODES.R)
    public fun getNat64Prefix(): IpPrefix? = linkProperties.nat64Prefix

    public fun getTcpBufferSizes():List<String>? = if(getResStr().contains(" TcpBufferSizes: ")) {
        getResStr().split(" TcpBufferSizes: "," ")?.split(",")?:null
    } else {
        null
    }

    public fun toResString(): String {
        var res: String = ""
        res += "getLinkAddresses ${getLinkAddresses().toList()}\n" +
                "getMtu ${getMtu()}\n" +
                "getRoutes ${getRoutes()}\n" +
                "getDomains ${getDomains()}\n" +
                "getDnsServer ${getDnsServer()}\n" +
                "getDhcpServerAddress ${getDhcpServerAddress()}\n" +
                "getHttpProxy ${getHttpProxy()}\n" +
                "getInterfaceName ${getInterfaceName()}\n" +
                "isPrivateDnsActive ${isPrivateDnsActive()}\n" +
                "getPrivateDnsServerName ${getPrivateDnsServerName()}\n" +
                "getTcpBufferSizes ${getTcpBufferSizes()}\n"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            res += "getNat64Prefix ${getNat64Prefix()}\n"
        }
        res += "\n\n"
        return res
    }
}
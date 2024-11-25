package kr.open.rhpark.library.system.service.access.internet.network

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import kr.open.rhpark.library.system.service.access.internet.base.BaseSubscriptionState
import kr.open.rhpark.library.system.service.access.internet.network.callback.NetworkStateCallback
import kr.open.rhpark.library.system.service.access.internet.network.data.NetworkCapabilitiesData
import kr.open.rhpark.library.system.service.access.internet.network.data.NetworkLinkPropertiesData


/**
 * 네트워크 상태 정보를 제공하는 클래스.
 * Provides network state information.
 */
public class NetworkStateInfo(
    context: Context, telephonyManager: TelephonyManager,
    subscriptionManager: SubscriptionManager,
    public val connectivityManager: ConnectivityManager,
    public val wifiManager: WifiManager
) : BaseSubscriptionState(
    context,
    listOf(READ_PHONE_STATE, READ_PHONE_NUMBERS, ACCESS_FINE_LOCATION),
    subscriptionManager,
    telephonyManager
) {
    /** 네트워크 상태 콜백
     *  Network state callback */
    private var networkCallBack: NetworkStateCallback? = null

    /** 기본 네트워크 상태 콜백
     *  Default network state callback */
    private var networkDefaultCallback: NetworkStateCallback? = null

    /**
     * 네트워크 연결 여부를 확인합니다.
     * Checks if the network is connected.
     *
     * @return Boolean - 네트워크가 연결되어 있으면 true.
     * @Returns true if the network is connected.
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isNetworkConnected() :Boolean {

        val caps = getCapabilities()
        val linkProperties = getLinkProperties()

        return (caps != null) && (linkProperties != null)
    }

    /**
     * 현재 네트워크의 NetworkCapabilities를 반환.
     * Returns the NetworkCapabilities of the current network.
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun getCapabilities() : NetworkCapabilities? = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun getLinkProperties() : LinkProperties? = connectivityManager.getLinkProperties(connectivityManager.activeNetwork)

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isConnectedWifi(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isConnectedMobile(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isConnectedVPN(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isConnectedBluetooth(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ?: false

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isConnectedWifiAware(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) ?: false

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isConnectedEthernet(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun isConnectedLowPan(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) ?: false

    @RequiresPermission(ACCESS_NETWORK_STATE)
    @RequiresApi(Build.VERSION_CODES.S)
    public fun isConnectedUSB(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_USB) ?: false

    public fun isWifiOn(): Boolean = wifiManager.isWifiEnabled

    /**
     * 핸들러를 사용해 네트워크 상태 변경을 모니터링하기 위해 콜백을 등록합니다.
     * Registers a callback to monitor network state changes with an optional handler.
     */

    /**
     * 네트워크 상태 콜백을 등록.
     * Registers a network state callback.
     *
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun registerNetworkCallback(
        handler: Handler? = null,
        onNetworkAvailable: ((Network) -> Unit)? = null,
        onNetworkLosing: ((Network, Int) -> Unit)? = null,
        onNetworkLost: ((Network) -> Unit)? = null,
        onUnavailable: (() -> Unit)? = null,
        onNetworkCapabilitiesChanged: ((Network, NetworkCapabilitiesData) -> Unit)? = null,
        onLinkPropertiesChanged: ((Network, NetworkLinkPropertiesData) -> Unit)? = null,
        onBlockedStatusChanged: ((Network, Boolean) -> Unit)? = null,
    ) {
        unregisterNetworkCallback()
        networkCallBack = NetworkStateCallback(
            onNetworkAvailable,
            onNetworkLosing,
            onNetworkLost,
            onUnavailable,
            onNetworkCapabilitiesChanged,
            onLinkPropertiesChanged,
            onBlockedStatusChanged
        )

        val networkRequest = NetworkRequest.Builder().build()
        networkCallBack?.let { callback->
            handler?.let {
                connectivityManager.registerNetworkCallback(networkRequest, callback, it)
            } ?: connectivityManager.registerNetworkCallback(networkRequest, callback)
        }
    }

    /**
     * 기본 네트워크 상태 콜백을 등록.
     * Registers a default network state callback.
     *
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun registerDefaultNetworkCallback(
        handler: Handler? = null,
        onNetworkAvailable: ((Network) -> Unit)? = null,
        onNetworkLosing: ((Network, Int) -> Unit)? = null,
        onNetworkLost: ((Network) -> Unit)? = null,
        onUnavailable: (() -> Unit)? = null,
        onNetworkCapabilitiesChanged: ((Network, NetworkCapabilitiesData) -> Unit)? = null,
        onLinkPropertiesChanged: ((Network, NetworkLinkPropertiesData) -> Unit)? = null,
        onBlockedStatusChanged: ((Network, Boolean) -> Unit)? = null,
    ) {
        unregisterDefaultNetworkCallback()

        networkDefaultCallback = NetworkStateCallback(
            onNetworkAvailable,
            onNetworkLosing,
            onNetworkLost,
            onUnavailable,
            onNetworkCapabilitiesChanged,
            onLinkPropertiesChanged,
            onBlockedStatusChanged
        )

        networkDefaultCallback?.let { callback->
            handler?.let {
                connectivityManager.registerDefaultNetworkCallback(callback, it)
            }?: connectivityManager.registerDefaultNetworkCallback(callback)
        }
    }

    public fun unregisterNetworkCallback() {
        networkCallBack?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
        networkCallBack = null
    }

    public fun unregisterDefaultNetworkCallback() {
        networkDefaultCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
        networkDefaultCallback = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkCallback()
        unregisterDefaultNetworkCallback()
    }
}
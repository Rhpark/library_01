package kr.open.rhpark.library.system.service.access.network

/**
 * request Permission
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
//public class NetworkStateInfo(
//    context: Context,
//    telephonyManager: TelephonyManager,
//    subscriptionManager: SubscriptionManager,
//    onCommonTelephonyCallback: CommonTelephonyCallback,
//    private val connectivityManager: ConnectivityManager,
//    private val wifiManager: WifiManager,
//) : BaseSystemServiceTelephonySubscription(
//    context,
//    telephonyManager,
//    subscriptionManager,
//    onCommonTelephonyCallback,
//    listOf(ACCESS_NETWORK_STATE)
//) {
//
//    private var networkCallback: ConnectivityManager.NetworkCallback? = null
//    private var defaultNetworkCallback: ConnectivityManager.NetworkCallback? = null
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isNetworkConnect() :Boolean {
//
//        val caps = getCapabilities()
//        val linkProperties = getLinkProperties()
//
//        if(caps == null) {
//            Logx.d("caps is null")
//        } else {
//            Logx.d("caps ${caps.toString()}")
//        }
//
//        if(linkProperties == null) {
//            Logx.d("linkProperties is null")
//        } else {
//            Logx.d("linkProperties ${linkProperties.toString()}")
//        }
//
//        return (caps != null) && (linkProperties != null)
//    }
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun getCapabilities() : NetworkCapabilities? = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun getLinkProperties() : LinkProperties? = connectivityManager.getLinkProperties(connectivityManager.activeNetwork)
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isConnectWifi(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isConnectMobile(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isConnectVPN(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isConnectBluetooth(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ?: false
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isConnectWifiAware(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) ?: false
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isConnectEthernet(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun isConnectLowPan(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) ?: false
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    @RequiresApi(Build.VERSION_CODES.S)
//    public fun isConnectUSB(): Boolean = getCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_USB) ?: false
//
//    public fun isWifiOn(): Boolean = wifiManager.isWifiEnabled
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun registerNetworkCallback(networkCallback: ConnectivityManager.NetworkCallback) {
//        if(networkCallback != null) { unregisterNetworkCallback() }
//        this.networkCallback = networkCallback
//        val networkRequest = NetworkRequest.Builder().build()
//        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
//    }
//
//    @RequiresPermission(ACCESS_NETWORK_STATE)
//    public fun registerDefaultNetworkCallback(
//        networkCallback: NetworkCallback,
//        handler: Handler? = null
//    ) {
//        unregisterDefaultNetworkCallback()
//        this.defaultNetworkCallback = networkCallback
//        handler?.let {
//            connectivityManager.registerDefaultNetworkCallback(networkCallback, it)
//        }?: connectivityManager.registerDefaultNetworkCallback(networkCallback)
//    }
//
//    public fun unregisterNetworkCallback() {
//        networkCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
//        networkCallback = null
//    }
//
//    public fun unregisterDefaultNetworkCallback() {
//        defaultNetworkCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
//        defaultNetworkCallback = null
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterNetworkCallback()
//        unregisterDefaultNetworkCallback()
//    }
//}
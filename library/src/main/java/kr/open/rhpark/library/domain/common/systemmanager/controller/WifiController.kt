package kr.open.rhpark.library.domain.common.systemmanager.controller

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.Manifest.permission.CHANGE_WIFI_STATE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.NetworkSpecifier
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.open.rhpark.library.domain.common.systemmanager.base.BaseSystemService
import kr.open.rhpark.library.util.extensions.conditional.sdk_version.checkSdkVersion
import kr.open.rhpark.library.util.extensions.context.getConnectivityManager
import kr.open.rhpark.library.util.extensions.context.getWifiManager
import java.net.Inet4Address
import java.util.Locale

public class WifiController(context: Context) :
    BaseSystemService(
        context,
        listOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
            CHANGE_WIFI_STATE,
            ACCESS_WIFI_STATE,
            ACCESS_NETWORK_STATE
        )
    ) {

    private val wifiManager: WifiManager by lazy { context.getWifiManager() }

    private val connectivityManager: ConnectivityManager by lazy { context.getConnectivityManager() }

    // WiFi 스캔 결과를 실시간으로 제공하는 StateFlow
    private val _scanResults = MutableStateFlow<List<ScanResult>>(emptyList())
    public val scanResults: StateFlow<List<ScanResult>> = _scanResults

    // 현재 연결 상태
    private val _isConnected = MutableStateFlow(false)
    public val isConnected: StateFlow<Boolean> = _isConnected

    // 수신자 등록 여부
    private var receiverRegistered = false

    // 브로드캐스트 리시버 정의
    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        _scanResults.value = wifiManager.scanResults
                    }
                }
                WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                    updateConnectionState()
                }
            }
        }
    }

    init {
        // 초기 연결 상태 확인
        updateConnectionState()
    }

    /**
     * WiFi 상태 확인
     * @return WiFi 활성화 상태 반환
     */
    public fun isWifiEnabled(): Boolean {
        return wifiManager.isWifiEnabled
    }

    /**
     * WiFi 설정 화면으로 이동
     * Android 10 이상에서는 직접 WiFi를 켜고 끌 수 없으므로 설정 화면으로 이동
     */
    public fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * WiFi 켜기 (Android 9 이하에서만 동작)
     * @return 성공 여부
     */
    @Suppress("DEPRECATION")
    @RequiresPermission(CHANGE_WIFI_STATE)
    public fun enableWifi(): Boolean = checkSdkVersion(Build.VERSION_CODES.Q,
        positiveWork = {
            openWifiSettings()
            false
        }, negativeWork = {
            wifiManager.setWifiEnabled(true)
        }
    )

    /**
     * WiFi 끄기 (Android 9 이하에서만 동작)
     * @return 성공 여부
     */
    @Suppress("DEPRECATION")
    @RequiresPermission(CHANGE_WIFI_STATE)
    public fun disableWifi(): Boolean = checkSdkVersion(Build.VERSION_CODES.Q,
        positiveWork = {
            openWifiSettings()
            false
        }, negativeWork = {
            wifiManager.setWifiEnabled(false)
        }
    )

    /**
     * WiFi 스캔 시작
     * ACCESS_FINE_LOCATION 또는 ACCESS_COARSE_LOCATION 권한 필요
     * @return 스캔 시작 성공 여부
     */
    @RequiresPermission(anyOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_WIFI_STATE])
    public fun startScan(): Boolean {
        registerReceivers()
        return wifiManager.startScan()
    }

    /**
     * 브로드캐스트 수신자 등록
     */
    private fun registerReceivers() {
        if (!receiverRegistered) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            context.registerReceiver(wifiScanReceiver, intentFilter)
            receiverRegistered = true
        }
    }

    /**
     * 브로드캐스트 수신자 해제
     */
    public fun unregisterReceivers() {
        if (receiverRegistered) {
            try {
                context.unregisterReceiver(wifiScanReceiver)
                receiverRegistered = false
            } catch (e: IllegalArgumentException) {
                // 이미 해제된 경우
            }
        }
    }

    /**
     * 현재 연결된 WiFi 정보 가져오기
     * @return 현재 연결된 WiFi의 SSID, 없으면 null
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun getCurrentSsid(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상에서는 ConnectivityManager를 사용
            val network = connectivityManager.activeNetwork ?: return null
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return null

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val wifiInfo = capabilities.transportInfo as? WifiInfo
                var ssid = wifiInfo?.ssid

                // SSID는 큰따옴표로 감싸져 있을 수 있음
                if (ssid?.startsWith("\"") == true && ssid.endsWith("\"")) {
                    ssid = ssid.substring(1, ssid.length - 1)
                }
                if (ssid == "<unknown ssid>" || ssid.isNullOrEmpty()) null else ssid
            } else {
                null
            }
        } else {
            // Android 9 이하에서는 WifiManager.connectionInfo 사용
            val info = wifiManager.connectionInfo
            var ssid = info.ssid
            // SSID는 큰따옴표로 감싸져 있을 수 있음
            if (ssid?.startsWith("\"") == true && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length - 1)
            }
            if (ssid == "<unknown ssid>" || ssid.isNullOrEmpty()) null else ssid
        }
    }

    /**
     * 현재 연결된 WiFi의 RSSI(신호 강도) 가져오기
     * @return RSSI 값 (dBm), 연결되지 않았으면 -127 반환
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun getCurrentRssi(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상에서는 ConnectivityManager를 사용
            val network = connectivityManager.activeNetwork ?: return -127
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return -127

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val wifiInfo = capabilities.transportInfo as? WifiInfo
                wifiInfo?.rssi ?: -127
            } else {
                -127
            }
        } else {
            // Android 9 이하에서는 WifiManager.connectionInfo 사용
            val info = wifiManager.connectionInfo
            info?.rssi ?: -127
        }
    }

    /**
     * 현재 연결된 WiFi의 BSSID 가져오기
     * @return BSSID (MAC 주소), 연결되지 않았으면 null
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun getCurrentBssid(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상에서는 ConnectivityManager를 사용
            val network = connectivityManager.activeNetwork ?: return null
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return null

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val wifiInfo = capabilities.transportInfo as? WifiInfo
                val bssid = wifiInfo?.bssid
                if (bssid.isNullOrEmpty() || bssid == "02:00:00:00:00:00") null else bssid
            } else {
                null
            }
        } else {
            // Android 9 이하에서는 WifiManager.connectionInfo 사용
            val info = wifiManager.connectionInfo
            val bssid = info?.bssid
            if (bssid.isNullOrEmpty() || bssid == "02:00:00:00:00:00") null else bssid
        }
    }

    /**
     * 현재 연결된 WiFi의 IP 주소 가져오기
     * @return IP 주소 문자열, 연결되지 않았으면 null
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public fun getCurrentIpAddress(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상에서는 ConnectivityManager와 LinkProperties를 사용
            val network = connectivityManager.activeNetwork ?: return null
            if (!isWifiConnected(network)) return null

            val linkProperties = connectivityManager.getLinkProperties(network) ?: return null

            // IPv4 주소 찾기
            for (address in linkProperties.linkAddresses) {
                val inetAddress = address.address
                if (inetAddress is Inet4Address && !inetAddress.isLoopbackAddress) {
                    return inetAddress.hostAddress
                }
            }
            null
        } else {
            // Android 9 이하에서는 WifiManager.connectionInfo 사용
            val info = wifiManager.connectionInfo
            val ipAddress = info?.ipAddress ?: 0
            if (ipAddress == 0) return null

            // IP 주소를 비트 연산으로 변환 (리틀 엔디안에서 빅 엔디안으로)
            String.format(
                Locale.getDefault(),
                "%d.%d.%d.%d",
                (ipAddress and 0xff),
                (ipAddress shr 8 and 0xff),
                (ipAddress shr 16 and 0xff),
                (ipAddress shr 24 and 0xff)
            )
        }
    }

    /**
     * 지정된 네트워크가 WiFi 연결인지 확인
     * @param network 확인할 네트워크
     * @return WiFi 연결 여부
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private fun isWifiConnected(network: Network?): Boolean {
        if (network == null) return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    /**
     * WiFi 연결 상태 업데이트
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private fun updateConnectionState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상에서는 ConnectivityManager 사용
            val network = connectivityManager.activeNetwork
            _isConnected.value = isWifiConnected(network)
        } else {
            // Android 9 이하에서는 WifiManager.connectionInfo 사용
            val info = wifiManager.connectionInfo
            _isConnected.value = info != null && info.networkId != -1
        }
    }

    /**
     * Android 9 이하에서 WiFi 네트워크에 연결
     * @param ssid 연결할 WiFi의 SSID
     * @param password WiFi 비밀번호 (WPA/WPA2인 경우)
     * @param isOpenNetwork 공개 네트워크 여부
     * @return 연결 시도 성공 여부
     */
    @Suppress("DEPRECATION")
    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, CHANGE_WIFI_STATE])
    public fun connectToNetwork(ssid: String, password: String? = null, isOpenNetwork: Boolean = false): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return false // Android 10 이상은 다른 메서드 사용
        }

        val conf = WifiConfiguration()
        conf.SSID = "\"$ssid\""

        if (isOpenNetwork) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
        } else if (!password.isNullOrEmpty()) {
            conf.preSharedKey = "\"$password\""
        } else {
            return false
        }

        val networkId = wifiManager.addNetwork(conf)
        if (networkId == -1) {
            return false
        }

        wifiManager.disconnect()
        val success = wifiManager.enableNetwork(networkId, true)
        wifiManager.reconnect()

        return success
    }

    /**
     * Android 10 이상에서 WiFi 네트워크에 연결 요청
     * @param ssid 연결할 WiFi의 SSID
     * @param password WiFi 비밀번호
     * @param callback 연결 상태 콜백
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, CHANGE_WIFI_STATE])
    public fun connectToNetworkModern(ssid: String, password: String? = null,
                               callback: ConnectivityManager.NetworkCallback) {
        val specifier: NetworkSpecifier = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)
            .apply {
                if (!password.isNullOrEmpty()) {
                    setWpa2Passphrase(password)
                }
            }
            .build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(specifier)
            .build()

        connectivityManager.requestNetwork(request, callback)
    }

    /**
     * 리소스 정리
     */
    public override fun onDestroy() {
        super.onDestroy()
        unregisterReceivers()
    }
}
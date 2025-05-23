package kr.open.rhpark.app.activity.wifi

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityWifiBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.domain.common.systemmanager.controller.WifiController
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.conditional.sdk_version.checkSdkVersion
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort

class WifiActivity : BaseBindingActivity<ActivityWifiBinding>(R.layout.activity_wifi) {
    private val wifiController: WifiController by lazy { WifiController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionWifiController()

        initListener()
    }

    private fun requestPermissionWifiController() {
        val permissionList = checkSdkVersion(Build.VERSION_CODES.S,
            positiveWork = {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
                )
            },
            negativeWork = {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
                )
            }
        )
        requestPermissions(permissionList) { deniedPermissions ->
            if (deniedPermissions.isEmpty()) {
                Logx.d("Permissions denied: ${deniedPermissions}")
            } else {
                // Handle the case where permissions are denied
                Logx.w("Permissions denied: ${deniedPermissions.joinToString(", ")}")
                toastShowShort("Permissions denied: ${deniedPermissions.joinToString(", ")}")
            }
        }
    }

    private fun initListener() {
        binding.btnWifiEnable.setOnClickListener {
            wifiController.enableWifi()
        }
        binding.btnWifiDisable.setOnClickListener {
            wifiController.disableWifi()
        }
        binding.btnWifiOpen.setOnClickListener {
            wifiController.openWifiSettings()
        }
        binding.btnWifiScan.setOnClickListener {
            lifecycleScope.launch {
                wifiController.startScan()
                wifiController.scanResults.collect { scanResults ->
                    binding.tvWifiScanRes.text = scanResults.toString()
                }
            }
        }

        binding.btnWifiSsid.setOnClickListener {
            binding.tvWifiSsid.text = "SSID : "+wifiController.getCurrentSsid() +
                    "\n Rssi : "+wifiController.getCurrentRssi() +
                    "\n Ssid : "+wifiController.getCurrentSsid()
                    "\n IpaDdress : "+wifiController.getCurrentIpAddress()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wifiController.onDestroy()
    }
}
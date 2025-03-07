package kr.open.rhpark.app.activity.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityLocationBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.info.location.LocationStateEvent
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getLocationStateInfo
import kr.open.rhpark.library.util.extensions.sdk_version.checkSdkVersion


class LocationActivity : BaseBindingActivity<ActivityLocationBinding>(R.layout.activity_location) {

    private val locationStateInfo by lazy { applicationContext.getLocationStateInfo(lifecycleScope) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(1024, listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)) { requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode , deniedPermissions $deniedPermissions")
            initListener()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initListener() {

        binding.btnGetLocationData.setOnClickListener {
            checkSdkVersion(Build.VERSION_CODES.S){
                binding.tvLocationFusedTurnOnOff.text = "OnFusedEnabled ${locationStateInfo.isFusedEnabled()}"
            }
            binding.tvLocationChange.text = "OnLocationChanged ${locationStateInfo.getLocation()}"
            Logx.d("${locationStateInfo.locationManager.allProviders}")
        }
        binding.btnLocationTurnOnOff.setOnClickListener {
            binding.tvLocationTurnOnOff.text = "Location Turn On ? ${locationStateInfo.isLocationEnabled()}"
            locationStateInfo.registerLocationOnOffState()
        }
        binding.btnLocationChange.setOnClickListener {
            locationStateInfo.registerLocationUpdateStart(
                LocationManager.GPS_PROVIDER, 1000L, 0.1f)
        }

        lifecycleScope.launch {
            locationStateInfo.sfUpdate.collect { type ->
                Logx.d("Type $type")
                when (type) {
                    is LocationStateEvent.OnGpsEnabled -> binding.tvLocationTurnOnOff.text = "OnGpsEnabled ${type.isEnabled}"

                    is LocationStateEvent.OnNetworkEnabled -> binding.tvLocationNetworkTurnOnOff.text = "OnNetworkEnabled ${type.isEnabled}"

                    is LocationStateEvent.OnFusedEnabled -> binding.tvLocationFusedTurnOnOff.text = "OnFusedEnabled ${type.isEnabled}"

                    is LocationStateEvent.OnPassiveEnabled -> binding.tvLocationPassiveTurnOnOff.text = "OnPassiveEnabled ${type.isEnabled}"

                    is LocationStateEvent.OnLocationChanged -> binding.tvLocationChange.text = "OnLocationChanged ${type.location}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationStateInfo.onDestroy()
    }


}
package kr.open.rhpark.app.activity.battery

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityBatteryBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class BatteryActivity : BaseBindingActivity<ActivityBatteryBinding>(R.layout.activity_battery) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(listOf(android.Manifest.permission.BATTERY_STATS)) { grantedPermissions, deniedPermissions ->
            Logx.d("grantedPermissions $grantedPermissions, \n deniedPermissions $deniedPermissions")
        }
        initListener()
    }

    private fun initListener() {
        binding.btnBatteryStatusStart.setOnClickListener {
            systemServiceManagerInfo.batteryInfo.registerBatteryReceiver()
            systemServiceManagerInfo.batteryInfo.startUpdateScope(lifecycleScope)
            updateInfo()
        }
    }

    private fun updateInfo() {
        systemServiceManagerInfo.batteryInfo.run {
            updateCapacityListener { it-> binding.tvCapacity.text = "Capacity = ${it}" }
            updateCurrentAmpereListener { it-> binding.tvCurrentAmpere.text = "Current Ampere = ${it} mA" }
            updateCurrentAverageAmpereListener { it-> binding.tvCurrentAverageAmpere.text = "Current AverageAmpere = ${it} mA" }
            updateChargePlugStrListener { it-> binding.tvChargeChargePlugStr.text = "ChargePlugStr = ${it}" }
            updateChargeStatusListener { it-> binding.tvChargeStatus.text = "Health = ${it}" }
            updateEnergyCounterListener {  it-> binding.tvEnergyCounte.text = "EnergyCounte = ${it}" }
            updateHealthStrListener {  it-> binding.tvHealth.text = "Health = ${it}" }
            updatePresentListener { it-> binding.tvPresent.text = "Present = ${it}" }
            updateTemperatureListener { it-> binding.tvTemperature.text = "Temperature = ${it} C" }
            updateTotalCapacityListener { it-> binding.tvTotalCapacity.text = "TotalCapacity = ${it} " }
            updateVoltageListener { it-> binding.tvVoltage.text = "Charge voltage = ${it} v" }
            updateChargeCountListener { it-> binding.tvChargeCounter.text = "ChargeCounter = ${it}" }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        systemServiceManagerInfo.batteryInfo.onDestroy()
    }
}
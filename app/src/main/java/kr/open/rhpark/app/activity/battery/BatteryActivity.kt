package kr.open.rhpark.app.activity.battery

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityBatteryBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.domain.common.systemmanager.info.battery.BatteryStateEvent
import kr.open.rhpark.library.domain.common.systemmanager.info.battery.BatteryStateInfo
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getBatteryStateInfo

class BatteryActivity : BaseBindingActivity<ActivityBatteryBinding>(R.layout.activity_battery) {

    private val batteryStateInfo: BatteryStateInfo by lazy { applicationContext.getBatteryStateInfo() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(listOf(android.Manifest.permission.BATTERY_STATS)) { deniedPermissions ->
            Logx.d("deniedPermissions $deniedPermissions ")
            initListener()
            batteryStateInfo.registerBatteryReceiver()
        }
    }

    private fun initListener() {
        binding.btnBatteryStatusStart.setOnClickListener {
            batteryStateInfo.updateStart(lifecycleScope)
            collectBatteryInfo()
        }

        binding.btnBatteryStatusStop.setOnClickListener {
            batteryStateInfo.updateStop()
        }

        binding.btnBatteryStatusRes.setOnClickListener {
            binding.tvTest.text = "Capacity = ${batteryStateInfo.getCapacity()}" +
            "\nTotalCapacity = ${batteryStateInfo.getTotalCapacity()}" +
            "\nPresent = ${batteryStateInfo.getPresent()}"+
            "\nVoltage = ${batteryStateInfo.getVoltage()}"+
            "\nTechnology = ${batteryStateInfo.getTechnology()}"+
            "\nChargePlugStr = ${batteryStateInfo.getChargePlugStr()}"+
            "\nCurrentAmpere = ${batteryStateInfo.getCurrentAmpere()}"+
            "\nTemperature = ${batteryStateInfo.getTemperature()}"+
            "\nHealth = ${batteryStateInfo.getHealth()}"
        }
    }

    private fun collectBatteryInfo() = lifecycleScope.launch {
        batteryStateInfo.sfUpdate.collect { type ->
            when (type) {
                is BatteryStateEvent.OnCapacity -> binding.tvCapacity.text = "Capacity = ${type.percent}"
                is BatteryStateEvent.OnChargeCounter -> binding.tvChargeCounter.text = "ChargeCounter = ${type.counter}"
                is BatteryStateEvent.OnChargePlug -> binding.tvChargeChargePlugStr.text = "ChargePlugStr = ${type.type}"
                is BatteryStateEvent.OnChargeStatus -> binding.tvChargeStatus.text = "OnChargeStatus = ${type.status}"
                is BatteryStateEvent.OnCurrentAmpere -> binding.tvCurrentAmpere.text = "Current Ampere = ${type.current} mA"
                is BatteryStateEvent.OnCurrentAverageAmpere -> binding.tvCurrentAverageAmpere.text = "Current AverageAmpere = ${type.current} mA"
                is BatteryStateEvent.OnEnergyCounter -> binding.tvEnergyCounte.text = "EnergyCounte = ${type.energy}"
                is BatteryStateEvent.OnHealth -> binding.tvHealth.text = "Health = ${type.health}"
                is BatteryStateEvent.OnPresent -> binding.tvPresent.text = "Present = ${type.present}"
                is BatteryStateEvent.OnTemperature -> binding.tvTemperature.text = "Temperature = ${type.temperature}"
                is BatteryStateEvent.OnTotalCapacity -> binding.tvTotalCapacity.text = "TotalCapacity = ${type.totalCapacity} "
                is BatteryStateEvent.OnVoltage -> binding.tvVoltage.text = "Charge voltage = ${type.voltage} v"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        batteryStateInfo.onDestroy()
    }
}
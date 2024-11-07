package kr.open.rhpark.app.activity.battery

import android.os.Bundle
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

        binding.run {
            btnBatteryStatusStart.setOnClickListener {
                systemServiceManagerInfo.batteryInfo.run {
                    setReceiverListener { intent-> updateBatteryInfo(intent.action) }
                }
            }

            btnBatteryStatusStop.setOnClickListener { systemServiceManagerInfo.batteryInfo.setReceiverListener(null) }
        }
    }

    private fun updateBatteryInfo(action:String?=null) {
        Logx.d()
        systemServiceManagerInfo.batteryInfo.run {
            val result =
                "Capacity = ${getCapacity()} %\n" + "ChargePlugStr = ${getChargePlugStr()}\n" + "Charge voltage = ${getVoltage()} v\n" +
                        "Current Ampere = ${getCurrentAmpere()} mA\n" + "Current AverageAmpere = ${getCurrentAverageAmpere()} mA\n" + "Health = ${getHealthStr()}\n" +
                        "IsCharge = ${isCharging()}\n" + "IsFull = ${isFull()}\n" + "Present = ${getPresent()} \n" + "Total capacity = ${getTotalCapacity()} mAh\n" +
                        "Temperature = ${getTemperature()} C \n" + "Technology = ${getTechnology()}\n" + "test ${test()}\n" + "action $action"
            binding.tvBatteryStatus.text = result
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        systemServiceManagerInfo.batteryInfo.onDestroy()
    }
}
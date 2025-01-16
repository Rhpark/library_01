package kr.open.rhpark.library.system.service.info.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kr.open.rhpark.library.system.service.info.battery.power.PowerProfile
import kr.open.rhpark.library.system.service.info.battery.power.PowerProfileVO
import kr.open.rhpark.library.system.service.base.BaseSystemService
import kr.open.rhpark.library.system.service.base.DataUpdate

/**
 * Thisclass provides information about the battery state of an Android device.
 * BatteryStateInfo 클래스는 Android 기기의 배터리 상태 정보를 제공.
 *
 * It is recommended to call destroy() upon complete shutdown.
 * 완전 종료 시 destroy()를 호출하는 것을 권장.
 *
 * to use update..Listener() method , you must update periodically to obtain a more accurate value.
 * update..Listener()를 사용하기 위해선 반드시 주기적으로 업데이트를 해야 조금 더 정확 한 값을 가져 올 수 있다.
 *
 * ex)
 * 1. startUpdate(coroutineScope: CoroutineScope) 사용
 * 2. startUpdate()
 *  . (Call startUpdate() periodically from outside)
 *  . 외부에서 주기적으로 startUpdate() 호출
 *
 *
 * @param context The application context
 * @param context 애플리케이션 컨텍스트.
 *
 * @param batteryManager The BatteryManager instance.
 * @param batteryManager BatteryManager 인스턴스.
 */
public class BatteryStateInfo(
    context: Context,
    public val batteryManager: BatteryManager,
    private val coroutineScope: CoroutineScope,
) : BaseSystemService(context, listOf(android.Manifest.permission.BATTERY_STATS)) {

    private val UPDATE_BATTERY = "RHPARK_BATTERY_STATE_UPDATE"

    public val ERROR_VALUE: Int = Integer.MIN_VALUE

    private val powerProfile: PowerProfile by lazy { PowerProfile(context) }

    private val msfUpdate: MutableStateFlow<BatteryStateEvent> = MutableStateFlow(BatteryStateEvent.OnCapacity(getCapacity()))
    public val sfUpdate: StateFlow<BatteryStateEvent> = msfUpdate.asStateFlow()

    private val capacity = DataUpdate(getCapacity()) { sendFlow(BatteryStateEvent.OnCapacity(it)) }

    private val currentAmpere = DataUpdate(getCurrentAmpere()) { sendFlow(BatteryStateEvent.OnCurrentAmpere(it)) }

    private val currentAverageAmpere = DataUpdate(getCurrentAverageAmpere()) { sendFlow(BatteryStateEvent.OnCurrentAverageAmpere(it)) }

    private val chargeStatus = DataUpdate(getChargeStatus()) { sendFlow(BatteryStateEvent.OnChargeStatus(it)) }

    private val chargeCounter = DataUpdate(getChargeCounter()) { sendFlow(BatteryStateEvent.OnChargeCounter(it)) }

    private val chargePlug = DataUpdate(getChargePlug()) { sendFlow(BatteryStateEvent.OnChargePlug(it)) }

    private val energyCounter = DataUpdate(getEnergyCounter()) { sendFlow(BatteryStateEvent.OnEnergyCounter(it)) }

    private val health = DataUpdate(getHealth()) { sendFlow(BatteryStateEvent.OnHealth(it)) }

    private val present = DataUpdate(getPresent()) { sendFlow(BatteryStateEvent.OnPresent(it)) }

    private val totalCapacity = DataUpdate(getTotalCapacity()) { sendFlow(BatteryStateEvent.OnTotalCapacity(it)) }

    private val temperature = DataUpdate(getTemperature()) { sendFlow(BatteryStateEvent.OnTemperature(it)) }

    private val voltage = DataUpdate(getVoltage()) { sendFlow(BatteryStateEvent.OnVoltage(it)) }

    private val batteryBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            batteryStatus = intent
            updateBatteryInfo()
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_BATTERY_CHANGED)
        addAction(Intent.ACTION_BATTERY_LOW)
        addAction(Intent.ACTION_BATTERY_OKAY)
        addAction(Intent.ACTION_POWER_CONNECTED)
        addAction(Intent.ACTION_POWER_DISCONNECTED)
        addAction(Intent.ACTION_POWER_USAGE_SUMMARY)
        addAction(UPDATE_BATTERY)
    }


    private var batteryStatus: Intent? = null

    private fun sendFlow(event: BatteryStateEvent) = coroutineScope.launch { msfUpdate.emit(event) }

    public fun registerBatteryReceiver() {
        unRegisterReceiver()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            batteryStatus = context.registerReceiver(batteryBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            batteryStatus = context.registerReceiver(batteryBroadcastReceiver, intentFilter)
        }
    }

    public var scope: Job? = null
    public fun registerBatteryUpdate(
        coroutine: CoroutineScope = coroutineScope,
        updateCycleTime: Long = 1000
    ) {
        registerBatteryReceiver()
        scope = coroutine.launch {

            while(isActive) {
                sendBroadcast()
                delay(updateCycleTime)
            }
            stopUpdateScope()
        }
    }

    public fun updateBatteryState() {
        sendBroadcast()
    }

    public fun stopUpdateScope() {
        scope?.cancel()
        scope = null
    }

    private fun updateBatteryInfo() {

        capacity.update(getCapacity())
        chargeCounter.update(getChargeCounter())
        chargePlug.update(getChargePlug())
//        chargePlugStr.update(getChargePlugStr())
        chargeStatus.update(getChargeStatus())
        currentAmpere.update(getCurrentAmpere())
        currentAverageAmpere.update(getCurrentAverageAmpere())
        energyCounter.update(getEnergyCounter())
        health.update(getHealth())
//        healthStr.update(getHealthStr())
        present.update(getPresent())
        temperature.update(getTemperature())
        totalCapacity.update(getTotalCapacity())
        voltage.update(getVoltage())
    }

    private fun sendBroadcast() {
        batteryStatus?.let {
            it.action = UPDATE_BATTERY
            context.sendBroadcast(it)
        }
    }

    private fun unRegisterReceiver() {
        batteryStatus?.let {
            try {
                context.unregisterReceiver(batteryBroadcastReceiver)
            } catch (e:Exception) {
//            e.printStackTrace()
            }
        }
        batteryStatus = null
    }

    /**
     * Instantaneous battery current in microamperes, as an integer.
     * Positive values indicate net current entering the battery from a charge source,
     * negative values indicate net current discharging from the battery.
     *
     * 순간 배터리 전류를 마이크로암페어 단위로 반환.
     * 양수 값은 충전 소스에서 배터리로 들어오는 순 전류, 음수 값은 배터리에서 방전되는 순 전류.
     *
     * @return The instantaneous battery current in microamperes.
     * @return 순간 배터리 전류 (마이크로암페어)
     */
    public fun getCurrentAmpere(): Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)

    /**
     * Average battery current in microamperes, as an integer.
     * 평균 배터리 전류를 마이크로암페어 단위로 반환.
     *
     * Positive values indicate net current entering the battery from a charge source,
     * negative values indicate net current discharging from the battery.
     *
     * 양수 값은 충전 소스에서 배터리로 들어오는 순 전류, 음수 값은 배터리에서 방전되는 순 전류.
     *
     * The time period over which the average is computed may depend on the fuel gauge hardware and its configuration.
     * Integer + is Charging
     * Integer - is Discharging
     * Unit microAmpere
     */
    public fun getCurrentAverageAmpere(): Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)

    /**
     * Battery charge status, from a BATTERY_STATUS_* value.
     * 배터리 충전 상태를 반환.
     *
     * @return The battery charge status.
     * @return 배터리 충전 상태 /
     * @see BatteryManager.BATTERY_STATUS_CHARGING
     * @see BatteryManager.BATTERY_STATUS_FULL
     * @see BatteryManager.BATTERY_STATUS_DISCHARGING
     * @see BatteryManager.BATTERY_STATUS_NOT_CHARGING
     * @see BatteryManager.BATTERY_STATUS_UNKNOWN
     */
    public fun getChargeStatus(): Int {
        val res = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        return if (res == ERROR_VALUE) {
            batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, ERROR_VALUE) ?: ERROR_VALUE
        } else {
            res
        }
    }

    public fun isCharging(): Boolean = getChargeStatus() == BatteryManager.BATTERY_STATUS_CHARGING
    public fun isDischarging(): Boolean = getChargeStatus() == BatteryManager.BATTERY_STATUS_DISCHARGING
    public fun isNotCharging(): Boolean = getChargeStatus() == BatteryManager.BATTERY_STATUS_NOT_CHARGING
    public fun isFull(): Boolean = getChargeStatus() == BatteryManager.BATTERY_STATUS_FULL

    /**
     * @see BatteryManager.BATTERY_PROPERTY_
     */
    private fun getIntProperty(batteryType:Int) = batteryManager.getIntProperty(batteryType)
    private fun getLongProperty(batteryType:Int) = batteryManager.getLongProperty(batteryType)

    /**
     * Remaining battery capacity as an integer percentage of total capacity (with no fractional part).
     * 남은 배터리 용량을 총 용량의 백분율로 반환.
     *
     * @return The remaining battery capacity as a percentage.
     * @return 남은 배터리 용량 (백분율)
     */
    public fun getCapacity(): Int = getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    /**
     * Battery capacity in microampere-hours, as an integer.
     * 배터리 용량을 마이크로암페어시 단위로 반환
     *
     * @return The battery capacity in microampere-hours.
     * @return 배터리 용량 (마이크로암페어시).
     */
    public fun getChargeCounter(): Int = getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)

    /**
     * Returns the battery remaining energy in nanowatt-hours.
     * 배터리 잔여 에너지를 나노와트시 단위로 반환.
     *
     * Warning!!, Values may not be accurate.
     * 경고!!, 값이 정확하지 않을 수 있음.
     *
     * Error value may be Long.MIN_VALUE.
     * 오류 값은 Long.MIN_VALUE일.
     *
     * @return The battery remaining energy in nanowatt-hours.
     * @return 배터리 잔여 에너지 (나노와트시).
     */
    public fun getEnergyCounter(): Long = getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)



    public val STR_CHARGE_PLUG_USB: String = "USB"
    public val STR_CHARGE_PLUG_AC: String = "AC"
    public val STR_CHARGE_PLUG_DOCK: String = "DOCK"
    public val STR_CHARGE_PLUG_UNKNOWN: String = "UNKNOWN"
    public val STR_CHARGE_PLUG_WIRELESS : String = "WIRELESS"

    /**
     * BatteryChargingPlugType
     * return BatteryManager
     * @see BatteryManager.BATTERY_PLUGGED_USB
     * @see BatteryManager.BATTERY_PLUGGED_AC
     * @see BatteryManager.BATTERY_PLUGGED_DOCK
     * @see BatteryManager.BATTERY_PLUGGED_WIRELESS
     * )
     * errorValue(-999)
     */
    public fun getChargePlug(): Int  =  batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, ERROR_VALUE) ?: ERROR_VALUE

    public fun isChargingUsb(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_USB
    public fun isChargingAc(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_AC

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public fun isChargingDock(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_DOCK
    public fun isChargingWireless(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_WIRELESS

    public fun getChargePlugStr(): String = when (getChargePlug()) {
        BatteryManager.BATTERY_PLUGGED_USB -> STR_CHARGE_PLUG_USB
        BatteryManager.BATTERY_PLUGGED_AC -> STR_CHARGE_PLUG_AC
        BatteryManager.BATTERY_PLUGGED_DOCK -> STR_CHARGE_PLUG_DOCK
        BatteryManager.BATTERY_PLUGGED_WIRELESS -> STR_CHARGE_PLUG_WIRELESS
        else -> STR_CHARGE_PLUG_UNKNOWN
    }

    /**
     * Battery Temperature
     * return double
     * error return errorValue(Integer.MIN_VALUE)
     */
    public fun getTemperature(): Double =
        (batteryStatus?.let { it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, ERROR_VALUE).toDouble() }
            ?: ERROR_VALUE.toDouble())/10.0

    /**
     * boolean indicating whether a battery is present.
     */
    public fun getPresent(): Boolean = batteryStatus?.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)?: false


    public val STR_BATTERY_HELTH_GOOD: String = "GOOD"
    public val STR_BATTERY_HELTH_COLD: String = "COLD"
    public val STR_BATTERY_HELTH_DEAD: String = "DEAD"
    public val STR_BATTERY_HELTH_OVER_VOLTAGE: String = "OVER_VOLTAGE"
    public val STR_BATTERY_HELTH_UNKNOWN: String = "UNKNOWN"

    /**
     * Battery Health Status
     *
     * return BatteryManager(
     *  BATTERY_HEALTH_GOOD or
     *  BATTERY_HEALTH_COLD or
     *  BATTERY_HEALTH_DEAD or
     *  )
     * error return errorValue(-999)
     */
    public fun getHealth(): Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, ERROR_VALUE) ?: ERROR_VALUE
    public fun isHealthGood(): Boolean = getHealth() == BatteryManager.BATTERY_HEALTH_GOOD
    public fun isHealthCool(): Boolean = getHealth() == BatteryManager.BATTERY_HEALTH_COLD
    public fun isHealthDead(): Boolean = getHealth() == BatteryManager.BATTERY_HEALTH_DEAD
    public fun isHealthOverVoltage(): Boolean = getHealth() == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE
    public fun getHealthStr(healthType: Int): String = when (healthType) {
        BatteryManager.BATTERY_HEALTH_GOOD -> STR_BATTERY_HELTH_GOOD
        BatteryManager.BATTERY_HEALTH_COLD -> STR_BATTERY_HELTH_COLD
        BatteryManager.BATTERY_HEALTH_DEAD -> STR_BATTERY_HELTH_DEAD
        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> STR_BATTERY_HELTH_OVER_VOLTAGE
        else -> STR_BATTERY_HELTH_UNKNOWN
    }

    public fun getCurrentHealthStr():String = getHealthStr(getHealth())


    /**
     * return volt (ex 3.5)
     * error is errorValue(-999.0)
     */
    public fun getVoltage(): Double = (batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, ERROR_VALUE * 1000) ?: ERROR_VALUE * 1000).toDouble() / 1000


    /**
     * return (ex Li-ion)
     * error is null
     */
    public fun getTechnology(): String? = batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)


    /**
     * battery total capacity (rated capacity)
     * unit mAh
     *
     * return (ex 4000.0)
     * error is null
     */
    public fun getTotalCapacity(): Double =
        powerProfile.getAveragePower(PowerProfileVO.POWER_BATTERY_CAPACITY) as Double

    public override fun onDestroy() {
        super.onDestroy()
        stopUpdateScope()
        unRegisterReceiver()
    }
}
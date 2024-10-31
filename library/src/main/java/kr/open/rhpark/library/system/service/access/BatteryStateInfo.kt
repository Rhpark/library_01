package kr.open.rhpark.library.system.service.access

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.RequiresApi
import kr.open.rhpark.library.system.service.base.BaseSystemService
import kr.open.rhpark.library.system.service.access.power.PowerProfile
import kr.open.rhpark.library.system.service.access.power.PowerProfileVO

/**
 *
 * Using BatteryManager, PowerProfile
 * BatteryManager

 *
 * <permission android:name="android.permission.BATTERY_STATS" />
 */
public class BatteryStateInfo(
    context: Context,
    private val batteryManager: BatteryManager,
) : BaseSystemService(context, arrayOf(android.Manifest.permission.BATTERY_STATS)) {

    private val batteryStatus: Intent? = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    private val powerProfile: PowerProfile = PowerProfile(context)

    public val ERROR_VALUE: Int = Integer.MIN_VALUE


    /**
     * Instantaneous battery current in microamperes, as an integer.
     * Positive values indicate net current entering the battery from a charge source,
     * negative values indicate net current discharging from the battery.
     * Integer + is Charging
     * Integer - is Discharging
     * Unit microAmpere
     */
    public fun getCurrentAmpere(): Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)

    /**
     * Average battery current in microamperes, as an integer.
     * Positive values indicate net current entering the battery from a charge source,
     * negative values indicate net current discharging from the battery.
     * The time period over which the average is computed may depend on the fuel gauge hardware and its configuration.
     * Integer + is Charging
     * Integer - is Discharging
     * Unit microAmpere
     */
    public fun getCurrentAverageAmpere(): Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)

    /**
     * Battery charge status, from a BATTERY_STATUS_* value.
     * return BatteryManager(
     *  BATTERY_STATUS_CHARGING,
     *  BATTERY_STATUS_FULL,
     *  BATTERY_STATUS_DISCHARGING,
     *  BATTERY_STATUS_NOT_CHARGING,
     *  BATTERY_STATUS_UNKNOWN)
     */
    public fun getStatus(): Int =
        if (batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS) == ERROR_VALUE) {
            batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, ERROR_VALUE) ?: ERROR_VALUE
        } else {
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        }

    public fun isCharging(): Boolean = getStatus() == BatteryManager.BATTERY_STATUS_CHARGING
    public fun isDischarging(): Boolean = getStatus() == BatteryManager.BATTERY_STATUS_DISCHARGING
    public fun isNotCharging(): Boolean = getStatus() == BatteryManager.BATTERY_STATUS_NOT_CHARGING
    public fun isFull(): Boolean = getStatus() == BatteryManager.BATTERY_STATUS_FULL

    /**
     * Remaining battery capacity as an integer percentage of total capacity (with no fractional part).
     * return percent
     */
    public fun getCapacity(): Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    /**
     * Battery capacity in microampere-hours, as an integer.
     */
    public fun getChargeCounter(): Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)

    /**
     * Battery remaining energy in nanowatt-hours, as a long integer.
     * Warring!!, Values may not be accurate
     * Error value may be Long.MIN_VALUE
     */
    public fun getEnergyCounter(): Long = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)

    /**
     * BatteryChargingPlugType
     * return BatteryManager(
     *  BATTERY_PLUGGED_USB
     *  BATTERY_PLUGGED_AC
     *  BATTERY_PLUGGED_DOCK
     *  BATTERY_PLUGGED_WIRELESS
     *  BATTERY_PLUGGED_ANY
     * )
     * errorValue(-999)
     */
    public fun getChargePlug(): Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, ERROR_VALUE) ?: ERROR_VALUE
    public fun isChargingUsb(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_USB
    public fun isChargingAc(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_AC

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public fun isChargingDock(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_DOCK
    public fun isChargingWireless(): Boolean = getChargePlug() == BatteryManager.BATTERY_PLUGGED_WIRELESS
//    fun isChargingAny() = getChargePlug() == BatteryManager.BATTERY_PLUGGED_ANY

    /**
     * Battery Temperature
     * return double
     * error return errorValue(-999.0)
     */
    public fun getTemperature(): Double = (batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, ERROR_VALUE * 10) ?: ERROR_VALUE * 10).toDouble() / 10

    /**
     * boolean indicating whether a battery is present.
     */
    public fun getPresent(): Boolean? = batteryStatus?.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)


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
    public fun getTotalCapacity(): Any? = powerProfile.getAveragePower(PowerProfileVO.POWER_BATTERY_CAPACITY)
}
package kr.open.rhpark.library.system.service.info.battery

public sealed class BatteryStateEvent {
    public data class OnCapacity(val percent: Int) : BatteryStateEvent()
    public data class OnChargeCounter(val counter: Int) : BatteryStateEvent()
    public data class OnChargePlug(val type: Int) : BatteryStateEvent()
    public data class OnTemperature(val temperature: Double) : BatteryStateEvent()
    public data class OnVoltage(val voltage: Double) : BatteryStateEvent()
    public data class OnCurrentAmpere(val current: Int) : BatteryStateEvent()
    public data class OnCurrentAverageAmpere(val current: Int) : BatteryStateEvent()
    public data class OnChargeStatus(val status: Int) : BatteryStateEvent()
    public data class OnHealth(val health: Int) : BatteryStateEvent()
    public data class OnEnergyCounter(val energy: Long) : BatteryStateEvent()
    public data class OnPresent(val present: Boolean) : BatteryStateEvent()
    public data class OnTotalCapacity(val totalCapacity: Double) : BatteryStateEvent()
}

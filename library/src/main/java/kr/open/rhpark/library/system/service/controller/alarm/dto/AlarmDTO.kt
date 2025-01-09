package kr.open.rhpark.library.system.service.controller.alarm.dto

/**
 *
 */
public class AlarmDTO(
    public val key: Int,
    public val title: String,
    public val msg: String,
    public val isActive: Boolean = false,
    public val isAllowIdle:Boolean= false,
    public val isVibrate: Boolean = false,
    public val sound: Int = 100,
    public val hour: Int,
    public val minute: Int,
    public val second: Int,
    public val acquireTime: Long = 3000
) {
}
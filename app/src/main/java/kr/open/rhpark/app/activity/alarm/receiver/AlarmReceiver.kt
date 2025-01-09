package kr.open.rhpark.app.activity.alarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import kr.open.rhpark.app.R
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.controller.alarm.dto.AlarmDTO
import kr.open.rhpark.library.system.service.controller.alarm.receiver.BaseAlarmReceiver
import kr.open.rhpark.library.system.service.controller.alarm.vo.AlarmVO
import kr.open.rhpark.library.util.extensions.context.getNotificationController

public class AlarmReceiver() : BaseAlarmReceiver() {

//    override val registerType = RegisterType.ALARM_EXACT_AND_ALLOW_WHILE_IDLE
    override val registerType = RegisterType.ALARM_CLOCK

    override val classType: Class<*> = this::class.java

    override val powerManagerAcquireTime: Long get() = 5000L

    override fun loadAllAlarmDtoList(): List<AlarmDTO> {
        //data load from realm or room or sharedpreference or other
        return emptyList<AlarmDTO>()
    }

    override fun loadAlarmDtoList(intent: Intent, alarmKey: Int): AlarmDTO? {
        Logx.d("alarmKey is " + alarmKey)
        if(alarmKey == AlarmVO.ALARM_KEY_DEFAULT_VALUE) {
            Logx.e("Error Alarm Key $alarmKey")
            return null
        }

        //data load from realm or room or sharedpreference or other
        return AlarmDTO(
            11,
            "test001",
            "msg001",
            true,
            true,
            true,
            100,
            11,
            0,
            0
        )
    }

    override fun createNotificationChannel(context: Context, alarmDto: AlarmDTO) {
        Logx.d()
        notificationController = context.getNotificationController().apply {
            createChannel(
                NotificationChannel("Alarm_ID", "Alarm_Name", NotificationManager.IMPORTANCE_HIGH).apply {
//            setShowBadge(true)
                }
            )
        }
    }
    override fun showNotification(context: Context, alarmDto: AlarmDTO) {
        Logx.d()

        notificationController.showNotificationForBroadcast(
            alarmDto.key,
            alarmDto.title,
            alarmDto.msg,
            false,
            R.drawable.ic_floating_fixed_close,
            null,
            null,
            null
        )
    }
}
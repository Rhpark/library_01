package kr.open.rhpark.app.activity.alarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import kr.open.rhpark.app.R
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.controller.alarm.dto.AlarmDTO
import kr.open.rhpark.library.system.service.controller.alarm.receiver.BaseAlarmReceiver
import kr.open.rhpark.library.util.extensions.context.getNotificationController

public class AlarmReceiver():BaseAlarmReceiver() {
    override val registerType = RegisterType.ALARM_EXACT_AND_ALLOW_WHILE_IDLE
    override val classType: Class<*> = this::class.java

    override fun loadAllAlarmDtoList(): List<AlarmDTO> {
        return emptyList<AlarmDTO>()
    }

    override fun loadAlarmDtoList(intent: Intent): AlarmDTO? {
        return AlarmDTO(
            11,
            "test001",
            "msg001",
            true,
            true,
            true,
            100,
            15,
            30,
            0
        )
    }

    override val notificationId: Int = 1

    override fun getNotificationChannel(): NotificationChannel =
        NotificationChannel("Alarm_ID","Alarm_Name", NotificationManager.IMPORTANCE_HIGH).apply {

        }

    override fun showNotification(context: Context, alarmDto: AlarmDTO) {
        Logx.d()
        notificationController = context.getNotificationController().apply {
            createChannel(getNotificationChannel())
        }
        notificationController.showNotificationForActivity(
            notificationId,
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
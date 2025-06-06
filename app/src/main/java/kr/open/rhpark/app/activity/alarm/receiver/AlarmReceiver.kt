package kr.open.rhpark.app.activity.alarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.alarm.AlarmSharedPreference
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.domain.common.systemmanager.controller.alarm.dto.AlarmDTO
import kr.open.rhpark.library.domain.common.systemmanager.controller.alarm.receiver.BaseAlarmReceiver
import kr.open.rhpark.library.domain.common.systemmanager.controller.alarm.vo.AlarmVO
import kr.open.rhpark.library.domain.common.systemmanager.controller.notification.dto.SimpleNotificationOption
import kr.open.rhpark.library.domain.common.systemmanager.controller.notification.vo.SimpleNotificationType
import kr.open.rhpark.library.util.extensions.context.getNotificationController

/**
 * AndroidManifest.xml
 * <receiver android:name=".activity.alarm.receiver.AlarmReceiver"
 *     android:enabled="true"
 *     android:exported="true">
 *     <intent-filter>
 *         <action android:name="android.intent.action.BOOT_COMPLETED"/>
 *     </intent-filter>
 *
 * </receiver>
 */
public class AlarmReceiver() : BaseAlarmReceiver() {

//    override val registerType = RegisterType.ALARM_EXACT_AND_ALLOW_WHILE_IDLE
    override val registerType = RegisterType.ALARM_CLOCK

    override val classType: Class<*> = this::class.java

    override val powerManagerAcquireTime: Long get() = 5000L

    override fun loadAllAlarmDtoList(context:Context): List<AlarmDTO> {
        //data load from realm or room or sharedpreference or other
        return emptyList<AlarmDTO>()
    }

    override fun loadAlarmDtoList(context:Context, intent: Intent, alarmKey: Int): AlarmDTO? {
        Logx.d("alarmKey is " + alarmKey)
        if(alarmKey == AlarmVO.ALARM_KEY_DEFAULT_VALUE) {
            Logx.e("Error Alarm Key $alarmKey")
            return null
        }

        //data load from realm or room or  other
        return AlarmSharedPreference(context).loadAlarm()
    }

    override fun createNotificationChannel(context: Context, alarmDto: AlarmDTO) {
        Logx.d()
        notificationController = context.getNotificationController(SimpleNotificationType.BROADCAST).apply {
            createChannel(
                NotificationChannel("Alarm_ID", "Alarm_Name", NotificationManager.IMPORTANCE_HIGH).apply {
//            setShowBadge(true)
                    alarmDto.vibrationEffect?.let {
                        enableVibration(true)
                        vibrationPattern = it
                    }
                    alarmDto.sound?.let {
                        setSound(
                            it, AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build()
                        )
                    }
                }
            )
        }
    }
    override fun showNotification(context: Context, alarmDto: AlarmDTO) {
        Logx.d()
        notificationController.showNotification(
            SimpleNotificationOption(
                alarmDto.key,
                alarmDto.title,
                alarmDto.msg,
                false,
                R.drawable.ic_floating_fixed_close,
            )
        )
    }
}
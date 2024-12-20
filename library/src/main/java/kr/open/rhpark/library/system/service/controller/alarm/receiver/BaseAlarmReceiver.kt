package kr.open.rhpark.library.system.service.controller.alarm.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.controller.NotificationController
import kr.open.rhpark.library.system.service.controller.alarm.AlarmController
import kr.open.rhpark.library.system.service.controller.alarm.dto.AlarmDTO
import kr.open.rhpark.library.util.extensions.context.getAlarmController
import kr.open.rhpark.library.util.extensions.context.getPowerManager

public abstract class BaseAlarmReceiver : BroadcastReceiver() {

    protected lateinit var notificationController: NotificationController
    protected abstract val notificationId: Int

    protected abstract val registerType: RegisterType
    protected abstract val classType: Class<*>

    protected abstract fun getNotificationChannel(): NotificationChannel

    protected abstract fun showNotification(context: Context, alarmDTO: AlarmDTO)

    protected abstract fun loadAllAlarmDtoList(): List<AlarmDTO>
    protected abstract fun loadAlarmDtoList(intent: Intent): AlarmDTO?

    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context?, intent: Intent?) {
        Logx.d()
        context?.let { context ->
            val pm = context.getPowerManager()
            val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmReceiver").apply {
                acquire(10 * 60 * 1000L /*10 minutes*/)
            }
            val alarmController = context.getAlarmController()

            intent?.let { intent ->
                Logx.d(" intent.action ${intent.action}")
                if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                    //data load
                    loadAllAlarmDtoList().forEach {
                        registerAlarm(alarmController, it)
                    }
                } else {
                    // intent를 통한 알람 리스트 유무 확인
                    // alarmDto Load
                    loadAlarmDtoList(intent)?.let {
                        getNotificationChannel()
                        showNotification(context, it)
                    } ?: Logx.e("")
                }
            }
            wl.release()
        }
    }

    private fun registerAlarm(alarmController: AlarmController, alarmDto: AlarmDTO): Unit =
        when (registerType) {
            RegisterType.ALARM_AND_ALLOW_WHILE_IDLE -> {
                alarmController.registerAlarmAndAllowWhileIdle(classType, alarmDto)
            }

            RegisterType.ALARM_CLOCK -> {
                alarmController.registerAlarmClock(classType, alarmDto)
            }

            RegisterType.ALARM_EXACT_AND_ALLOW_WHILE_IDLE -> {
                alarmController.registerAlarmExactAndAllowWhileIdle(classType, alarmDto)
            }
        }

    public enum class RegisterType {
        ALARM_CLOCK,
        ALARM_AND_ALLOW_WHILE_IDLE,
        ALARM_EXACT_AND_ALLOW_WHILE_IDLE
    }
}

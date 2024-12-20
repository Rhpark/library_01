package kr.open.rhpark.app.activity.alarm


import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.RECEIVE_BOOT_COMPLETED
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.Manifest.permission.WAKE_LOCK
import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.alarm.receiver.AlarmReceiver
import kr.open.rhpark.app.databinding.ActivityAlarmBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.controller.alarm.dto.AlarmDTO
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getAlarmController
import kr.open.rhpark.library.util.extensions.context.getNotificationController

class AlarmActivity :
    BaseBindingActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {


    private val notificationController by lazy { getNotificationController() }
    private val alarmController by lazy { getAlarmController() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(
            listOf(
                POST_NOTIFICATIONS,
                RECEIVE_BOOT_COMPLETED,
                SCHEDULE_EXACT_ALARM,
                WAKE_LOCK
            )
        ) { requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
            if (deniedPermissions.isEmpty()) {

            }
            initListener()
        }

    }

    private fun initListener() {
        binding.run {
            btnAlarmRegister.setOnClickListener {
                Logx.d()
                val a = AlarmDTO(
                    1,
                    "test002",
                    "msg002",
                    true,
                    true,
                    true,
                    100,
                    15,
                    30,
                    0
                )
                alarmController.registerAlarmAndAllowWhileIdle(AlarmReceiver::class.java, a)
            }
        }
    }

}
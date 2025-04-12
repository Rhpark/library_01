package kr.open.rhpark.app.activity.alarm


import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.RECEIVE_BOOT_COMPLETED
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.Manifest.permission.USE_EXACT_ALARM
import android.Manifest.permission.WAKE_LOCK
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.alarm.receiver.AlarmReceiver
import kr.open.rhpark.app.databinding.ActivityAlarmBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.domain.common.systemmanager.controller.alarm.dto.AlarmDTO
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getAlarmController
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort
import kr.open.rhpark.library.util.extensions.conditional.sdk_version.checkSdkVersion
import java.time.LocalDateTime

class AlarmActivity :
    BaseBindingActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {

    private val alarmController by lazy { getAlarmController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(getPermissionList()) { deniedPermissions ->
            Logx.d("deniedPermissions $deniedPermissions")
            if (deniedPermissions.isEmpty()) {

            }
            initListener()
        }
    }

    private fun initListener() {
        binding.run {
            btnAlarmRegister.setOnClickListener {
                val edit = edtTimer.text
                if (edit.isNullOrEmpty()) {
                    toastShowShort("input Min timer")
                } else if (edit.toString().toInt() < 1) {
                    toastShowShort("over than 0")
                }else {
                    Logx.d()
                    val min = edtTimer.text.toString().toInt()
                    val localDataTime = LocalDateTime.now()
                    val a = AlarmDTO(
                        key = 1,
                        title = "test002",
                        msg = "msg002",
                        isActive = true,
                        isAllowIdle = true,
                        vibrationEffect = longArrayOf(0,250,500,250),
                        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                        hour =localDataTime.hour,
                        minute = localDataTime.minute + min ,
                        second = 0
                    )
                    AlarmSharedPreference(applicationContext).saveAlarm(a)
                    alarmController.registerAlarmClock(AlarmReceiver::class.java, a)
                }
            }
        }
    }

    private fun getPermissionList(): List<String> {
        val list = mutableListOf<String>()
        list.add(RECEIVE_BOOT_COMPLETED)
        list.add(WAKE_LOCK)
        checkSdkVersion(Build.VERSION_CODES.TIRAMISU) {
            list.add(USE_EXACT_ALARM)
            list.add(POST_NOTIFICATIONS)
        }
        checkSdkVersion(Build.VERSION_CODES.S) {
            list.add(SCHEDULE_EXACT_ALARM)
        }
        return list.toList()
    }

}
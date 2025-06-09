package kr.open.rhpark.app.activity.notification


import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityNotificationBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.domain.common.systemmanager.controller.notification.dto.SimpleNotificationOption
import kr.open.rhpark.library.domain.common.systemmanager.controller.notification.dto.SimplePendingIntentOption
import kr.open.rhpark.library.domain.common.systemmanager.controller.notification.dto.SimpleProgressNotificationOption
import kr.open.rhpark.library.domain.common.systemmanager.controller.notification.vo.SimpleNotificationType
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getNotificationController
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class NotificationActivity :
    BaseBindingActivity<ActivityNotificationBinding>(R.layout.activity_notification) {

    private val CHANNEL_ID = "Channel_ID_01"
    private val CHANNEL_NAME = "Channel_NAME_01"
    private val data01 = "PutData_01"
    private val dataAction01 = "PutData_Action_01"
    private val dataAction02 = "PutData_Action_02"

    private val notificationController by lazy { getNotificationController(SimpleNotificationType.ACTIVITY) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(listOf(POST_NOTIFICATIONS)) { deniedPermissions ->
            Logx.d("deniedPermissions $deniedPermissions")
            if (deniedPermissions.isEmpty()) {
                initListener()
            }
        }

        intent.extras?.run {
            val data01 = getString(data01)
            val data02 = getString(dataAction01)
            val data03 = getInt(dataAction02)
            binding.tvReceiveIntentData.text = "intent extras = $data01, action 01 =  $data02, action 02 =  ${data03}"
        } ?: Logx.d("intent.extras is Null")
    }

    private fun initListener() {
        binding.run {
            initNotificationChannel()

            btnBigTextNotification.setOnClickListener {

                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)
                val notificationId = 2
                val getNotificationIntent1 = getNotificationIntent(4, dataAction01, "action01 Click", "Action01",notificationId)
                val getNotificationIntent2 = getNotificationIntent(4, dataAction02, 12313, "Action02",16)


                notificationController.showNotificationBigText(
                    SimpleNotificationOption(
                        notificationId,
                        "BigTextNotification",
                        "BigText",
                        true,
                        R.drawable.ic_floating_fixed_close,
                        snippet = "Fesafasdfasefdsfasefesadf asef sadf asdf asefas fdsf seaf safawef aa feasf sadfasefasdf asdf asf aesf asdf asfe saedf asef sadf asef asdf asefseaf",
                        clickIntent = clickIntent,
                        largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.bg_notifi),
                        actions = listOf(getNotificationIntent1, getNotificationIntent2)
                    )
                )
            }

            btnBitImageNotification.setOnClickListener {

                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)
                val notificationId = 4
                val getNotificationIntent1 = getNotificationIntent(4, dataAction01, "action01 Click", "Action01",notificationId)
                val getNotificationIntent2 = getNotificationIntent(4, dataAction02, 12313, "Action02",6)


                notificationController.showNotificationBigImage(
                    SimpleNotificationOption(
                        notificationId,
                        "BigImageNotification",
                        "BigImage",
                        true,
                        R.drawable.ic_floating_fixed_close,
                        clickIntent = clickIntent,
                        largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.bg_notifi),
                        actions = listOf(getNotificationIntent1, getNotificationIntent2)
                    )
                )
            }

            btnShowProgressNotification.setOnClickListener {
                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)
                val notificationId = 16

                notificationController.showProgressNotification(
                    SimpleProgressNotificationOption(
                        notificationId,
                        "TitleProgress01",
                        "ContentsProgress01",
                        false,
                        R.drawable.ic_floating_fixed_close,
                        clickIntent = clickIntent,
                        progressPercent = 0,
                    )
                )

                thread {
                    for (i in 0..10) {
                        notificationController.updateProgress(notificationId, i * 10)
                        sleep(1000)
                    }

                    notificationController.completeProgress(notificationId)
                }
            }

            btnAllClearNotification.setOnClickListener {
                notificationController.cancelAll()
            }
        }
    }

    private fun initNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Channel_Description_01"
            setShowBadge(true)

            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audio = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            setSound(uri, audio)
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 100, 200)
        }

        notificationController.createChannel(channel)
    }

    private fun getNotificationIntent(notificationId:Int, actionKey:String, actionValue:Any, title:String,actionId:Int): NotificationCompat.Action {

        val actionIntent1 = Intent(applicationContext, NotificationActivity::class.java).apply {
            when(actionValue) {
                is Int -> putExtra(actionKey, actionValue)
                is String -> putExtra(actionKey, actionValue)
                is Byte -> putExtra(actionKey, actionValue)
                is Char -> putExtra(actionKey, actionValue)
                is Float -> putExtra(actionKey, actionValue)
                is Long -> putExtra(actionKey, actionValue)
                is Double -> putExtra(actionKey, actionValue)
                is Boolean -> putExtra(actionKey, actionValue)
            }
        }
        val actionPendingIntent1 = NotificationCompat.Action(
            notificationId,
            title,
            notificationController.getClickShowActivityPendingIntent(
                SimplePendingIntentOption(actionId, actionIntent1)
            )
        )
        return actionPendingIntent1
    }
}
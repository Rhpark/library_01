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
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
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

    private val notificationController by lazy { getNotificationController() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(listOf(POST_NOTIFICATIONS)) { requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
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
                val notificationId = 2
                val clickIntent = Intent(applicationContext, NotificationActivity::class.java).apply {
                    putExtra(data01, "notificationClick")
                }

                val actionIntent1 = Intent(applicationContext, NotificationActivity::class.java).apply {
                    putExtra(dataAction01, "action01 Click")
                }
                val actionPendingIntent1 = NotificationCompat.Action(
                    notificationId,
                    "Action01",
                    notificationController.getClickShowActivityPendingIntent(4, actionIntent1)
                )

                val actionIntent2 = Intent(applicationContext, NotificationActivity::class.java).apply {
                    putExtra(dataAction02, 12313)
                }
                val actionPendingIntent2 = NotificationCompat.Action(
                    notificationId,
                    "Action02",
                    notificationController.getClickShowActivityPendingIntent(6, actionIntent2)
                )

                notificationController.showNotificationBigTextForActivity(
                    notificationId,
                    "BigTextNotification",
                    "BigText",
                    true,
                    R.drawable.ic_floating_fixed_close,
                    snippet = "Fesafasdfasefdsfasefesadf asef sadf asdf asefas fdsf seaf safawef aa feasf sadfasefasdf asdf asf aesf asdf asfe saedf asef sadf asef asdf asefseaf",
                    clickIntent = clickIntent,
                    largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.bg_notifi),
                    actions = listOf(actionPendingIntent1, actionPendingIntent2)
                )
            }

            btnBitImageNotification.setOnClickListener {

                val notificationId = 4

                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)

                val actionIntent1 = Intent(applicationContext, NotificationActivity::class.java).apply {
                    putExtra(dataAction01, "action01 Click")
                }
                val actionPendingIntent1 = NotificationCompat.Action(
                    notificationId,
                    "Action01",
                    notificationController.getClickShowActivityPendingIntent(4, actionIntent1)
                )

                val actionIntent2 = Intent(applicationContext, NotificationActivity::class.java).apply {
                    putExtra(dataAction02, 12313)
                }
                val actionPendingIntent2 = NotificationCompat.Action(
                    notificationId,
                    "Action02",
                    notificationController.getClickShowActivityPendingIntent(6, actionIntent2)
                )

                notificationController.showNotificationBigImageForActivity(
                    notificationId,
                    "BigImageNotification",
                    "BigImage",
                    true,
                    R.drawable.ic_floating_fixed_close,
                    clickIntent = clickIntent,
                    largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.bg_notifi),
                    actions = listOf(actionPendingIntent1, actionPendingIntent2)
                )
            }

            btnShowProgressNotification.setOnClickListener {
                val notificationId = 16
                val actionID = 18
                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)
                val actionIntent = Intent(applicationContext, NotificationActivity::class.java).apply {
                        putExtra(data01, "Pending01")
                        putExtra(dataAction01, 123)
                }

                val builder = notificationController.showProgressNotificationForActivity(
                    notificationId,
                    "TitleProgress01",
                    "ContentsProgress01",
                    false,
                    R.drawable.ic_floating_fixed_close,
                    clickIntent = clickIntent,
                    progressPercent = 0,
                )

                thread {
                    for (i in 0..10) {
                        builder.setProgress(100, i * 10, false)
                        notificationController.notify(notificationId, builder.build())
                        sleep(1000)
                    }
                    builder.addAction(
                        NotificationCompat.Action(
                            notificationId,
                            "Action01",
                            notificationController.getClickShowActivityPendingIntent(actionID, actionIntent)
                        )
                    )
                    notificationController.notify(notificationId, builder.build())
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

}
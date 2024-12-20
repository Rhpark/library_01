package kr.open.rhpark.library.system.service.controller

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationCompat.PRIORITY_LOW
import kr.open.rhpark.library.system.service.base.BaseSystemService

/**
 * <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
 *
 * NotificationManager.IMPORTANCE_HIGH	긴급 상황이며 알림음이 울리며 헤드업으로 표시
 * NotificationManager.IMPORTANCE_DEFAULT	높은 중요도이며 알림음이 울림
 * NotificationManager.IMPORTANCE_LOW	중간 중요도이며 알림음이 울리지 않음
 * NotificationManager.IMPORTANCE_MIN   낮은 중요도이며 알림음이 없고 상태표시줄에 표시되지 않음
 */
public class NotificationController(
    context: Context,
    public val notificationManager: NotificationManager,

) : BaseSystemService(context, listOf(POST_NOTIFICATIONS)) {


    private var currentChannel: NotificationChannel? = null
    public fun createChannel(notificationChannel:NotificationChannel) {
        currentChannel = notificationChannel
        notificationManager.createNotificationChannel(notificationChannel)
    }

    public fun createChannel(
        channelId: String,
        channelName: String,
        importance: Int,
        description: String? = null
    ): NotificationChannel = NotificationChannel(channelId, channelName, importance).apply {
        description?.let { this.description = it }
    }

    public fun getBuilder(
        title: String? = null,
        content: String? = null,
        isAutoCancel: Boolean = false,
        smallIcon: Int? = null,
        largeIcon: Bitmap? = null
    ): NotificationCompat.Builder {
        return currentChannel?.let {
            NotificationCompat.Builder(context, it.id).apply {
                title?.let { setContentTitle(it) }
                content?.let { setContentText(it) }
                setAutoCancel(isAutoCancel)
                smallIcon?.let { setSmallIcon(it) }
                largeIcon?.let { setLargeIcon(it) }
            }
        }?: throw Exception("Perform Function before that createChannel")
    }

    public fun showNotificationForActivity(
        notificationId: Int,
        title: String? = null,
        content: String? = null,
        isAutoCancel: Boolean = false,
        smallIcon: Int? = null,
        largeIcon: Bitmap? = null,
        clickIntent: Intent? = null,
        actions: List<Action>? = null,
    ) {
        val builder = getBuilder(title, content, isAutoCancel, smallIcon, largeIcon).apply {
            clickIntent?.let {
                setContentIntent(getClickShowActivityPendingIntent(notificationId, it))
            }
            actions?.forEach { addAction(it) }
        }
       notificationManager.notify(notificationId, builder.build())
    }

    public fun showNotificationForBroadcast(
        notificationId: Int,
        title: String? = null,
        content: String? = null,
        isAutoCancel: Boolean = false,
        smallIcon: Int? = null,
        largeIcon: Bitmap? = null,
        clickIntent: Intent? = null,
        actions: List<Action>? = null,
    ) {
        val builder = getBuilder(title, content, isAutoCancel, smallIcon, largeIcon).apply {
            clickIntent?.let {
                setContentIntent(getClickShowBroadcastPendingIntent(notificationId, it))
            }
            actions?.forEach { addAction(it) }
        }
        notificationManager.notify(notificationId, builder.build())
    }

    public fun showNotificationBigImageForActivity(
        notificationId: Int,
        title: String? = null,
        content: String? = null,
        isAutoCancel: Boolean = false,
        smallIcon: Int? = null,
        largeIcon: Bitmap,
        clickIntent: Intent? = null,
        actions: List<Action>? = null,
    ) {
        val builder = getBuilder(title, content, isAutoCancel, smallIcon, largeIcon).apply {
            clickIntent?.let {
                setContentIntent(getClickShowActivityPendingIntent(notificationId, it))
            }
            largeIcon?.let {
                setStyle(NotificationCompat.BigPictureStyle().bigPicture(it))
            }
            actions?.forEach { addAction(it) }
        }
        notificationManager.notify(notificationId, builder.build())
    }

    public fun showNotificationBigTextForActivity(
        notificationId: Int,
        title: String? = null,
        content: String? = null,
        isAutoCancel: Boolean = false,
        smallIcon: Int? = null,
        largeIcon: Bitmap? = null,
        snippet: String,
        clickIntent: Intent? = null,
        actions: List<Action>? = null,
    ) {
        val builder = getBuilder(title, content, isAutoCancel, smallIcon, largeIcon).apply {
            clickIntent?.let {
                setContentIntent(getClickShowActivityPendingIntent(notificationId, it))
            }
            snippet?.let {
                setStyle(NotificationCompat.BigTextStyle().bigText(snippet))
            }
            actions?.forEach { addAction(it) }
        }
        notificationManager.notify(notificationId, builder.build())
    }

    public fun showNotificationForActionsActivity(
        notificationId: Int,
        title: String? = null,
        content: String? = null,
        isAutoCancel: Boolean = false,
        smallIcon: Int? = null,
        largeIcon: Bitmap? = null,
        clickIntent: Intent? = null,
        actions: List<Action>
    ) : NotificationCompat.Builder {
        val builder = getBuilder(title, content, isAutoCancel, smallIcon, largeIcon).apply {
            clickIntent?.let {
                setContentIntent(getClickShowActivityPendingIntent(notificationId, it))
            }
            actions?.forEach { addAction(it) }
        }
        notificationManager.notify(notificationId, builder.build())

        return builder
    }

    public fun showProgressNotificationForActivity(
        notificationId: Int,
        title: String? = null,
        content: String? = null,
        isAutoCancel: Boolean = false,
        smallIcon: Int? = null,
        largeIcon: Bitmap? = null,
        clickIntent: Intent? = null,
        action01: Action? = null,
        action02: Action? = null,
        progressPercent: Int
    ) : NotificationCompat.Builder {
        val builder = getBuilder(title, content, isAutoCancel, smallIcon, largeIcon).apply {
            clickIntent?.let {
                setContentIntent(getClickShowActivityPendingIntent(notificationId, it))
            }
            setPriority(PRIORITY_LOW)
            setProgress(100, progressPercent, false)
            action01?.let { addAction(it) }
            action02?.let { addAction(it) }
        }
        notificationManager.notify(notificationId, builder.build())
        return builder
    }

    public fun cancelNotification(tag: String?, notificationId: Int) {
        try {
            tag?.let { notificationManager.cancel(tag, notificationId) }
                ?: notificationManager.cancel(notificationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public fun cancelAll(): Unit = notificationManager.cancelAll()

    public fun getClickShowActivityPendingIntent(
        actionId: Int,
        clickIntent: Intent,
        flags: Int = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    ): PendingIntent = PendingIntent.getActivity(context, actionId, clickIntent, flags)

    public fun getClickShowServicePendingIntent(
        actionId: Int,
        clickIntent: Intent,
        flags: Int = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    ): PendingIntent = PendingIntent.getService(context, actionId, clickIntent, flags)

    public fun getClickShowBroadcastPendingIntent(
        actionId: Int,
        clickIntent: Intent,
        flags: Int = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    ): PendingIntent = PendingIntent.getBroadcast(context, actionId, clickIntent, flags)

    public fun notify(notificationId: Int,build: Notification) {
        notificationManager.notify(notificationId, build)
    }

}
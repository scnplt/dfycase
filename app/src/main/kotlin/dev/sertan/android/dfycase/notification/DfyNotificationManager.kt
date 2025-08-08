package dev.sertan.android.dfycase.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import dev.sertan.android.dfycase.R

internal object DfyNotificationManager {

    fun canPostNotification(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun sendNotification(
        context: Context,
        title: String?,
        content: String?,
        id: Int = 1,
        autoCancel: Boolean = true,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        @DrawableRes icon: Int = R.drawable.ic_launcher_foreground,
    ) {
        if (!canPostNotification(context)) return;

        val channelId = context.getString(R.string.notif_channel_default_id)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(priority)
            .setAutoCancel(autoCancel)

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDefaultNotificationChannel(context, manager, channelId)
        }

        manager.notify(id, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDefaultNotificationChannel(
        context: Context,
        manager: NotificationManager,
        channelId: String
    ) {
        val channelName = context.getString(R.string.notif_channel_default_name)
        val channel = NotificationChannel(
            channelId,
            channelName,
            IMPORTANCE_DEFAULT
        ).apply { description = context.getString(R.string.notif_channel_default_desc) }
        manager.createNotificationChannel(channel)
    }
}
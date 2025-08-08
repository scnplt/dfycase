package dev.sertan.android.dfycase.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "TAG_FCM_LOG"

internal class DfyMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: $message")
        message.notification?.let {
            DfyNotificationManager.sendNotification(
                context = this,
                title = it.title,
                content = it.body,
            )
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken: $token")
    }
}

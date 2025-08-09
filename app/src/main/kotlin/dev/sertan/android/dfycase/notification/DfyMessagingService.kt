package dev.sertan.android.dfycase.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.dfycase.domain.repository.UserRepository
import dev.sertan.android.dfycase.util.FCMTokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

// TAG for logging
private const val TAG = "TAG_FCM_LOG"

/**
 * Firebase Messaging Service for handling incoming messages and token updates.
 * This service is responsible for receiving FCM messages,
 * displaying notifications, and updating the FCM token in the user repository.
 */
@AndroidEntryPoint
internal class DfyMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var tokenManager: FCMTokenManager

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Called when a message is received.
     * Displays a notification with the message content if the token is available.
     *
     * @param message The received FCM message.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: $message")
        if (tokenManager.token == null) return
        message.notification?.let {
            DfyNotificationManager.sendNotification(
                context = this,
                title = it.title,
                content = it.body,
            )
        }
    }

    /**
     * Called when a new FCM token is generated.
     * Updates the token in the user repository.
     *
     * @param token The new FCM token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken: $token")
        job = scope.launch { userRepository.updateFCMToken(token) }
    }

    /**
     * Called when the service is destroyed.
     * Cancels the job to prevent memory leaks.
     */
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}

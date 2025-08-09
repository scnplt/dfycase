package dev.sertan.android.dfycase.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.dfycase.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TAG_FCM_LOG"

@AndroidEntryPoint
internal class DfyMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userRepository: UserRepository
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

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
        job = scope.launch { userRepository.updateFCMToken(token) }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}

package dev.sertan.android.dfycase.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Model class created for the client/user
 *
 * @property id the unique identifier of the user.
 * @property email the email address of the user.
 * @property fcmToken the Firebase Cloud Messaging token for the user.
 * @property createdAt the timestamp when the user was created.
 * @constructor Creates a user with the specified properties.
 */
internal data class User(
    val id: String = "",
    val email: String = "",
    val fcmToken: String = "",
    @ServerTimestamp
    val createdAt: Date = Date()
)

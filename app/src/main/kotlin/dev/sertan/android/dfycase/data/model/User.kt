package dev.sertan.android.dfycase.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

internal data class User(
    val id: String = "",
    val email: String = "",
    val fcmToken: String = "",
    @ServerTimestamp
    val createdAt: Date = Date()
)

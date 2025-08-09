package dev.sertan.android.dfycase.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val FCM_TOKEN_PREFS = "fcm_token_prefs"
private const val FCM_TOKEN_KEY = "fcm_token"

/**
 * A manager for storing and retrieving the FCM token in shared preferences.
 * This class provides methods to get, set, and delete the FCM token.
 */
@Singleton
internal class FCMTokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(FCM_TOKEN_PREFS, Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString(FCM_TOKEN_KEY, null)
        set(value) = prefs.edit { putString(FCM_TOKEN_KEY, value) }

    fun deleteToken() {
        prefs.edit { remove(FCM_TOKEN_KEY) }
    }
}

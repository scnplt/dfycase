package dev.sertan.android.dfycase.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Extension function to show a toast message with a string resource.
 *
 * @param stringRes The string resource to display in the toast.
 * @param duration The duration for which the toast should be shown. Default is Toast.LENGTH_SHORT.
 */
internal fun Context.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}

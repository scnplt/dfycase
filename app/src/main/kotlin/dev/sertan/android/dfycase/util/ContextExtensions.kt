package dev.sertan.android.dfycase.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

internal fun Context.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}

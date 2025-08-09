package dev.sertan.android.dfycase.data.model

import android.net.Uri

internal data class File(
    val name: String,
    val url: String? = null,
    val uri: Uri? = null
)
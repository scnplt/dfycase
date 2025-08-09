package dev.sertan.android.dfycase.data.model

import android.net.Uri

/**
 * Model class created for file upload operations
 *
 * @property name the name of file.
 * @property uri the URI of the file.
 * @constructor Creates a file.
 */
internal data class File(
    val name: String,
    val uri: Uri? = null
)

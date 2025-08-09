package dev.sertan.android.dfycase.domain.repository

import android.net.Uri
import dev.sertan.android.dfycase.data.model.File
import dev.sertan.android.dfycase.util.State

internal interface FileRepository {

    suspend fun getFiles(): State<List<File>>

    suspend fun uploadFile(file: File)

    suspend fun downloadFile(file: File, onDone: (Uri) -> Unit)
}
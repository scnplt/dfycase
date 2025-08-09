package dev.sertan.android.dfycase.domain.repository

import dev.sertan.android.dfycase.data.model.File
import dev.sertan.android.dfycase.util.State

/**
 * Repository interface for managing files.
 */
internal interface FileRepository {

    suspend fun getFiles(): State<List<File>>

    suspend fun uploadFile(file: File)
}
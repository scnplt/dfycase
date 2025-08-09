package dev.sertan.android.dfycase.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dev.sertan.android.dfycase.data.model.File
import dev.sertan.android.dfycase.domain.repository.FileRepository
import dev.sertan.android.dfycase.util.State
import kotlinx.coroutines.tasks.await

private const val USER_FILES = "user_files"

internal class FileRepositoryImpl(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : FileRepository {

    override suspend fun getFiles(): State<List<File>> = try {
        val uid = auth.currentUser?.uid ?: throw Exception("User not found")
        val files = storage.reference
            .child("$USER_FILES/$uid")
            .listAll().await()
            .items.mapNotNull { File(name = it.name) }
        State.Success(data = files)
    } catch (e: Exception) {
        State.Error(e.message.toString())
    }

    override suspend fun uploadFile(file: File) {
        auth.currentUser?.uid?.let { uid ->
            val ref = getStorageRef(uid, file.name)
            val uploadTask = ref.putFile(file.uri!!)
            uploadTask.await()
        }
    }

    override suspend fun downloadFile(file: File, onDone: (Uri) -> Unit) {
        auth.currentUser?.uid?.let { uid ->
            val ref = getStorageRef(uid, file.name)
            val downloadUrl = ref.downloadUrl.await()
            onDone(downloadUrl)
        }
    }

    private fun getStorageRef(id: String, fileName: String): StorageReference =
        storage.reference.child("$USER_FILES/$id/$fileName")
}
package dev.sertan.android.dfycase.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dev.sertan.android.dfycase.data.model.File
import dev.sertan.android.dfycase.domain.repository.FileRepository
import dev.sertan.android.dfycase.util.State
import kotlinx.coroutines.tasks.await

// USER_FILES is the directory in Firebase Storage where user files are stored.
// Each user has a subdirectory named after their UID, where their files are stored.
private const val USER_FILES = "user_files"

/**
 * FileRepositoryImpl is an implementation of the FileRepository interface.
 * It provides methods to interact with Firebase Storage for file operations.
 * It allows users to upload and list files associated with their account.
 *
 * @property auth FirebaseAuth instance for user authentication.
 * @property storage FirebaseStorage instance for file storage operations.
 * @constructor Creates an instance of FileRepositoryImpl.
 */
internal class FileRepositoryImpl(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : FileRepository {

    /**
     * Retrieves a list of files associated with the current user.
     *
     * @return State<List<File>> containing the list of files or an error message.
     */
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

    /**
     * Uploads a file to Firebase Storage under the current user's directory.
     *
     * @param file The File object containing the file to be uploaded.
     */
    override suspend fun uploadFile(file: File) {
        auth.currentUser?.uid?.let { uid ->
            val ref = getStorageRef(uid, file.name)
            val uploadTask = ref.putFile(file.uri!!)
            uploadTask.await()
        }
    }

    /**
     * Retrieves a StorageReference for a specific file under the user's directory.
     *
     * @param id The user's unique identifier (UID).
     * @param fileName The name of the file to be accessed.
     * @return StorageReference pointing to the specified file in Firebase Storage.
     */
    private fun getStorageRef(id: String, fileName: String): StorageReference =
        storage.reference.child("$USER_FILES/$id/$fileName")
}
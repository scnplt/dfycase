package dev.sertan.android.dfycase.ui.screen.home

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.dfycase.data.model.File
import dev.sertan.android.dfycase.domain.repository.FileRepository
import dev.sertan.android.dfycase.domain.repository.UserRepository
import dev.sertan.android.dfycase.util.FCMTokenManager
import dev.sertan.android.dfycase.util.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen, managing file uploads and user sessions.
 * Handles loading files, uploading new files, and user logout.
 *
 * @property application The application context for accessing resources.
 * @property fileRepository Repository for file operations.
 * @property userRepository Repository for user operations.
 * @property fcmTokenManager Manager for Firebase Cloud Messaging tokens.
 * @constructor Creates a HomeViewModel instance.
 */
@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val application: Application,
    private val fileRepository: FileRepository,
    private val userRepository: UserRepository,
    private val fcmTokenManager: FCMTokenManager
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeState(isLoading = true))
    val uiState = _uiState
        .onStart { loadFiles() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeState(isLoading = true)
        )

    fun getFCMToken(): String? = fcmTokenManager.token

    fun closeTopMenu() {
        _uiState.update { it.copy(isTopMenuExpanded = false) }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _uiState.update { it.copy(isUserLoggedOut = true) }
        }
    }

    /**
     * Loads files from the repository and updates the UI state.
     * This function fetches files asynchronously and updates the UI state with the result.
     */
    fun loadFiles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = fileRepository.getFiles()
            when (result) {
                is State.Success -> {
                    _uiState.update { it.copy(files = result.data, isLoading = false) }
                }

                is State.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false) }
                }

                else -> {}
            }
        }
    }

    /**
     * Uploads files to the repository.
     * This function takes a list of URIs, retrieves their file names,
     * and uploads them using the file repository.
     *
     * @param uris List of URIs representing the files to be uploaded.
     */
    fun uploadFile(uris: List<Uri>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            uris.forEach { uri ->
                val fileName = getFileNameFromUri(uri)
                fileRepository.uploadFile(File(name = fileName, uri = uri))
            }
            loadFiles()
        }
    }

    /**
     * Retrieves the file name from a URI.
     * This function checks if the URI scheme is "content" and queries the content resolver
     * to get the display name of the file. If the scheme is not "content",
     * it returns the last path segment or a default name.
     *
     * @param uri The URI from which to extract the file name.
     * @return The file name as a String.
     */
    private fun getFileNameFromUri(uri: Uri): String {
        if (uri.scheme != "content") return uri.lastPathSegment ?: "temp"

        application.contentResolver
            .query(uri, null, null, null, null)
            .use {
                if (it != null && it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        return it.getString(nameIndex)
                    }
                }
            }

        return uri.lastPathSegment.orEmpty()
    }
}

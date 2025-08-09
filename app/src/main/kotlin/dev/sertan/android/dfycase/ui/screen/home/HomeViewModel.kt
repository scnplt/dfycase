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

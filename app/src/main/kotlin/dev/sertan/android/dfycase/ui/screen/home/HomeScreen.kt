package dev.sertan.android.dfycase.ui.screen.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sertan.android.dfycase.R
import dev.sertan.android.dfycase.data.model.File
import dev.sertan.android.dfycase.ui.theme.DFYCaseTheme
import dev.sertan.android.dfycase.util.showToast
import kotlinx.serialization.Serializable

/**
 * Represents the Home route in the application.
 */
@Serializable
internal object HomeRoute

/**
 * Composable function that displays the Home screen.
 * This screen allows users to upload files, copy the FCM token,
 * and log out of the application.
 *
 * @param navigateToLogin A lambda function to navigate to the login screen when the user is logged out.
 * It is called when the user logs out or when the FCM token is not found.
 */
@Composable
internal fun HomeScreen(navigateToLogin: () -> Unit) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = viewModel::uploadFile
    )

    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(uiState.isUserLoggedOut) {
        if (uiState.isUserLoggedOut) navigateToLogin()
    }

    HomeScreen(
        uiState = uiState,
        onUploadFileClicked = { launcher.launch("*/*") },
        onCopyFCMTokenClicked = {
            viewModel.getFCMToken()?.let {
                clipboardManager.setText(AnnotatedString(it))
                viewModel.closeTopMenu()
                context.showToast(stringRes = R.string.fcm_token_copied)
            } ?: context.showToast(stringRes = R.string.fcm_token_not_found)
        },
        onLogoutClicked = viewModel::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeState,
    onUploadFileClicked: () -> Unit,
    onCopyFCMTokenClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.uploaded_files)) },
                actions = {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.copy_fcm_token)) },
                            onClick = onCopyFCMTokenClicked
                        )

                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.logout)) },
                            onClick = onLogoutClicked
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onUploadFileClicked) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
                return@Box
            }

            if (uiState.error != null) {
                Text(text = uiState.error)
                return@Box
            }

            if (uiState.files.isEmpty()) {
                Text(text = stringResource(R.string.no_files_found))
                return@Box
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.files) { file ->
                    ListItem(
                        headlineContent = { Text(text = file.name) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewHomeScreen() {
    DFYCaseTheme {
        HomeScreen(
            uiState = HomeState(
                files = listOf(
                    File(name = "file1.txt"),
                    File(name = "file2.pdf"),
                    File(name = "file3.jpg")
                )
            ),
            onUploadFileClicked = {},
            onCopyFCMTokenClicked = {},
            onLogoutClicked = {}
        )
    }
}

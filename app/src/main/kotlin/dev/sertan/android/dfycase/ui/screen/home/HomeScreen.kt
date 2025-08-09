package dev.sertan.android.dfycase.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sertan.android.dfycase.ui.theme.DFYCaseTheme
import kotlinx.serialization.Serializable

@Serializable
internal object HomeRoute

@Composable
internal fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(uiState = uiState)
}

@Composable
private fun HomeScreen(uiState: HomeState) {
    Scaffold { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(text = uiState.message)
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewHomeScreen() {
    DFYCaseTheme {
        HomeScreen(uiState = HomeState())
    }
}
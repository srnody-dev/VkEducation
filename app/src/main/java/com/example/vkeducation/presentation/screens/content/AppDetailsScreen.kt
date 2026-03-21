package com.example.vkeducation.presentation.screens.content


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    appId: Int,
    viewModel: AppDetailViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val app = state.app

    var descriptionCollapsed by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is AppDetailEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(context.getString(event.resId))
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        if (app == null) {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                CircularProgressIndicator()
            }
        } else {

            Column(
                modifier = modifier
                    .fillMaxSize()

            ) {
                Toolbar(
                    onBackClick = onBackClick,
                    onShareClick = viewModel::onShareClick,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                AppDetailsHeader(
                    app = app,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(16.dp))
                InstallButton(
                    onClick = viewModel::onInstallClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
                ScreenshotsList(
                    screenshotUrlList = app.screenshotUrlList,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                )
                Spacer(Modifier.height(12.dp))
                AppDescription(
                    description = app.description,
                    collapsed = descriptionCollapsed,
                    onReadMoreClick = {
                        descriptionCollapsed = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Spacer(Modifier.height(12.dp))
                Developer(
                    name = app.developer,
                    onClick = viewModel::onDeveloperClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                )
            }
        }
    }
}

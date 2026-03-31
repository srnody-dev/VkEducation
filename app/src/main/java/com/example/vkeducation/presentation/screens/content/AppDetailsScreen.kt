package com.example.vkeducation.presentation.screens.content


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.vkeducation.presentation.screens.content.AppDetailCommand.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: AppDetailViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val currentContext by rememberUpdatedState(context)

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is AppDetailCommand.ShowSnackBar -> {
                    try {
                        val message = currentContext.getString(event.resId)
                        snackBarHostState.showSnackbar(message)
                    } catch (e: Exception) {
                        snackBarHostState.showSnackbar("Unknown error $e")
                    }
                }

                AppDetailCommand.Back -> {
                    onBackClick()
                }

                else -> {}
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) {
        when (val currentState = state) {
            AppDetailState.Finished -> {
                LaunchedEffect(Unit) { onBackClick() }
            }

            AppDetailState.Initial -> {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    CircularProgressIndicator()
                }
            }

            is AppDetailState.Details -> {
                val app = currentState.appDetails
                if (app != null) {
                    Column(
                        modifier = modifier.fillMaxSize()

                    ) {
                        TopAppBar(title = {}, navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }, actions = {
                            IconButton(onClick = {
                                viewModel.processCommand(ToggleWishlist(app.id))
                            }) {
                                Icon(
                                    imageVector = if (app.isInWishlist) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                                    contentDescription = if (app.isInWishlist) "Remove from wishlist" else "Add to wishlist",
                                    tint = if (app.isInWishlist) Color.Red else Color.Gray
                                )
                            }
                            IconButton(onClick = { viewModel.processCommand(AppDetailCommand.Share) }) {
                                Icon(
                                    imageVector = Icons.Outlined.Share, contentDescription = "Share"
                                )
                            }
                        })
                        Spacer(Modifier.height(8.dp))
                        AppDetailsHeader(
                            appDetails = app,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                        InstallButton(
                            onClick = { viewModel.processCommand(AppDetailCommand.Install) },
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
                            collapsed = currentState.descriptionCollapsed,
                            onReadMoreClick = {
                                viewModel.processCommand(AppDetailCommand.ToggleDescription)
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
                            onClick = { viewModel.processCommand(AppDetailCommand.DeveloperClick) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                        )
                    }
                }
            }

            is AppDetailState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Warning", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = currentState.message,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = onBackClick) {
                            Text("Back")
                        }
                    }
                }
            }
        }
    }
}
package com.example.vkeducation.presentation.screens.apps

import android.annotation.SuppressLint
import com.example.vkeducation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.vkeducation.domain.entity.AppShort
import coil3.compose.AsyncImage
import com.example.vkeducation.presentation.utils.toCategoryText
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppsScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    onAppClick: (AppShort) -> Unit,
    viewModel: AppsViewModel = hiltViewModel()
) {


    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val currentContext by rememberUpdatedState(context)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.processCommand(AppsCommand.RefreshApps) }
    )


    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is AppsCommand.ShowSnackBar -> {
                    val message = currentContext.getString(event.message)
                    snackBarHostState.showSnackbar(message)
                }

                AppsCommand.RefreshApps -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            AppTopBar(
                onClickToMenu = onNavigateToMenu,
                onLogoClick = viewModel::onLogoClick
            )

            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .pullRefresh(pullRefreshState)
                        .fillMaxSize()
                ) {
                    when (val currentState = state) {
                        is AppsState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (state as AppsState.Error).message,
                                    color = Color.Red,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        AppsState.Initial -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            ) {
                                items(emptyList<AppShort>()) {}
                            }

                            PullRefreshIndicator(
                                refreshing = isRefreshing,
                                state = pullRefreshState,
                                modifier = Modifier.align(Alignment.TopCenter)
                            )
                        }

                        is AppsState.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            ) {
                                items(currentState.appShorts) { app ->
                                    AppCard(
                                        appShort = app,
                                        onAppClick = onAppClick
                                    )
                                }
                            }

                            PullRefreshIndicator(
                                refreshing = isRefreshing,
                                state = pullRefreshState,
                                modifier = Modifier.align(Alignment.TopCenter)
                            )
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    modifier: Modifier = Modifier,
    onClickToMenu: () -> Unit,
    onLogoClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLogoClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rustorelogo),
                    tint = Color.Unspecified,
                    contentDescription = "RuStore Logo",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )

                Text(
                    text = "RuStore",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        actions = {
            Icon(
                tint = Color.White,
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClickToMenu)
                    .padding(8.dp)
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = Color.Blue,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}


@Composable
private fun AppCard(
    modifier: Modifier = Modifier,
    appShort: AppShort,
    onAppClick: (AppShort) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = { onAppClick(appShort) })
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp),

        ) {

        AsyncImage(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = appShort.iconUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(
                fontSize = 18.sp,
                text = appShort.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = appShort.description
            )
            Text(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = appShort.category.toCategoryText()
            )

        }
    }


}
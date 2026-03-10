package com.example.vkeducation.presentation.screens.apps

import com.example.vkeducation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vkeducation.data.MockApps
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.presentation.AppIconMapper

import coil3.compose.AsyncImage

@Composable
fun AppsScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    onAppClick: (App) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        AppTopBar(
            onClickToMenu = onNavigateToMenu,
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(MockApps.apps) { app ->
                    AppCard(
                        app = app,
                        onAppClick = onAppClick
                    )
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
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = "RuStore",
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
    app: App,
    onAppClick: (App) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = { onAppClick(app) })
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp),

        ) {

        val iconResId = remember(app.iconUrl) {
            AppIconMapper.mapToIconResId(app.iconUrl)
        }

        AsyncImage(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = iconResId,
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
                text = app.name,
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
                text = app.description
            )
            Text(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = app.category.toString().lowercase().replaceFirstChar { it.uppercase() }
            )

        }
    }

}
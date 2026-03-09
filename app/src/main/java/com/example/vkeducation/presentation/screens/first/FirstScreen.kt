package com.example.vkeducation.presentation.screens.first

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vkeducation.R


@Composable
fun FirstScreen(
    modifier: Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onOpenClick: () -> Unit,
    onCallClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Title(text = "First Activity")

        SearchBar(
            query = text,
            modifier = modifier,
            onQueryChange = onTextChange
        )
        ViewCard(
            modifier = modifier ,
            onClickCallButton = onCallClick,
            onClickOpenButton = onOpenClick,
            onClickShareButton = onShareClick

        )

    }

}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        value = query, onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search___),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ViewCard(
    modifier: Modifier = Modifier,
    onClickCallButton: () -> Unit,
    onClickOpenButton: () -> Unit,
    onClickShareButton: () -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onClickOpenButton,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open second Activity")
        }

        Button(
            onClick = onClickCallButton,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Call a friend")
        }

        Button(
            onClick = onClickShareButton,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share text")
        }
    }

}
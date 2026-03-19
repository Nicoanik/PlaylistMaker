package com.example.playlistmaker.ui.screens.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.ui.components.PlaylistItem
import com.example.playlistmaker.ui.theme.Typography

@Composable
fun PlaylistsScreen(
    playlists: List<Playlist>,
    onNewPlaylistClick: () -> Unit,
    onPlaylistClick: (Playlist) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .height(36.dp),
            shape = RoundedCornerShape(54.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
            onClick = onNewPlaylistClick
        ) {
            Text(
                text = stringResource(R.string.new_playlist),
                style = Typography.ysMedium14,
                color = MaterialTheme.colorScheme.background
            )
        }
        if (playlists.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 12.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(playlists) { playlist ->
                    PlaylistItem(playlist) { onPlaylistClick(playlist) }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 46.dp),
                    painter = painterResource(R.drawable.ic_empty_120),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp),
                    text = stringResource(R.string.empty_playlists),
                    style = Typography.ysMedium19,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
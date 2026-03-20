package com.example.playlistmaker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.theme.Typography
import com.example.playlistmaker.utils.antiRepetitionClick

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onPlaylistClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 16.dp)
            .antiRepetitionClick(
                coroutineScope = rememberCoroutineScope(),
                onClick = onPlaylistClick
            )
    ) {
        AsyncImage(
            model = playlist.coverPath,
            contentDescription = playlist.title,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(R.drawable.album_cover_placeholder),
            error = painterResource(R.drawable.album_cover_placeholder),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = playlist.title,
            style = Typography.ysRegular12,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = pluralStringResource(
                id = R.plurals.playlist_size,
                count = playlist.playlistSize,
                formatArgs = arrayOf(playlist.playlistSize)
            ),
            style = Typography.ysRegular12,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    PlaylistItem(
        Playlist(
            id = 1,
            title = "Заголовок",
            description = "Описание",
            coverPath = "Ссылка на картинку",
            trackIds = emptyList(),
            playlistSize = 15
        ),
        onPlaylistClick = {  }
    )
}
package com.example.playlistmaker.ui.screens.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.components.TrackItem
import com.example.playlistmaker.ui.screens.search.dismissKeyboardOnScroll
import com.example.playlistmaker.ui.theme.Typography
import java.util.UUID

@Composable
fun FavoriteScreen(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (tracks.isNotEmpty()) {
            LazyColumn(
                Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .dismissKeyboardOnScroll()
            ) {
                items(tracks, key = { it.trackId ?: UUID.randomUUID() }) { track ->
                    TrackItem(track) { onTrackClick(track) }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 102.dp),
                    painter = painterResource(R.drawable.ic_empty_120),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp),
                    text = stringResource(R.string.empty_media_library),
                    style = Typography.ysMedium19,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
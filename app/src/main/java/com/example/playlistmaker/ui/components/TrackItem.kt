package com.example.playlistmaker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.timeConversion
import com.example.playlistmaker.ui.theme.Typography
import com.example.playlistmaker.utils.antiRepetitionClick

@Composable
fun TrackItem(
    track: Track,
    onTrackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(horizontal = 12.dp)
            .antiRepetitionClick(
                onClick = onTrackClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.artworkUrl100,
            contentDescription = track.trackName,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
            placeholder = painterResource(R.drawable.album_cover_placeholder),
            error = painterResource(R.drawable.album_cover_placeholder),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            track.trackName?.let {
                Text(
                    text = it,
                    style = Typography.ysRegular16,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                track.artistName?.let {
                    Text(
                        modifier = Modifier.weight(1f, fill = false),
                        text = it,
                        style = Typography.ysRegular11,
                        color = MaterialTheme.colorScheme.onSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_dot_dash_13),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                track.trackTime?.let {
                    Text(
                        text = timeConversion(it),
                        style = Typography.ysRegular11,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_forward_arrow_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    TrackItem(
        Track(
            trackId = null,
            trackName = "Master Of Puppets",
            artistName = "Metallica",
            trackTime = 52698,
            collectionName = null,
            releaseDate = null,
            primaryGenreName = null,
            country = null,
            artworkUrl100 = null,
            previewUrl = null
        ),
        onTrackClick = {  }
    )
}
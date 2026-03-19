package com.example.playlistmaker.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.domain.media.models.timeConversion
import com.example.playlistmaker.ui.theme.Typography

@Composable
fun TrackItem(
    track: Track,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(horizontal = 12.dp)
            .clickable { onClick() },
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
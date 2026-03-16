package com.example.playlistmaker.search.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.media.domain.models.timeConversion
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.theme.Typography
import com.example.playlistmaker.settings.ui.theme.YP_black
import com.example.playlistmaker.settings.ui.theme.YP_blue
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onTrackClick: (Track) -> Unit,
    onClearHistory: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Log.d("Nico", "SearchScreen recomposition. History size: ${state.history.size}")
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Text(
                        text = stringResource(R.string.search),
                        style = Typography.ysMedium22
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchTextField(state.searchText) { viewModel.searchDebounce(it) }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 140.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(44.dp),
                    color = YP_blue
                )
            } else {

                if (state.history.isNotEmpty() && state.searchText.isEmpty()) {
                    SearchHistory(state.history, onTrackClick, onClearHistory)
                }

                if (state.content.isNotEmpty()) {
                    FoundTracks(state.content, onTrackClick)
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ),
        value = text,
        onValueChange = onTextChange,
        textStyle = Typography.ysRegular16.copy(
            color = YP_black,
            textAlign = TextAlign.Start,
            lineHeight = 20.sp
        ),
        singleLine = true,
        cursorBrush = SolidColor(YP_blue),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp),
                    painter = painterResource(R.drawable.ic_search_16),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = Typography.ysRegular16,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier
                            .clickable(onClick = { onTextChange("") })
                            .padding(horizontal = 12.dp),
                        painter = painterResource(R.drawable.ic_x_16),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    )
}

@Composable
private fun SearchHistory(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit,
    onClearHistory: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 42.dp, bottom = 20.dp),
            text = stringResource(R.string.search_history),
            style = Typography.ysMedium19
        )

        LazyColumn(Modifier.fillMaxWidth()) {
            items(tracks, key = { it.trackId?.toString() ?: "temp_${tracks.indexOf(it)}" }) { track ->
                TrackItem(track) { onTrackClick(track) }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier
                            .height(36.dp),
                        shape = RoundedCornerShape(54.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
                        onClick = { onClearHistory() }
                    ) {
                        Text(
                            text = stringResource(R.string.clear_button_search_history),
                            style = Typography.ysMedium14,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FoundTracks(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(tracks, key = { it.trackId ?: UUID.randomUUID() }) { track ->
                TrackItem(track) { onTrackClick(track) }
            }
        }
    }
}

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
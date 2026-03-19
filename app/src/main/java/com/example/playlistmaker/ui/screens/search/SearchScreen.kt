package com.example.playlistmaker.ui.screens.search

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.ui.components.TrackItem
import com.example.playlistmaker.ui.view_models.search.SearchViewModel
import com.example.playlistmaker.ui.theme.Typography
import com.example.playlistmaker.ui.theme.ypBlack
import com.example.playlistmaker.ui.theme.ypBlue
import com.example.playlistmaker.utils.antiRepetition
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToPlayer: (Track) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val onTrackClick: (Track) -> Unit = remember {
        antiRepetition(
            SearchViewModel.CLICK_DEBOUNCE_DELAY,
            scope
        ) { track ->
            viewModel.addTrackToSearchHistory(track)
            onNavigateToPlayer(track)
        }
    }

    DisposableEffect(viewModel) {
        viewModel.registerNetworkReceiver(context)

        onDispose {
            viewModel.unregisterNetworkReceiver(context)
        }
    }

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
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            SearchTextField(
                state.searchText,
                { viewModel.onSearchTextChanged(it) },
                { viewModel.searchRequest(it) }
            )

            if (!state.isConnected) ShowToast(
                context,
                stringResource(R.string.no_internet)
            ) { viewModel.toastShown() }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 140.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(44.dp),
                    color = ypBlue
                )
            } else {
                when {
                    state.history.isNotEmpty() && state.searchText.isEmpty() ->
                        SearchHistory(
                            state.history,
                            onTrackClick
                        ) { viewModel.clearSearchHistory() }

                    state.content.isNotEmpty() -> FoundTracks(state.content, onTrackClick)
                    state.error -> {
                        ShowError { viewModel.searchRequest(state.searchText) }
                        ShowToast(context, state.errorMessage) { viewModel.toastShown() }
                    }

                    state.empty -> ShowEmpty()
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onEnterPressed: (String) -> Unit
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
            color = ypBlack,
            textAlign = TextAlign.Start,
            lineHeight = 20.sp
        ),
        singleLine = true,
        cursorBrush = SolidColor(ypBlue),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onEnterPressed(text) }
        ),
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

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .dismissKeyboardOnScroll()
        ) {
            items(
                tracks,
                key = { it.trackId?.toString() ?: "temp_${tracks.indexOf(it)}" }) { track ->
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
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .dismissKeyboardOnScroll()
        ) {
            items(tracks, key = { it.trackId ?: UUID.randomUUID() }) { track ->
                TrackItem(track) { onTrackClick(track) }
            }
        }
    }
}

@Composable
private fun ShowError(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(top = 102.dp),
            painter = painterResource(R.drawable.ic_no_internet_120),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.something_went_wrong),
            style = Typography.ysMedium19,
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier
                .padding(top = 24.dp)
                .height(36.dp),
            shape = RoundedCornerShape(54.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
            onClick = { onClick() }
        ) {
            Text(
                text = stringResource(R.string.button_refresh),
                style = Typography.ysMedium14,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun ShowEmpty() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
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
            text = stringResource(R.string.nothing_found),
            style = Typography.ysMedium19,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowToast(
    context: Context,
    message: String,
    toastShown: () -> Unit
) {
    if (message.isNotEmpty()) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        toastShown()
    }
}

@Composable
fun Modifier.dismissKeyboardOnScroll(): Modifier {
    val focusManager = LocalFocusManager.current
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                focusManager.clearFocus()
                return Offset.Zero
            }
        }
    }
    return this.nestedScroll(nestedScrollConnection)
}
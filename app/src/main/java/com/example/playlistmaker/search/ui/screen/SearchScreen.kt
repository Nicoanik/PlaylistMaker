package com.example.playlistmaker.search.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.theme.Typography
import com.example.playlistmaker.settings.ui.theme.YP_black
import com.example.playlistmaker.settings.ui.theme.YP_blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onTrackClick: (Track) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
            SearchTextField()
        }
    }
}

@Composable
private fun SearchTextField(
//    text: String,
//    onTextChange: (String) -> Unit,
//    onClickClear: () -> Unit
) {
    var text by remember { mutableStateOf("") }

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
        onValueChange = { newText -> text = newText },
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
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = Typography.ysRegular16,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    innerTextField()
                }
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        painter = painterResource(R.drawable.ic_x_16),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}
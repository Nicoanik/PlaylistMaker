package com.example.playlistmaker.media.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(

) {
    // CoroutineScope нам нужен для анимации при переключении табов
    val scope = rememberCoroutineScope()

    // Ключевое состояние Pager, при инициализации которого указывается количество экранов, которые можно переключать
    val pagerState = rememberPagerState(pageCount = { 2 })

    // Индекс текущего выбранного таба
    val selectedTabIndex = remember { pagerState.currentPage }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Text(
                        text = stringResource(R.string.media),
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
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.favorite_tracks),
                            style = Typography.ysMedium14
                        )
                    },
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.playlists),
                            style = Typography.ysMedium14
                        )
                    },
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> FirstScreen()
                    1 -> SecondScreen()
                }
            }
        }
    }
}


@Composable
fun FirstScreen() {
    // Контент первого экрана
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // для визуализации
    ) {
        Text(
            text = "Первый экран!",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun SecondScreen() {
    // Контент второго экрана
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // для визуализации
    ) {
        Text(
            text = "Второй экран!",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}
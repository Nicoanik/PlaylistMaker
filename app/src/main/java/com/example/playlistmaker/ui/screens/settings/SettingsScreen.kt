package com.example.playlistmaker.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.theme.Typography
import com.example.playlistmaker.ui.view_models.settings.SettingsViewModel
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val isChecked by viewModel.isChecked.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = Typography.ysMedium22
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Spacer(Modifier.height(24.dp))
            SettingSwitch(isChecked) {
                viewModel.changeThemeMode(!isChecked)
            }
            SettingItem(R.string.share_the_app, R.drawable.ic_share_24) {
                viewModel.shareApp()
            }
            SettingItem(R.string.write_to_support, R.drawable.ic_support_24) {
                viewModel.openSupport()
            }
            SettingItem(R.string.user_agreement, R.drawable.ic_forward_arrow_24) {
                viewModel.openTerms()
            }
        }
    }
}

@Composable
private fun SettingSwitch(
    isChecked: Boolean,
    onClick: () -> Unit
) {
    var isClicked by remember { mutableStateOf(isChecked) }

    LaunchedEffect(isClicked != isChecked) {
        if (isClicked != isChecked) {
            delay(300L)
            onClick()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable { isClicked = !isClicked },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = stringResource(R.string.dark_theme),
            style = Typography.ysRegular16
        )
        CustomSwitch(isClicked = isClicked, 32.dp, 12.dp, 18.dp)
    }
}

@Composable
private fun SettingItem(
    stringRes: Int,
    icRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = stringResource(stringRes),
            style = Typography.ysRegular16
        )
        Icon(
            modifier = Modifier.padding(end = 12.dp),
            painter = painterResource(icRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun CustomSwitch(
    isClicked: Boolean,
    trackWidth: Dp,
    trackHeight: Dp,
    thumbSize: Dp
) {
    val boxWidth = if (thumbSize > trackHeight) (thumbSize - trackHeight + trackWidth) else trackWidth
    val boxHeight = if (thumbSize > trackHeight) thumbSize else trackHeight
    val offset by animateDpAsState(
        targetValue = if (isClicked) boxWidth - thumbSize else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .size(
                width = boxWidth + 15.dp,
                height = boxHeight
            )
            .padding(end = 15.dp)
    ) {
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .align(Alignment.Center)
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(trackHeight / 2)
                )
        )
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = offset)
                .background(MaterialTheme.colorScheme.onTertiary, shape = CircleShape)
                .align(Alignment.CenterStart)
        )
    }
}
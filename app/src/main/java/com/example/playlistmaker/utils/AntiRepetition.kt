package com.example.playlistmaker.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> antiRepetition(
    coroutineScope: CoroutineScope,
    lockTimeMillis: Long = LOCK_TIME,
    onClick: (T) -> Unit
): (T) -> Unit {
    var isLocked = false

    return { param: T ->
        if (!isLocked) {
            onClick(param)
            isLocked = true
            coroutineScope.launch {
                delay(lockTimeMillis)
                isLocked = false
            }
        }
    }
}

fun Modifier.antiRepetitionClick(
    lockTimeMillis: Long = LOCK_TIME,
    onClick: () -> Unit
): Modifier = antiRepetitionClick(lockTimeMillis, Unit) { _ ->
    onClick()
}

fun <T> Modifier.antiRepetitionClick(
    lockTimeMillis: Long = LOCK_TIME,
    param: T,
    onClick: (T) -> Unit
): Modifier = composed {
    val scope = rememberCoroutineScope()
    var isLocked by remember { mutableStateOf(false) }

    fun startDelay() {
        scope.launch {
            delay(lockTimeMillis)
            isLocked = false
        }
    }

    this.then(
        Modifier.pointerInput(Unit) {
            detectTapGestures {
                if (!isLocked) {
                    onClick(param)
                    isLocked = true
                    startDelay()
                }
            }
        }
    )
}

private const val LOCK_TIME = 300L
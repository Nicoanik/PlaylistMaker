package com.example.playlistmaker.utils

import android.util.Log
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null

    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}

fun Modifier.debounceClick(
    delayMillis: Long = DEBOUNCE_TIME,
    useLastParam: Boolean = true,
    onDebouncedClick: () -> Unit
): Modifier = debounceClick(delayMillis, useLastParam, Unit) { _ ->
    onDebouncedClick()
}

fun <T> Modifier.debounceClick(
    delayMillis: Long = DEBOUNCE_TIME,
    useLastParam: Boolean = true,
    param: T,
    onDebouncedClick: (T) -> Unit
): Modifier = composed {
    val scope = rememberCoroutineScope()
    var debounceJob: Job? by remember { mutableStateOf(null) }

    fun startDelay() {
        debounceJob = scope.launch {
            delay(delayMillis)
            onDebouncedClick(param)
        }
    }

    this.then(
        Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitFirstDown(requireUnconsumed = false)

                    if (useLastParam) debounceJob?.cancel()

                    if (shouldSkipClick(debounceJob, useLastParam)) continue
                    startDelay()
                }
            }
        }
    )
}

private fun shouldSkipClick(
    job: Job?,
    useLastParam: Boolean
): Boolean = job?.isCompleted != true && !useLastParam

private const val DEBOUNCE_TIME = 2000L
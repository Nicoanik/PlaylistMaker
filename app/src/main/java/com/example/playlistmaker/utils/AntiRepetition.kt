package com.example.playlistmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> antiRepetition(
    lockTimeMillis: Long,
    coroutineScope: CoroutineScope,
    action: (T) -> Unit
): (T) -> Unit {
    var isLocked = false

    return { param: T ->
        if (!isLocked) {
            action(param)
            isLocked = true
            coroutineScope.launch {
                delay(lockTimeMillis)
                isLocked = false
            }
        }
    }
}
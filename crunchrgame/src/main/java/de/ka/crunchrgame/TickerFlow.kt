package de.ka.crunchrgame

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

fun CoroutineScope.ticker(
    tickMs: Long = 100,
    startDurationMs: Long = 0,
    maxDurationMs: Long,
    tickUpdate: (timeElapsedMs: Long, timeLeftMs: Long) -> Unit,
    end: () -> Unit
) = this.async {
    var currentDuration = startDurationMs
    while (isActive && currentDuration < maxDurationMs) {
        tickUpdate(currentDuration, maxDurationMs - currentDuration)
        currentDuration += tickMs
        delay(tickMs)
    }
    if (isActive) {
        tickUpdate(currentDuration, maxDurationMs - currentDuration)
        end()
    }
}

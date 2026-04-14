package de.ka.crunchrgame.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Starts a time ticker for the given [maxDurationMs]. It ticks with [tickMs].
 *
 * You can specify an [already elapsed time interval][startDurationMs].
 *
 * To be notified about ticks, use [tickUpdate].
 * If reached, the final update wil be [end].
 */
fun CoroutineScope.launchTicker(
    tickMs: Long = 100,
    startDurationMs: Long = 0,
    maxDurationMs: Long,
    tickUpdate: (timeElapsedMs: Long, timeLeftMs: Long) -> Unit,
    end: () -> Unit
) = this.launch {
    var currentDuration = startDurationMs
    tickUpdate(currentDuration, maxDurationMs - currentDuration)
    while (isActive && currentDuration < maxDurationMs) {
        delay(tickMs)
        currentDuration += tickMs
        tickUpdate(currentDuration, maxDurationMs - currentDuration)
    }
    if (isActive) {
        tickUpdate(currentDuration, 0)
        end()
    }
}

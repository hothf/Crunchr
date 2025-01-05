package de.ka.crunchr.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun TimeLeftHost(
    timerHostState: TimerHostState,
    content: @Composable (time: Int) -> Unit = {}
) {

    val scope = rememberCoroutineScope()

    var time by remember { mutableIntStateOf(timerHostState.current.timeLeftMs / 1000) }

    LaunchedEffect(timerHostState.current) {
        scope.coroutineContext.cancelChildren()

        if (timerHostState.current.started) {
            scope.launch {
                val timeLeft = timerHostState.current.timeLeftMs
                var i = 0

                if (scope.isActive) {
                    repeat((timeLeft / 1000) + 1) {
                        if (scope.isActive) {
                            time = timeLeft - i * 1000
                            delay(1000)
                            i++
                        }
                    }
                }
            }
        }

    }
    content(time)
}

@Composable
@Preview
private fun PreviewTimerScore() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val timerHostState = TimerHostState()

        LaunchedEffect(Unit) {
            scope.launch {
                timerHostState.handle(false, 10_000, 0.5f)
                delay(3000)
                timerHostState.handle(false, 15_000, 0.75f)
                delay(3000)
                timerHostState.handle(false, 0, 1f)
            }
        }

        TimeLeftHost(timerHostState = timerHostState) { time ->
            Text("Time: $time s")
        }
    }
}
package de.ka.crunchr.ui.composables

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.ka.crunchr.ui.composables.utils.UiDefaults
import kotlinx.coroutines.delay

data class TimeProgress(
    val timeLeftMs: Int = 30_000,
    val progress: Float = 1.0f,
    val started: Boolean = false,
    val atTime: Long = System.currentTimeMillis()
)

class TimerHostState {
    var current by mutableStateOf(TimeProgress())
        private set

    suspend fun handle(stop: Boolean, timeLeftMs: Int, progress: Float) {
        if (stop) {
            current = TimeProgress(timeLeftMs = 0, progress = progress, started = false)
            delay(100)
        } else {
            current = TimeProgress(timeLeftMs = timeLeftMs, progress = progress, started = false)
            delay(100)
            current = TimeProgress(timeLeftMs = timeLeftMs, progress = 0.0f, started = true)
        }
    }
}

@Composable
fun TimerProgressHost(
    modifier: Modifier = Modifier,
    hostState: TimerHostState,
    color: Color = MaterialTheme.colorScheme.onSecondary,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    animateCloseCall: Boolean = false,
    isCircular: Boolean = false,
    strokeWidth: Dp = 4.dp
) {
    val gameProgress = remember { Animatable(hostState.current.progress) }
    val current = hostState.current

    LaunchedEffect(current) {
        if (current.started) {
            gameProgress.animateTo(
                targetValue = current.progress,
                animationSpec = tween(durationMillis = current.timeLeftMs, easing = LinearEasing)
            )
        } else {
            gameProgress.animateTo(
                targetValue = current.progress,
                animationSpec = tween(durationMillis = 0, easing = LinearEasing)
            )
            gameProgress.stop()
        }
    }

    TimerProgress(
        modifier = modifier,
        progress = gameProgress.value,
        isCircular = isCircular,
        color = color,
        animateCloseCall = animateCloseCall,
        backgroundColor = backgroundColor,
        strokeWidth = strokeWidth
    )
}

@Composable
fun TimerProgress(
    modifier: Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    backgroundColor: Color,
    color: Color,
    isCircular: Boolean,
    animateCloseCall: Boolean,
    strokeWidth: Dp
) {
    val coercedProgress = progress.coerceIn(0f, 1f)
    val expectedAlpha = if (animateCloseCall && coercedProgress < 0.25f) 0.25f else 1f
    val infiniteTransition = rememberInfiniteTransition(label = "alpha")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = expectedAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(UiDefaults.LONG_MS, easing = LinearEasing)
        ), label = "alpha"
    )
    Canvas(modifier.alpha(alpha)) {
        if (isCircular) {
            drawRoundIndicator(coercedProgress, color, strokeWidth)
        } else {
            drawLinearIndicatorBackground(backgroundColor)
            drawLinearIndicator(
                coercedProgress,
                color
            )
        }
    }
}

private fun DrawScope.drawLinearIndicatorBackground(color: Color) = drawLinearIndicator(1f, color)

private fun DrawScope.drawRoundIndicator(
    endFraction: Float,
    color: Color,
    strokeWidth: Dp,
) {
    drawArc(
        color = color,
        startAngle = -90f,
        sweepAngle = -endFraction * 360f,
        false,
        style = Stroke(width = strokeWidth.toPx())
    )
}

private fun DrawScope.drawLinearIndicator(
    endFraction: Float,
    color: Color
) {
    val width = size.width
    val yOffset = size.height / 2

    drawLine(
        color,
        Offset(0f, yOffset),
        Offset(width * endFraction, yOffset),
        size.height
    )
}

@Preview
@Composable
fun PreviewTimerProgress() {
    MaterialTheme {
        val hostState = remember { TimerHostState() }

        LaunchedEffect(Unit) {
            hostState.handle(false, 5_000, 1.0f)
            delay(500)
            hostState.handle(true, 5_000, 0.25f)
            delay(2500)
            hostState.handle(false, 5_000, 1.0f)
        }

        Column {
            TimerProgressHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp),
                color = Color.Blue,
                animateCloseCall = true,
                hostState = hostState
            )
            TimerProgressHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .padding(top = 20.dp),
                animateCloseCall = true,
                hostState = hostState
            )
            TimerProgressHost(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp),
                hostState = hostState,
                isCircular = true
            )
        }

    }
}
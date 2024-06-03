package de.ka.crunchr.ui.composables

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.abs

@Composable
fun TimerProgress(
    modifier: Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    backgroundColor: Color = Color.Gray,
    color: Color = Color.Green
) {
    val coercedProgress = progress.coerceIn(0f, 1f)
    Canvas(modifier) {
        val strokeWidth = size.height
        drawLinearIndicatorBackground(backgroundColor, strokeWidth)
        drawLinearIndicator(
            coercedProgress,
            color,
            strokeWidth,
        )
    }
}

private fun DrawScope.drawLinearIndicatorBackground(
    color: Color,
    strokeWidth: Float
) = drawLinearIndicator(1f, color, strokeWidth)

private fun DrawScope.drawLinearIndicator(
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height

    val yOffset = height / 2

    drawLine(color, Offset(0f, yOffset), Offset(width * endFraction, yOffset), strokeWidth)
}
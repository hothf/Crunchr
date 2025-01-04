package de.ka.crunchr.ui.composables.utils

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.dp
import de.ka.crunchr.R

object UiDefaults {
    val defaultStroke = 1.dp
    val sideMargin = 12.dp
    val topMargin = 14.dp
    val smallPadding = 4.dp
    val tinyPadding = 1.dp
    val verySmallPadding = 2.dp
    val defaultPaddings = 8.dp
    val bigPaddings = 16.dp
    val blurRadius = 12.dp
    val defaultSpacer = 6.dp
    val mediumMargin = 6.dp
    val smallCorners = 6.dp
    val smallIconSize = 21.dp
    val defaultCorners = 12.dp
    val buttonSize = 52.dp
    val editsSize = 30.dp
    val editsBigSize = 40.dp
    val timerBigSize = 18.dp
    val timerSmallSize = 28.dp
    val smallButtonSize = 42.dp
    val defaultCheckBoxSize = 48.dp

    val defaultCheckImageRes = R.drawable.ic_check
    val circleCrossImageRes = R.drawable.ic_clear_round

    const val WIDTH_PERCENTAGE = 0.75f
    const val DISPLAY_SIDE_WIDTH_PERCENTAGE = 0.32f
    const val DISPLAY_MIDDLE_WIDTH_PERCENTAGE = 0.4f
    const val TOP_PERCENTAGE = 0.38f
    const val BOTTOM_PERCENTAGE = 0.62f
    const val START_PERCENTAGE = 0.45f
    const val END_PERCENTAGE = 0.55f

    const val QUICK_MS = 50
    const val VERY_FAST_MS = 75
    const val FAST_MS = 150
    const val MEDIUM_MS = 300
    const val LONG_MS = 500

    const val DELAY_LONG_MS = 3_000L
    const val DELAY_VERY_LONG_MS = 3_500L

    val overshoot = tween<Float>(QUICK_MS, easing = {
        OvershootInterpolator().getInterpolation(it)
    })
}
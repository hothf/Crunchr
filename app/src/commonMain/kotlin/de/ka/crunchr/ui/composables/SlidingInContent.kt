package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SlidingInContent(modifier: Modifier = Modifier, index: Int, isVisible: Boolean, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        visible = isVisible
    }

    AnimatedVisibility(
        modifier = modifier,
        label = "slide:$index",
        visible = visible,
        enter = appearAnimFor(index),
        exit = fadeOut()
    ) {
        content()
    }

}

private fun appearAnimFor(index: Int) = fadeIn(
    animationSpec = tween(300, 70 * index),
    initialAlpha = 0.4f
) + slideInVertically(
    animationSpec = tween(
        durationMillis = 300,
        delayMillis = 70 * index,
        easing = LinearOutSlowInEasing
    ),
    initialOffsetY = { it },
)

package de.ka.crunchr.ui.composables

import androidx.compose.animation.VectorConverter
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import de.ka.crunchr.ui.composables.utils.UiDefaults
import kotlinx.coroutines.delay

data class ColorUpdate(val isShown: Boolean = false, val animationEnded: Boolean = true)

class ColorUpdateHostState {
    var current by mutableStateOf(ColorUpdate())
        private set

    fun success() {
        current = ColorUpdate(isShown = true, animationEnded = false)
    }

    fun reset() {
        current = ColorUpdate()
    }

    fun fail() {
        current = ColorUpdate(isShown = false, animationEnded = false)
    }

    fun endAnimation() {
        current = current.copy(animationEnded = true)
    }
}

@Composable
fun getColorUpdate(
    hostState: ColorUpdateHostState,
    defaultColor: Color = MaterialTheme.colorScheme.secondary,
    successColor: Color = MaterialTheme.colorScheme.outline,
    errorColor: Color = MaterialTheme.colorScheme.error
): Animatable<Color, AnimationVector4D> {
    val color = remember {
        Animatable(defaultColor, Color.VectorConverter(ColorSpaces.Srgb))
    }

    LaunchedEffect(hostState.current) {
        if (hostState.current.animationEnded) return@LaunchedEffect
        if (hostState.current.isShown) {
            color.animateTo(successColor, animationSpec = tween(UiDefaults.VERY_FAST_MS))
            delay(UiDefaults.LONG_MS.toLong())
            color.animateTo(defaultColor, animationSpec = tween(UiDefaults.LONG_MS))
        } else {
            color.animateTo(errorColor, animationSpec = tween(UiDefaults.VERY_FAST_MS))
            delay(UiDefaults.LONG_MS.toLong())
            color.animateTo(defaultColor, animationSpec = tween(UiDefaults.LONG_MS))
        }
        hostState.endAnimation()
    }

    return color
}

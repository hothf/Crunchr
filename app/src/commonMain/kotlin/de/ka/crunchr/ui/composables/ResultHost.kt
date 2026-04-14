package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.ic_check_round
import de.ka.crunchr.generated.ic_clear_round
import de.ka.crunchr.ui.composables.utils.UiDefaults
import org.jetbrains.compose.resources.painterResource


@Composable
fun ResultHost(
    modifier: Modifier = Modifier,
    hostState: SolvingResultUpdateHostState,
    input: String
) {
    val current = hostState.currentSolvingResult

    LaunchedEffect(current) {
        delay(UiDefaults.DELAY_LONG_MS)
        hostState.hide()
    }

    LaunchedEffect(input) {
        if (input.isNotEmpty()) {
            hostState.hide()
        }
    }

    Result(modifier, current)
}

private suspend fun delay(ms: Long) = kotlinx.coroutines.delay(ms)

@Composable
private fun Result(modifier: Modifier, result: SolvingResult) {

    val scale = remember { Animatable(1f) }
    val translation = remember { Animatable(0f) }

    LaunchedEffect(result) {
        if (result.successful) {
            scale.animateTo(1.15f)
            scale.animateTo(1f)
        } else {
            translation.animateTo(-5f, animationSpec = UiDefaults.overshoot)
            translation.animateTo(5f, animationSpec = UiDefaults.overshoot)
            translation.animateTo(-5f, animationSpec = UiDefaults.overshoot)
            translation.animateTo(0f, animationSpec = UiDefaults.overshoot)
        }
    }

    Box(
        modifier = modifier
            .size(UiDefaults.editsSize),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = result.shown,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(
                animationSpec = tween(300)
            )
        ) {
            if (result.successful) {
                Image(
                    modifier = modifier
                        .size(UiDefaults.editsSize)
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                        },
                    painter = painterResource(Res.drawable.ic_check_round),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outline)
                )
            } else {
                Image(
                    modifier = modifier
                        .size(UiDefaults.editsSize)
                        .graphicsLayer {
                            translationX = translation.value
                        },
                    painter = painterResource(Res.drawable.ic_clear_round),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
                )
            }

        }
    }
}

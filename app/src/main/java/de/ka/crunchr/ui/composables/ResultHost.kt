package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchrgame.models.crunch.Crunch
import de.ka.crunchrgame.models.crunch.Symbols
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


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
                    painter = painterResource(R.drawable.ic_check_round),
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
                    painter = painterResource(R.drawable.ic_clear_round),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
                )
            }

        }
    }
}

@Composable
@Preview
fun PreviewResult() {
    val hostState = remember { SolvingResultUpdateHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            delay(2000)
            repeat(5) {
                hostState.show(
                    SolvingResult(
                        Random.nextBoolean(),
                        crunch = Crunch(1000, 1, 1, Symbols.ADD)
                    )
                )
                delay(4000)
            }
        }
    }

    MaterialTheme {
        Column {
            ResultHost(modifier = Modifier, hostState = hostState, input = "")
        }

    }
}
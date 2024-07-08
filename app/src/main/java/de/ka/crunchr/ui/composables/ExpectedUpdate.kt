package de.ka.crunchr.ui.composables

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.ui.composables.utils.ScoreDisplayUtils.getDisplayableFloat
import de.ka.crunchr.ui.composables.utils.UiDefaults
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Expected(
    val expected: String = "",
    val was: String = "",
    val points: String = "",
    val shown: Boolean = false,
)

class ExpectedUpdateHostState {
    var current by mutableStateOf(Expected())
        private set

    suspend fun show(result: SolvingResult) {
        if (current.expected.isNotEmpty()) {
            hide()
            delay(UiDefaults.VERY_FAST_MS.toLong())
        }
        current = Expected(
            expected = result.expected.getDisplayableFloat() ?: "",
            was = result.was?.getDisplayableFloat() ?: "",
            points = result.score,
            shown = true,
        )
    }

    fun hide() {
        current = current.copy(shown = false)
    }
}

@Composable
fun ExpectedUpdateHost(
    modifier: Modifier = Modifier,
    hostState: ExpectedUpdateHostState,
) {
    val current = hostState.current

    LaunchedEffect(current) {
        delay(UiDefaults.DELAY_LONG_MS)
        hostState.hide()
    }

    ExpectedUpdate(modifier = modifier, expected = current)
}

@Composable
fun ExpectedUpdate(modifier: Modifier, expected: Expected) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Update(
            visible = expected.shown,
            expected = expected.expected,
            was = expected.was,
            points = expected.points
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Update(
    visible: Boolean,
    expected: String,
    was: String,
    points: String,
    delay: Int = 0
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.clip(
                RoundedCornerShape(UiDefaults.defaultCorners)
            ),
            enter = scaleIn(
                animationSpec = tween(UiDefaults.MEDIUM_MS, delay, easing = {
                    OvershootInterpolator().getInterpolation(it)
                }),
                initialScale = 0f,
            ),
            exit = shrinkOut(
                targetSize = { it / 2 },
                shrinkTowards = Alignment.Center,
                clip = true
            )
        ) {
            Column(
                modifier = Modifier
                    .animateEnterExit(
                        enter = fadeIn(),
                        exit = fadeOut(animationSpec = tween(UiDefaults.MEDIUM_MS))
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(UiDefaults.defaultCorners)
                        )
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(
                            start = UiDefaults.defaultPaddings,
                            end = UiDefaults.defaultPaddings,
                            bottom = UiDefaults.tinyPadding
                        )

                ) {
                    Icon(
                        modifier = Modifier
                            .size(UiDefaults.smallIconSize)
                            .padding(top = UiDefaults.smallPadding),
                        imageVector = ImageVector.vectorResource(id = UiDefaults.circleCrossImageRes),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        text = was,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Row(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(UiDefaults.defaultCorners)
                        )
                        .background(MaterialTheme.colorScheme.error)
                        .padding(
                            start = UiDefaults.smallPadding,
                            end = UiDefaults.defaultPaddings
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(UiDefaults.smallIconSize)
                            .padding(top = UiDefaults.mediumMargin),
                        imageVector = ImageVector.vectorResource(id = UiDefaults.defaultCheckImageRes),
                        contentDescription = expected,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        modifier = Modifier.padding(vertical = UiDefaults.smallPadding),
                        text = expected,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

        }
    }
}

@Composable
@Preview
fun PreviewExpectedUpdate() {
    val hostState = remember { ExpectedUpdateHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            repeat(5) {
                val expected = Random.nextFloat() * 10
                hostState.show(
                    SolvingResult(
                        expected = expected,
                        was = expected + 1,
                        score = "-250"
                    )
                )
                delay(2000)
            }
        }
    }

    MaterialTheme {
        Column {
            ExpectedUpdateHost(modifier = Modifier, hostState = hostState)
            Spacer(modifier = Modifier.size(UiDefaults.editsSize))
            ExpectedUpdate(
                modifier = Modifier,
                expected = Expected(
                    expected = "1,5",
                    was = "222",
                    points = "-500",
                    shown = true
                )
            )
        }

    }
}
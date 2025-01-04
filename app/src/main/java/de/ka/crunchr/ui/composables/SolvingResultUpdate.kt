package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

data class SolvingResult(
    val successful: Boolean = false,
    val performance: String = "",
    val expected: Float = 0f,
    val was: Float? = null,
    val score: String = "",
    val multiplier: String = "",
    val shown: Boolean = false,
)

class SolvingResultUpdateHostState {
    var currentSolvingResult by mutableStateOf(SolvingResult())
        private set

    suspend fun show(score: SolvingResult) {
        if (currentSolvingResult.score.isNotEmpty()) {
            hide()
            delay(UiDefaults.MEDIUM_MS.toLong() + 100)
        }
        currentSolvingResult = SolvingResult(
            score.successful,
            score.performance,
            score.expected,
            score.was,
            score.score,
            score.multiplier
        ).copy(shown = true)
    }

    fun hide() {
        currentSolvingResult = currentSolvingResult.copy(shown = false)
    }
}

@Composable
fun SolvingResultUpdateHost(
    modifier: Modifier = Modifier,
    hostState: SolvingResultUpdateHostState,
    showPerformance: Boolean = false,
) {
    val currentScore = hostState.currentSolvingResult

    LaunchedEffect(currentScore) {
        delay(UiDefaults.DELAY_LONG_MS)
        hostState.hide()
    }

    SolvingResultUpdate(
        modifier = modifier,
        score = currentScore,
        showPerformance = showPerformance
    )
}

@Composable
fun SolvingResultUpdate(
    modifier: Modifier,
    score: SolvingResult,
    showPerformance: Boolean
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        SingleUpdate(
            visible = score.shown,
            score = score.score,
            expected = score.expected,
            performance = score.performance,
            showPerformance = showPerformance,
            successful = score.successful
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SingleUpdate(
    visible: Boolean,
    score: String,
    delay: Int = 0,
    expected: Float,
    performance: String,
    successful: Boolean,
    showPerformance: Boolean
) {
    val alignment = if (showPerformance) Alignment.CenterStart else Alignment.CenterEnd
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        AnimatedVisibility(
            visible = visible,
            enter = if (showPerformance) fadeIn() else slideInVertically(
                animationSpec = tween(UiDefaults.MEDIUM_MS, delay),
                initialOffsetY = { it / 2 },
            ),
            exit = if (showPerformance) fadeOut() else slideOutVertically(
                animationSpec = tween(UiDefaults.MEDIUM_MS, 0),
                targetOffsetY = { -it / 2 }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateEnterExit(
                        enter = fadeIn(),
                        exit = fadeOut(animationSpec = tween(200))
                    ),
                contentAlignment = Alignment.Center
            ) boxer@{
                val clipModifier = if (showPerformance) Modifier
                    .padding(
                        horizontal = UiDefaults.defaultPaddings,
                        vertical = UiDefaults.verySmallPadding
                    ) else Modifier
                if (!showPerformance && successful) {
                    Row(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(UiDefaults.defaultCorners)
                            )
                            .background(MaterialTheme.colorScheme.outline)
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
                            contentDescription = score,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        expected.getDisplayableFloat()?.let {
                            Text(
                                modifier = Modifier.padding(vertical = UiDefaults.smallPadding),
                                text = it,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                } else if (showPerformance && !successful) {
                    Text(
                        modifier = clipModifier.padding(vertical = UiDefaults.verySmallPadding),
                        text = score,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if (showPerformance && successful) {
                    Text(
                        modifier = clipModifier.padding(vertical = UiDefaults.verySmallPadding),
                        text = "$score $performance",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }
    }
}

@Composable
@Preview
fun PreviewResultUpdate() {
    val hostState = remember { SolvingResultUpdateHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            repeat(5) {
                hostState.show(
                    SolvingResult(
                        true,
                        performance = "Okay",
                        score = Random.nextLong(100, 5000).toString()
                    )
                )
                delay(2000)
            }
        }
    }

    MaterialTheme {
        Column {
            SolvingResultUpdateHost(modifier = Modifier.fillMaxWidth(), hostState = hostState)
            SolvingResultUpdateHost(
                modifier = Modifier.fillMaxWidth(),
                hostState = hostState,
                showPerformance = true
            )
            SingleUpdate(
                visible = true,
                score = "1234",
                performance = "okay",
                expected = 12.2f,
                showPerformance = false,
                successful = false
            )
            SingleUpdate(
                visible = true,
                score = "1234",
                performance = "okay okay! okay",
                expected = 12.2f,
                showPerformance = true,
                successful = true
            )
        }

    }
}
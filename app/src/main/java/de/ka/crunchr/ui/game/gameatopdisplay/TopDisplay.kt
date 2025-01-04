package de.ka.crunchr.ui.game.gameatopdisplay

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.ui.composables.ExpectedUpdateHost
import de.ka.crunchr.ui.composables.SolvingResultUpdateHost
import de.ka.crunchr.ui.composables.TimeLeftHost
import de.ka.crunchr.ui.composables.TimerProgressHost
import de.ka.crunchr.ui.composables.getColorUpdate
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.game.GameHostStates
import de.ka.crunchr.ui.game.GameViewModel
import de.ka.crunchr.ui.theme.CrunchrTheme
import de.ka.crunchrgame.models.crunch.Crunch
import de.ka.crunchrgame.models.crunch.Symbols
import kotlinx.coroutines.launch

@Composable
fun TopDisplay(
    modifier: Modifier = Modifier,
    uiState: GameViewModel.UiState,
    gameHostStates: GameHostStates = GameHostStates()
) {
    val color = getColorUpdate(hostState = gameHostStates.colorUpdateHostState)
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(UiDefaults.sideMargin)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                var score by remember {
                    mutableIntStateOf(
                        uiState.currentScore?.score?.toInt() ?: 0
                    )
                }
                val scoreCounter by animateIntAsState(
                    targetValue = score,
                    animationSpec = tween(
                        durationMillis = if (score == 0) 0 else UiDefaults.LONG_MS,
                        easing = FastOutSlowInEasing
                    ), label = "score"
                )
                LaunchedEffect(uiState.currentScore) {
                    if (uiState.currentScore != null) {
                        score = uiState.currentScore.score.toInt()
                    }
                }
                Text(
                    modifier = Modifier
                        .weight(1f),

                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.game_level))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" ${uiState.currentScore?.level?.value}")
                        }
                    },
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = UiDefaults.smallPadding),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            uiState.currentScore?.level?.symbols?.forEach {
                                append(it.stringRepresentation)
                            }
                        }
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    modifier = Modifier
                        .weight(1f),

                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.game_score))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" $scoreCounter")
                        }
                    },
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.titleSmall,
                    color = color.value
                )
            }

            TimeLeftHost(
                timerHostState = gameHostStates.gameTimerHostState
            ) { time ->
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.game_time))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" ${stringResource(R.string.time_seconds, "${time / 1000}")}")
                        }
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            TimerProgressHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDefaults.defaultPaddings)
                    .height(UiDefaults.timerBigSize)
                    .clip(RoundedCornerShape(UiDefaults.smallCorners)),
                animateCloseCall = true,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                hostState = gameHostStates.gameTimerHostState
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(UiDefaults.DISPLAY_SIDE_WIDTH_PERCENTAGE)) {
                ExpectedUpdateHost(
                    modifier = Modifier
                        .fillMaxWidth(),
                    hostState = gameHostStates.expectedUpdateHostState
                )
            }
            val rotation = remember { Animatable(0.0f) }
            val alpha = remember { Animatable(0f) }
            var text by remember { mutableStateOf("") }
            LaunchedEffect(uiState.crunch) {
                launch {
                    alpha.animateTo(0f, animationSpec = tween(0))
                }
                rotation.animateTo(90.0f, animationSpec = tween(UiDefaults.FAST_MS))
                launch {
                    text = uiState.crunch?.display ?: ""
                    alpha.animateTo(
                        1f,
                        animationSpec = tween(UiDefaults.FAST_MS + UiDefaults.VERY_FAST_MS)
                    )
                }
                rotation.animateTo(0.0f, animationSpec = tween(UiDefaults.FAST_MS))
            }
            Column(
                modifier = Modifier
                    .weight(UiDefaults.DISPLAY_MIDDLE_WIDTH_PERCENTAGE),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(UiDefaults.editsBigSize)
                            .graphicsLayer {
                                rotationY = rotation.value
                                cameraDistance = 12f * density
                            }
                            .clip(RoundedCornerShape(UiDefaults.defaultCorners))
                            .background(color = MaterialTheme.colorScheme.onSecondary)
                            .padding(horizontal = UiDefaults.defaultPaddings)
                            .alpha(alpha.value),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Box(
                    modifier = Modifier.padding(vertical = UiDefaults.bigPaddings),
                    contentAlignment = Alignment.Center
                ) {
                    TimerProgressHost(
                        modifier = Modifier
                            .size(UiDefaults.timerSmallSize),
                        hostState = gameHostStates.crunchTimerHostState,
                        isCircular = true,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    TimeLeftHost(timerHostState = gameHostStates.crunchTimerHostState) { time ->
                        Text(
                            text = "${time / 1000}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                val translation = remember { Animatable(0f) }
                LaunchedEffect(gameHostStates.expectedUpdateHostState.current) {
                    val result = gameHostStates.expectedUpdateHostState.current
                    if (result.shown) {
                        translation.animateTo(-25f, animationSpec = UiDefaults.overshoot)
                        translation.animateTo(25f, animationSpec = UiDefaults.overshoot)
                        translation.animateTo(-25f, animationSpec = UiDefaults.overshoot)
                        translation.animateTo(0f, animationSpec = UiDefaults.overshoot)
                    }
                }
                Box(
                    modifier = Modifier
                        .graphicsLayer { translationX = translation.value }
                        .fillMaxWidth()
                        .height(UiDefaults.editsBigSize)
                        .background(MaterialTheme.colorScheme.secondary)
                        .border(
                            BorderStroke(
                                UiDefaults.defaultStroke,
                                color = color.value
                            ),
                            RoundedCornerShape(UiDefaults.defaultCorners)
                        )
                        .clip(RoundedCornerShape(UiDefaults.defaultCorners)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "alpha")
                    val alphaAnim by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(UiDefaults.LONG_MS * 2, easing = LinearEasing)
                        ), label = "alpha"
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = UiDefaults.defaultPaddings)
                            .alpha(if (uiState.input.isNotEmpty()) 1f else alphaAnim),
                        text = stringResource(
                            id = R.string.game_display_caret
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = color.value
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = uiState.input,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = color.value
                    )
                }
                Spacer(modifier = Modifier.size(UiDefaults.topMargin))
            }

            Column(
                modifier = Modifier.weight(UiDefaults.DISPLAY_SIDE_WIDTH_PERCENTAGE),
                verticalArrangement = Arrangement.Center
            ) {
                SolvingResultUpdateHost(
                    modifier = Modifier
                        .fillMaxWidth(),
                    hostState = gameHostStates.scoreUpdateHostState
                )
                SolvingResultUpdateHost(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = UiDefaults.defaultPaddings),
                    hostState = gameHostStates.scoreUpdateHostState,
                    showPerformance = true
                )

            }
        }
    }
}

@Preview
@Composable
fun PreviewTopDisplay() {
    CrunchrTheme {
        Column(modifier = Modifier.fillMaxHeight(1f)) {
            TopDisplay(
                uiState = GameViewModel.UiState(
                    input = "123,45",
                    crunch = Crunch(
                        2000,
                        firstNum = 1,
                        secondNum = 2,
                        symbol = Symbols.ADD
                    )
                ),
            )
        }
    }
}
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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.game_display_caret
import de.ka.crunchr.generated.game_time
import de.ka.crunchr.generated.time_seconds
import de.ka.crunchr.ui.composables.ResultHistory
import de.ka.crunchr.ui.composables.ResultHistoryHost
import de.ka.crunchr.ui.composables.ResultHistoryHostState
import de.ka.crunchr.ui.composables.ResultHost
import de.ka.crunchr.ui.composables.ScoreHost
import de.ka.crunchr.ui.composables.TimeLeftHost
import de.ka.crunchr.ui.composables.TimerProgressHost
import de.ka.crunchr.ui.composables.getColorUpdate
import de.ka.crunchr.ui.composables.utils.Corners
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.innerShadow
import de.ka.crunchr.ui.game.GameHostStates
import de.ka.crunchr.ui.game.GameViewModel
import de.ka.crunchr.ui.game.getLevelSymbols
import de.ka.crunchrgame.models.Level
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopDisplay(
    modifier: Modifier = Modifier,
    uiState: GameViewModel.UiState,
    gameHostStates: GameHostStates = GameHostStates(),
    isHorizontal: Boolean = false,
) {
    val color = getColorUpdate(hostState = gameHostStates.colorUpdateHostState)
    Column(
        modifier = modifier
            .padding(
                top = if (isHorizontal) 0.dp else UiDefaults.bigPaddings,
                end = if (isHorizontal) UiDefaults.bigPaddings else 0.dp
            )
            .clip(
                RoundedCornerShape(
                    topStart = if (isHorizontal) 0.dp else UiDefaults.defaultCorners,
                    topEnd = UiDefaults.defaultCorners,
                    bottomEnd = if (isHorizontal) UiDefaults.defaultCorners else 0.dp
                )
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .innerShadow(
                blur = UiDefaults.blurRadius,
                hideSides = false,
                color = MaterialTheme.colorScheme.tertiary,
                cornersRadius = UiDefaults.defaultCorners,
                corners = Corners(
                    topLeft = !isHorizontal,
                    topRight = true,
                    bottomRight = isHorizontal
                )
            )
            .padding(UiDefaults.sideMargin)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (uiState.status.current != GameViewModel.GameScreenStatus.RUNNING) return@Column
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(UiDefaults.buttonSize)
            ) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("$scoreCounter")
                            }
                        },
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        color = color.value
                    )
                    ScoreHost(
                        modifier = Modifier.fillMaxWidth(),
                        hostState = gameHostStates.scoreUpdateHostState
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TimerProgressHost(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(UiDefaults.defaultPaddings)
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary),
                                RoundedCornerShape(UiDefaults.smallCorners)
                            )
                            .clip(RoundedCornerShape(UiDefaults.smallCorners)),
                        animateCloseCall = true,
                        color = MaterialTheme.colorScheme.onSecondary,
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        hostState = gameHostStates.gameTimerHostState
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(getLevelSymbols(uiState.currentScore?.level ?: Level()))
                            }
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    TimeLeftHost(
                        timerHostState = gameHostStates.gameTimerHostState
                    ) { time ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(
                                        "${
                                            stringResource(
                                                Res.string.time_seconds,
                                                "${time / 1000}"
                                            )
                                        } "
                                    )
                                }
                                append(stringResource(Res.string.game_time))
                            },
                            modifier = modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = UiDefaults.veryBigPaddings),
                contentAlignment = Alignment.CenterEnd
            ) {
                ResultHistoryHost(
                    modifier = Modifier.fillMaxWidth(),
                    hostState = gameHostStates.resultHistoryHostState
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val alpha = remember { Animatable(0f) }
                    var text by remember { mutableStateOf("") }
                    LaunchedEffect(uiState.crunch) {
                        alpha.animateTo(0f, animationSpec = tween(0))
                        text = uiState.crunch?.display ?: ""
                        alpha.animateTo(
                            1f,
                            animationSpec = tween(UiDefaults.FAST_MS + UiDefaults.VERY_FAST_MS)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = UiDefaults.mediumPadding,
                                    end = UiDefaults.mediumPadding
                                )
                                .alpha(alpha.value),
                            text = text,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        Box(
                            modifier = Modifier
                                .alpha(alpha.value),
                            contentAlignment = Alignment.Center
                        ) {
                            TimerProgressHost(
                                modifier = Modifier
                                    .size(UiDefaults.smallIconSize),
                                hostState = gameHostStates.crunchTimerHostState,
                                isCircular = true,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Column(modifier = Modifier.height(UiDefaults.bigPaddings).padding(
                        end = 0.dp
                    ),
                        verticalArrangement = Arrangement.Center) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                )

                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .height(1.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                )

                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ResultHost(hostState = gameHostStates.scoreUpdateHostState, input = uiState.input )
                        val infiniteTransition = rememberInfiniteTransition(label = "alpha")
                        val alphaAnim by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(UiDefaults.LONG_MS * 2, easing = LinearEasing)
                            ), label = "alpha"
                        )
                        Text(
                            modifier = Modifier.padding(start = UiDefaults.smallPadding),
                            text = uiState.input,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = color.value
                        )
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 11.dp,
                                    end = 2.dp,
                                )
                                .alpha(if (uiState.input.isNotEmpty()) 1f else alphaAnim),
                            text = stringResource(
                                Res.string.game_display_caret
                            ),
                            style = MaterialTheme.typography.titleLarge,
                            color = color.value
                        )
                    }

                    Spacer(modifier = Modifier.size(UiDefaults.editsBigSize))
                }
            }

        }
    }
}

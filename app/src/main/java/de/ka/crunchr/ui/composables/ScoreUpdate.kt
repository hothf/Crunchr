package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ka.crunchr.ui.game.ScoreDisplayUtils
import de.ka.crunchr.ui.theme.CrunchrTheme
import de.ka.crunchrgame.CrunchResult
import de.ka.crunchrgame.Level
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class Score(
    val successful: Boolean,
    val performance: String,
    val score: String? = null,
    val multiplier: String? = null,
    val timeStamp: Long = System.currentTimeMillis()
)

class ScoreUpdateHostState {
    var currentScore by mutableStateOf<Score?>(null)
        private set

    suspend fun show(score: Score) {
        if (currentScore != null) {
            currentScore = null
            delay(500)
        }
        currentScore = score
    }

    fun hide() {
        currentScore = null
    }
}

@Composable
fun ScoreUpdateHost(
    modifier: Modifier = Modifier,
    hostState: ScoreUpdateHostState,
) {
    val currentScore = hostState.currentScore
    if (currentScore != null) {
        LaunchedEffect(currentScore) {
            delay(5000L)
            hostState.hide()
        }
    }
    ScoreUpdate(modifier = modifier, score = currentScore)
}

@Composable
fun ScoreUpdate(modifier: Modifier, score: Score?) {
    Column(
        modifier = Modifier
            .height(300.dp)
            .width(200.dp)
    ) {
        SingleScoreUpdate(visible = (score?.performance != null), score = score?.performance, 0)
        SingleScoreUpdate(visible = (score?.score != null), score = score?.score, 200)
        SingleScoreUpdate(visible = (score?.multiplier != null), score = score?.multiplier, 400)
    }
}

data class LastKnownState(var current: String? = null)

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SingleScoreUpdate(visible: Boolean, score: String?, delay: Int) {
    val state = remember { LastKnownState(score) }
    if (score != null) {
        state.current = score
    }
    Box(modifier = Modifier.height(50.dp)) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(animationSpec = tween(500, delay)),
            exit = slideOutVertically(targetOffsetY = { -50 })
        ) {
            Box(
                modifier = Modifier.animateEnterExit(
                    enter = fadeIn(),
                    exit = fadeOut()
                )
            ) {
                state.current?.let {
                    Text(text = it)
                }

            }

        }
    }
}

@Composable
@Preview
fun PreviewScoreUpdate() {
    val hostState = remember { ScoreUpdateHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            ScoreDisplayUtils.createScoreFor(
                CrunchResult(true, Level(1), 500)
            )?.let { hostState.show(it) }
        }
    }

    CrunchrTheme {
        ScoreUpdateHost(hostState = hostState)
    }
}
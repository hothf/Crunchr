package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchrgame.models.crunch.Crunch
import kotlinx.coroutines.delay

@Composable
fun ScoreHost(
    modifier: Modifier = Modifier,
    hostState: SolvingResultUpdateHostState
) {
    val currentScore = hostState.currentSolvingResult

    LaunchedEffect(currentScore) {
        delay(UiDefaults.DELAY_LONG_MS)
        hostState.hide()
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = currentScore.shown,
        enter = fadeIn(),
        exit = fadeOut() + slideOutVertically(
            animationSpec = tween(UiDefaults.MEDIUM_MS),
            targetOffsetY = { -it / 2 }
        )
    ) {
        if (currentScore.successful) {
            Text(
                text = "${currentScore.performance} ${currentScore.score}".trim(),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelSmall
            )
        } else {
            Text(
                text = currentScore.score,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

data class SolvingResult(
    val successful: Boolean = false,
    val performance: String = "",
    val crunch: Crunch? = null,
    val was: Float? = null,
    val score: String = "",
    val multiplier: String = "",
    val shown: Boolean = false,
)

class SolvingResultUpdateHostState {
    var currentSolvingResult by mutableStateOf(SolvingResult())
        private set

    fun reset() {
        currentSolvingResult = SolvingResult()
    }

    fun show(score: SolvingResult) {
        if (currentSolvingResult.score.isNotEmpty()) {
            hide()
        }
        currentSolvingResult = SolvingResult(
            score.successful,
            score.performance,
            score.crunch,
            score.was,
            score.score,
            score.multiplier
        ).copy(shown = true)
    }

    fun hide() {
        currentSolvingResult = currentSolvingResult.copy(shown = false)
    }
}

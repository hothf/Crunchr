package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchrgame.models.crunch.Crunch
import kotlinx.coroutines.delay
import java.text.DecimalFormat


@Composable
fun ResultHistoryHost(
    modifier: Modifier = Modifier,
    hostState: ResultHistoryHostState,
) {
    val currentHistory = hostState.current

    LaunchedEffect(currentHistory) {
        delay(UiDefaults.DELAY_LONG_MS)
        hostState.hide()
    }

    HistoryEntry(modifier, currentHistory)
}

@Composable
private fun HistoryEntry(modifier: Modifier, history: ResultHistory) {

    val item = history.entry
    val text = if (item != null) "${item.first.display} = ${
        DecimalFormat().apply {
            maximumFractionDigits = 1
        }.format(item.first.expected)
    }" else ""

    val translation = remember { Animatable(0f) }
    var rememberedText by remember { mutableStateOf(text) }

    LaunchedEffect(item) {
        if (item != null) {
            rememberedText = text


            translation.animateTo(-25f, animationSpec = UiDefaults.overshoot)
            translation.animateTo(25f, animationSpec = UiDefaults.overshoot)
            translation.animateTo(-25f, animationSpec = UiDefaults.overshoot)
            translation.animateTo(0f, animationSpec = UiDefaults.overshoot)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(UiDefaults.editsBigSize),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = history.entry != null,
            enter = slideInHorizontally(initialOffsetX = { it / 2 }),
            exit = fadeOut(
                animationSpec = tween(300)
            )
        ) {
            if (item == null) return@AnimatedVisibility
            if (!item.second) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationX = translation.value
                        },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    text = rememberedText
                )
            }

        }
    }
}

data class ResultHistory(
    val entry: Pair<Crunch, Boolean>? = null,
)

class ResultHistoryHostState(private val initial: ResultHistory = ResultHistory()) {
    var current by mutableStateOf(initial)
        private set

    fun reset() {
        current = initial
    }

    fun show(result: SolvingResult) {
        hide()

        if (result.crunch == null) return

        current = ResultHistory(entry = result.crunch to result.successful)
    }

    fun hide() {
        current = ResultHistory(entry = null)
    }
}

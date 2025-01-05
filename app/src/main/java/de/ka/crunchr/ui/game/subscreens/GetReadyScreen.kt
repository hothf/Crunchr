package de.ka.crunchr.ui.game.subscreens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.game.GameInteractions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GetReadyScreen(
    isVisible: Boolean = false,
    gameInteractions: GameInteractions = GameInteractions()
) {
    val resetTime = 3
    var time by remember { mutableIntStateOf(resetTime) }

    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            time = resetTime
            scope.launch {
                repeat(resetTime) { count ->
                    delay(1000)
                    time = resetTime - (count + 1)
                }
                gameInteractions.onReady()
            }
        }
    }

    LaunchedEffect(time) {
        scale.animateTo(1.25f)
        scale.animateTo(1f)
    }

    AnimatedContainer(isVisible = isVisible) {
        Menu(
            isVisible = isVisible,
            menuTitle = stringResource(R.string.game_starts),
            padTitle = false,
            menuHint = {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.fillMaxWidth().graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    },
                    textAlign = TextAlign.Center,
                    text = "$time"
                )
            },
            menuEntries = emptyList(),
            middleContent = {
                1
            }
        )
    }
}

@Preview
@Composable
private fun PreviewGetReadyScreen(){
    MaterialTheme {
        GetReadyScreen(true)
    }
}
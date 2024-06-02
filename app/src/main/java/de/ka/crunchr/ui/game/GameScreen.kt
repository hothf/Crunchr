package de.ka.crunchr.ui.game

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.ka.crunchr.data.AppGameSaverImpl
import de.ka.crunchr.ui.composables.ScoreUpdateHost
import de.ka.crunchr.ui.composables.ScoreUpdateHostState

import de.ka.crunchr.ui.lifecycle.OnPause
import de.ka.crunchr.ui.theme.CrunchrTheme
import de.ka.crunchrgame.GameStatus

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val context = LocalContext.current.applicationContext
    val uiState by viewModel.state.collectAsState()

    OnPause {
        viewModel.pause()
    }

    LaunchedEffect(viewModel) {
        viewModel.saver = { AppGameSaverImpl(context) }
    }

    GameScreenContent(
        uiState = uiState,
        onStart = { viewModel.start() },
        onPause = { viewModel.pause() },
        onResume = { viewModel.resume() },
        onQuit = { viewModel.quit() },
        onSolve = { viewModel.solve() },
        input = { input -> viewModel.updateInput(input) },
        clear = { viewModel.clear() }
    )
}

@Composable
fun GameScreenContent(
    uiState: GameViewModel.UiState,
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onQuit: () -> Unit = {},
    onSolve: () -> Unit = {},
    input: (String) -> Unit = {},
    clear: () -> Unit = {}
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GameInputsScreen(
            uiState = uiState,
            onPause = onPause,
            onSolve = onSolve,
            onQuit = onQuit,
            input = input,
            clear = clear
        )

        if (uiState.state == GameStatus.PAUSED) {
            GamePauseScreen(onResume)
        }

        if (uiState.state == GameStatus.ENDED) {
            GameOverScreen(onStart)
        }

        if (uiState.state == GameStatus.NOT_STARTED) {
            GameNotStartedScreen(onStart)
        }
    }
}

@Composable
private fun GameInputsScreen(
    uiState: GameViewModel.UiState,
    onPause: () -> Unit = {},
    onQuit: () -> Unit = {},
    onSolve: () -> Unit = {},
    input: (String) -> Unit = {},
    clear: () -> Unit = {}
) {
    val scoreUpdateHostState = remember { ScoreUpdateHostState() }
    val color = remember { Animatable(Color.White) }

    LaunchedEffect(uiState.score) {
        if (uiState.score != null) {
            if (uiState.score.successful) {
                color.animateTo(Color.Green, animationSpec = tween(500))
                color.animateTo(Color.White, animationSpec = tween(500))
                scoreUpdateHostState.show(uiState.score)
            } else {
                color.animateTo(Color.Red, animationSpec = tween(500))
                color.animateTo(Color.White, animationSpec = tween(500))
                scoreUpdateHostState.hide()
            }
        }
    }
    Column {

        Row() {
            Text(
                modifier = Modifier.weight(2f),
                text = "Status: ${uiState.state.name} " +
                        ":: Display ${uiState.crunch?.display} " +
                        ":: Timeleft ${uiState.gameOverTimeLeft}, " +
                        ":: TimeleftCrunch ${uiState.crunchTimeLeft}" +
                        ":: Crunches solved ${uiState.crunchesSolved}" +
                        ":: Score ${uiState.currentScore}"
            )
            ScoreUpdateHost(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
                    .weight(1f),
                hostState = scoreUpdateHostState
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = color.value),
            text = uiState.crunch?.display ?: "",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 60.sp
            ),
            textAlign = TextAlign.End
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.input,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 60.sp
            )
        )

        Row {
            Column {
                Button(onClick = { input("1") }) {
                    Text(text = "1")
                }
            }
            Column {
                Button(onClick = { input("2") }) {
                    Text(text = "2")
                }
            }
            Column {
                Button(onClick = { input("3") }) {
                    Text(text = "3")
                }
            }
            Button(onClick = { onSolve() }) {
                Text(text = "solve crunch!")
            }
            Button(onClick = onPause) {
                Text(text = "Pause")
            }
        }

        Row {
            Column {
                Button(onClick = { input("4") }) {
                    Text(text = "4")
                }
            }
            Column {
                Button(onClick = { input("5") }) {
                    Text(text = "5")
                }
            }
            Column {
                Button(onClick = { input("6") }) {
                    Text(text = "6")
                }
            }
            Button(onClick = { input("0") }) {
                Text(text = "0")
            }
        }
        Row {
            Column {
                Button(onClick = { input("7") }) {
                    Text(text = "7")
                }
            }
            Column {
                Button(onClick = { input("8") }) {
                    Text(text = "8")
                }
            }
            Column {
                Button(onClick = { input("9") }) {
                    Text(text = "9")
                }
            }
            Button(onClick = { input(".") }) {
                Text(text = ".")
            }
        }

        Button(onClick = clear) {
            Text(text = "<-")
        }

        Column(
            modifier = Modifier
        ) {
            Button(onClick = onQuit) {
                Text(text = "Quit")
            }
        }
    }
}

@Composable
private fun GamePauseScreen(onResume: () -> Unit = {}) {
    Text(text = "Game Paused")
    Button(onClick = onResume) {
        Text(text = "Resume Game!")
    }
}

@Composable
private fun GameOverScreen(onStart: () -> Unit = {}) {
    Column(
        modifier = Modifier.background(Color.Gray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Game Over")
        Button(onClick = onStart) {
            Text(text = "Start new game!")
        }
    }
}

@Composable
private fun GameNotStartedScreen(onStart: () -> Unit = {}) {
    Column(
        modifier = Modifier.background(Color.Gray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome!")
        Button(onClick = onStart) {
            Text(text = "Start new game!")
        }
    }
}

@Composable
@Preview
fun PreviewGameScreen() {
    CrunchrTheme {
        GameScreenContent(uiState = GameViewModel.UiState(state = GameStatus.ENDED))
    }
}
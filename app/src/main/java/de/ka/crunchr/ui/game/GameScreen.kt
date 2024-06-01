package de.ka.crunchr.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.data.AppGameSaverImpl

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
        onSolve = { input -> viewModel.solve(input) }
    )
}

@Composable
fun GameScreenContent(
    uiState: GameViewModel.UiState,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onResume: () -> Unit = {},
    onQuit: () -> Unit = {},
    onSolve: (Float) -> Unit = {}
) {
    Column {
        Text(
            text = "Status: ${uiState.state.name} " +
                    ":: Display ${uiState.crunch?.display} " +
                    ":: Timeleft ${uiState.gameOverTimeLeft}, " +
                    ":: TimeleftCrunch ${uiState.crunchTimeLeft}" +
                    ":: Crunches solved ${uiState.crunchesSolved}" +
                    ":: Score ${uiState.currentScore}"
        )

        Column(
            modifier = Modifier
        ) {
            when (uiState.state) {
                GameStatus.NOT_STARTED, GameStatus.ENDED -> {
                    Button(onClick = onStart) {
                        Text(text = "Start new game!")
                    }
                }

                GameStatus.PAUSED -> {
                    Button(onClick = onResume) {
                        Text(text = "Resume game!")
                    }
                }

                GameStatus.RUNNING -> {
                    Button(onClick = { onSolve(1f) }) {
                        Text(text = "solve crunch!")
                    }
                    Button(onClick = onPause) {
                        Text(text = "Pause")
                    }
                }
            }
            Button(onClick = onQuit) {
                Text(text = "Quit")
            }

        }
    }
}

@Composable
@Preview
fun PreviewGameScreen() {
    CrunchrTheme {
        GameScreenContent(uiState = GameViewModel.UiState(state = GameStatus.RUNNING))
    }
}
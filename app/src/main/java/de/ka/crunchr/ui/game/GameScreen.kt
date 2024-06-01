package de.ka.crunchr.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
        onSolve = { viewModel.solve() },
        input = { input -> viewModel.updateInput(input) }
    )
}

@Composable
fun GameScreenContent(
    uiState: GameViewModel.UiState,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onResume: () -> Unit = {},
    onQuit: () -> Unit = {},
    onSolve: () -> Unit = {},
    input: (String) -> Unit = {}
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


        Text(
            modifier = Modifier.fillMaxWidth(),
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
        }

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

                GameStatus.RUNNING -> { /* do nothing */
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
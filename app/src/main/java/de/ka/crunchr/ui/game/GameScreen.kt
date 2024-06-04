package de.ka.crunchr.ui.game

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.ui.game.subscreens.GameNotStartedScreen
import de.ka.crunchr.ui.game.subscreens.GameOverScreen
import de.ka.crunchr.ui.game.subscreens.PauseScreen
import de.ka.crunchr.ui.game.subscreens.SettingsScreen

import de.ka.crunchr.ui.composables.lifecycle.OnLifeCycle
import de.ka.crunchr.ui.composables.lifecycle.collectInLaunchedEffectWithLifecycle
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.game.gamebelowinput.LowerInput
import de.ka.crunchr.ui.game.gameatopdisplay.TopDisplay
import de.ka.crunchr.ui.game.subscreens.LevelSelectScreen
import de.ka.crunchr.ui.game.subscreens.LoadingScreen
import de.ka.crunchr.ui.theme.CrunchrTheme
import de.ka.crunchrgame.models.crunch.Crunch
import de.ka.crunchrgame.models.crunch.Symbols
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    viewModel: GameViewModel = GameViewModel(),
    gameHostStates: GameHostStates = defaultGameHostStates(),
    handleBack: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()

    OnLifeCycle(
        onDestroyEvent = viewModel::release,
        onPauseEvent = viewModel::pause
    )

    BackHandler {
        if (!viewModel.consumeBack()) {
            handleBack()
        }
    }

    val gameInteractions = GameInteractions(
        onStart = { level -> viewModel.start(level) },
        onPause = { viewModel.pause() },
        onResume = { viewModel.resume() },
        onForfeit = { viewModel.forfeit() },
        onSolve = { viewModel.solve() },
        input = { input -> viewModel.updateInput(input) },
        clear = { viewModel.clear() },
        onQuit = { viewModel.quit() },
        onExit = { handleBack() },
        onBack = { viewModel.back() }
    )

    val settingsInteractions = SettingsInteractions(
        onOpenLevelSelect = { viewModel.openLevelSelect() },
        onOpenSettings = { open -> if (open) viewModel.openSettings() else viewModel.back() },
        onSettingsChanged = { vibration, sound -> viewModel.saveSettings(vibration, sound) }
    )

    viewModel.event.collectInLaunchedEffectWithLifecycle {
        when (it) {
            is GameViewModel.Event.GameTimeEvent -> {
                gameHostStates.gameTimerHostState.handle(
                    stop = it.stopped,
                    timeLeftMs = it.timeMs.toInt(),
                    progress = it.percentage
                )
            }

            is GameViewModel.Event.CrunchTimeEvent -> {
                gameHostStates.crunchTimerHostState.handle(
                    stop = it.stopped,
                    timeLeftMs = it.timeMs.toInt(),
                    progress = it.percentage
                )
            }

            is GameViewModel.Event.SolvingResultEvent -> {
                if (it.result.successful) {
                    launch { gameHostStates.scoreUpdateHostState.show(it.result) }
                    launch { gameHostStates.expectedUpdateHostState.hide() }
                    launch { gameHostStates.colorUpdateHostState.success() }
                } else {
                    launch { gameHostStates.scoreUpdateHostState.hide() }
                    launch { gameHostStates.expectedUpdateHostState.show(it.result) }
                    launch { gameHostStates.colorUpdateHostState.fail() }
                }
            }

            else -> {
                /* do nothing */
            }
        }
    }

    GameScreenContent(
        uiState = uiState,
        gameInteractions = gameInteractions,
        settingsInteractions = settingsInteractions,
        gameHostStates = gameHostStates
    )
}

@Composable
fun GameScreenContent(
    uiState: GameViewModel.UiState,
    gameInteractions: GameInteractions = GameInteractions(),
    settingsInteractions: SettingsInteractions = SettingsInteractions(),
    gameHostStates: GameHostStates = GameHostStates(),
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.status.current != GameViewModel.GameScreenStatus.NOT_LOADED) {
            val configuration = LocalConfiguration.current
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    LowerInput(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(UiDefaults.END_PERCENTAGE),
                        gameInteractions = gameInteractions,
                        isHorizontal = true
                    )
                    TopDisplay(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(UiDefaults.START_PERCENTAGE),
                        uiState = uiState,
                        gameHostStates = gameHostStates
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxHeight()) {
                    TopDisplay(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(UiDefaults.TOP_PERCENTAGE),
                        uiState = uiState,
                        gameHostStates = gameHostStates
                    )
                    LowerInput(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(UiDefaults.BOTTOM_PERCENTAGE),
                        gameInteractions = gameInteractions
                    )
                }
            }
        }
        LoadingScreen(isVisible = uiState.status.current != GameViewModel.GameScreenStatus.RUNNING)
        PauseScreen(
            isVisible = uiState.status.current == GameViewModel.GameScreenStatus.PAUSED,
            score = uiState.currentScore,
            gameInteractions = gameInteractions,
            settingsInteractions = settingsInteractions
        )
        GameOverScreen(
            isVisible = uiState.status.current == GameViewModel.GameScreenStatus.ENDED,
            score = uiState.currentScore,
            highScore = uiState.highScore,
            gameInteractions = gameInteractions
        )
        GameNotStartedScreen(
            isVisible = uiState.status.current == GameViewModel.GameScreenStatus.NOT_STARTED,
            gameInteractions = gameInteractions,
            settingsInteractions = settingsInteractions
        )
        SettingsScreen(
            isVisible = uiState.status.current == GameViewModel.GameScreenStatus.SETTINGS,
            appSettings = uiState.settings,
            settingsInteractions = settingsInteractions
        )
        LevelSelectScreen(
            isVisible = uiState.status.current == GameViewModel.GameScreenStatus.CHOOSE_LEVEL,
            gameInteractions = gameInteractions
        )
    }
}

@Composable
@Preview
fun PreviewGameScreenPortrait() {
    CrunchrTheme {
        GameScreenContent(
            uiState = GameViewModel.UiState(
                status = GameViewModel.ScreenStates(current = GameViewModel.GameScreenStatus.RUNNING),
                crunch = Crunch(2000, 1, 2, Symbols.ADD),
                input = "123"
            )
        )
    }
}

@Composable
@Preview(widthDp = 800, heightDp = 450)
fun PreviewGameScreenLandscape() {
    CrunchrTheme {
        val configuration = Configuration().apply {
            orientation = Configuration.ORIENTATION_LANDSCAPE
        }
        CompositionLocalProvider(LocalConfiguration provides configuration) {
            GameScreenContent(
                uiState = GameViewModel.UiState(
                    status = GameViewModel.ScreenStates(current = GameViewModel.GameScreenStatus.RUNNING),
                    crunch = Crunch(2000, 1, 2, Symbols.ADD),
                    input = "123"
                )
            )
        }
    }
}
        
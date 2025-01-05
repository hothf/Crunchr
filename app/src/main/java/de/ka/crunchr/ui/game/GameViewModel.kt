package de.ka.crunchr.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ka.crunchr.domain.HighScore
import de.ka.crunchr.domain.Settings
import de.ka.crunchr.domain.Sound
import de.ka.crunchr.ui.composables.SolvingResult
import de.ka.crunchrgame.models.crunch.Crunch
import de.ka.crunchrgame.CrunchrGame
import de.ka.crunchrgame.models.Listeners
import de.ka.crunchrgame.models.Savers
import de.ka.crunchrgame.models.Status
import de.ka.crunchrgame.models.Score
import de.ka.crunchrgame.models.Level
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event

    var dependencies: GameDependencies = GameDependencies()

    private var ready: Ready? = null

    private val game: CrunchrGame = CrunchrGame(viewModelScope)

    init {
        game.listener = Listeners(
            gameOverTimerUpdate = { stop, timeLeftMs, maxTimeMs ->
                viewModelScope.launch {
                    _event.emit(Event.GameTimeEvent(stop, timeLeftMs, maxTimeMs))
                }
            },
            currentCalculationTimerUpdate = { stop, timeLeftMs, maxTimeMs ->
                viewModelScope.launch {
                    _event.emit(Event.CrunchTimeEvent(stop, timeLeftMs, maxTimeMs))
                }
            },
            currentCrunchUpdate = {
                viewModelScope.launch {
                    _state.update { state -> state.copy(crunch = it, input = "") }
                }
            },
            gameStatusUpdate = {
                if (_state.value.status.current == GameScreenStatus.RUNNING && it == Status.ENDED) {
                    vibrate(true)
                    playSound(Sound.Sounds.GAME_OVER)
                    _state.value.currentScore?.let { score ->
                        viewModelScope.launch {
                            _state.update { state ->
                                state.copy(
                                    highScore = dependencies.highScoreSaver().findHighScore(score)
                                )
                            }
                        }
                    }
                }
                updateGameScreenStatusTo(GameScreenStatus.getStateForStatus(it))
            },
            solveUpdate = { result ->
                if (!result.successful) {
                    vibrate()
                    playSound(Sound.Sounds.SOLVE_FAIL)
                } else {
                    playSound(Sound.Sounds.SOLVE_SUCCESS)
                }
                viewModelScope.launch {
                    _event.emit(
                        Event.SolvingResultEvent(result.toSolvingResult(dependencies.stringResolver))
                    )
                }
            },
            currentScoreUpdate = { highScore ->
                val currentLevel = _state.value.currentScore?.level?.value
                if (currentLevel != null && currentLevel < highScore.level.value) {
                    playSound(Sound.Sounds.NEW_LEVEL)
                }
                _state.update { state -> state.copy(currentScore = highScore) }
            }
        )

        game.saver = Savers(
            onGameSave = { gameState -> dependencies.gameSaver().saveGameState(gameState) },
            onGameLoad = { dependencies.gameSaver().loadGameState() }
        )
    }

    private fun updateGameScreenStatusTo(gameScreenStatus: GameScreenStatus) {
        _state.update { state ->
            state.copy(
                status = state.status.copy(
                    previous = state.status.current,
                    current = gameScreenStatus
                )
            )
        }
    }

    fun saveSettings(vibrationChanged: Boolean = false, soundChanged: Boolean = false) {
        viewModelScope.launch {
            var settings = _state.value.settings
            if (vibrationChanged) {
                settings = settings.copy(isVibrationEnabled = !settings.isVibrationEnabled)
            }
            if (soundChanged) {
                settings = settings.copy(isSoundEnabled = !settings.isSoundEnabled)
            }
            settings.save(dependencies.settingsSaver)
            _state.update { state -> state.copy(settings = settings) }
            if (vibrationChanged) {
                vibrate()
            }
            if (soundChanged) {
                playSound(Sound.Sounds.SOLVE_SUCCESS)
            }
        }
    }

    fun openLevelSelect() {
        updateGameScreenStatusTo(GameScreenStatus.CHOOSE_LEVEL)
    }

    fun openSettings() {
        updateGameScreenStatusTo(GameScreenStatus.SETTINGS)
    }

    fun back() {
        _state.value.status.previous?.let(::updateGameScreenStatusTo)
    }

    fun quit() {
        game.quit()
    }

    /**
     * Handle back press consumption. Returns `true` if back press is consumed by this logic.
     * Return `false` to let the caller handle the back press
     */
    fun consumeBack(): Boolean {
        return when (_state.value.status.current) {
            GameScreenStatus.RUNNING -> {
                pause()
                true
            }

            GameScreenStatus.SETTINGS, GameScreenStatus.CHOOSE_LEVEL -> {
                back()
                true
            }

            GameScreenStatus.PAUSED -> {
                startReady(resume = true, skipReady = false)
                true
            }

            GameScreenStatus.NOT_LOADED,
            GameScreenStatus.NOT_STARTED,
            GameScreenStatus.GET_READY,
            GameScreenStatus.ENDED -> false
        }
    }

    fun loadLastGameAndSettings() {
        viewModelScope.launch {
            updateGameScreenStatusTo(GameScreenStatus.NOT_LOADED)

            // 1. load last settings
            val settings = Settings.load(dependencies.settingsSaver) ?: Settings()
            _state.update { state -> state.copy(settings = settings) }

            // 2. prepare sounds
            dependencies.sound.prepare()

            // 3. load last game to notify status loaded
            game.loadLast()
        }
    }

    fun release() {
        game.release()

        dependencies.sound.release()
    }

    fun pause() {
        game.pause()
    }

    fun forfeit() {
        game.forfeit()
    }

    fun startReady(resume: Boolean, skipReady: Boolean, level: Level? = null) {
        ready = Ready(resume = resume, level = level)

        if (skipReady) {
            game.solve(null)
        } else {
            updateGameScreenStatusTo(GameScreenStatus.GET_READY)
        }
    }

    fun onReady() {
        val isReady = ready ?: return
        if (isReady.resume) {
            game.resume()
        } else {
            viewModelScope.launch {
                _event.emit(Event.ResetEvent)

                if (isReady.level != null) {
                    dependencies.settingsSaver().setChosenLevel(isReady.level)
                }
                game.startNew(dependencies.settingsSaver().getChosenLevel())
            }
        }
    }

    fun solve() {
        try {
            val solvingInput = _state.value.input.toFloat()
            game.solve(solvingInput)
        } catch (ex: NumberFormatException) {
            Log.i("Solving", "Can't solve for ${_state.value.input}")
        }
    }

    fun clear() {
        _state.update { state -> state.copy(input = "") }
    }

    fun newCrunch() {
        game.newCrunch()
    }

    fun updateInput(input: String) {
        if (_state.value.status.current != GameScreenStatus.RUNNING || (input == "." && _state.value.input.contains(
                "."
            ))
            || _state.value.input.length >= 7
        ) return
        _state.update { state -> state.copy(input = _state.value.input + input) }
    }

    private fun vibrate(long: Boolean = false) {
        _state.value.settings.let {
            if (it.isVibrationEnabled) {
                dependencies.vibrator.vibrate(long)
            }
        }
    }

    private fun playSound(sound: Sound.Sounds) {
        _state.value.settings.let {
            if (it.isSoundEnabled) {
                dependencies.sound.play(sound)
            }
        }
    }

    data class UiState(
        val status: ScreenStates = ScreenStates(current = GameScreenStatus.NOT_LOADED),
        val crunch: Crunch? = null,
        val currentScore: Score? = null,
        val highScore: HighScore? = null,
        val input: String = "",
        val settings: Settings = Settings()
    )

    data class ScreenStates(val previous: GameScreenStatus? = null, val current: GameScreenStatus)

    enum class GameScreenStatus {
        NOT_LOADED,
        NOT_STARTED,
        GET_READY,
        RUNNING,
        PAUSED,
        ENDED,
        CHOOSE_LEVEL,
        SETTINGS;

        companion object {
            fun getStateForStatus(status: Status): GameScreenStatus {
                return when (status) {
                    Status.ENDED -> ENDED
                    Status.PAUSED -> PAUSED
                    Status.RUNNING -> RUNNING
                    Status.NOT_STARTED -> NOT_STARTED
                }
            }
        }
    }

    sealed class Event {
        open class TimeEvent(leftMs: Long, timeMs: Long) : Event() {
            val percentage = if (timeMs <= 0L) 1f else leftMs.toFloat() / timeMs.toFloat()
        }

        data class CrunchTimeEvent(val stopped: Boolean, val timeMs: Long, val overallMs: Long) :
            TimeEvent(timeMs, overallMs)

        data class GameTimeEvent(val stopped: Boolean, val timeMs: Long, val overallMs: Long) :
            TimeEvent(timeMs, overallMs)

        data object ResetEvent : Event()

        data class SolvingResultEvent(val result: SolvingResult) : Event()
    }

    private data class Ready(val resume: Boolean, val level: Level? = null)
}
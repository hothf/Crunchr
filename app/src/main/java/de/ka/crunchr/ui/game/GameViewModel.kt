package de.ka.crunchr.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ka.crunchr.domain.AppGameSaver
import de.ka.crunchrgame.Crunch
import de.ka.crunchrgame.CrunchrGame
import de.ka.crunchrgame.GameStatus
import de.ka.crunchrgame.HighScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.lang.NumberFormatException

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    var saver: () -> AppGameSaver? = { null }

    private val game: CrunchrGame = CrunchrGame()

    init {
        game.gameOverTimerUpdate = { timeLeftMs, maxTimeMs ->
            _state.update { state ->
                state.copy(
                    gameOverTimeLeft = timeLeftMs.toString(),
                    gameOverTime = maxTimeMs.toString()
                )
            }
        }

        game.currentCalculationTimerUpdate = { timeLeftMs, maxTimeMs ->
            _state.update { state ->
                state.copy(
                    crunchTimeLeft = timeLeftMs.toString(),
                    crunchTime = maxTimeMs.toString()
                )
            }
        }

        game.currentCrunchUpdate = {
            _state.update { state -> state.copy(crunch = it, input = "") }
        }

        game.gameEndUpdate = {
            _state.update { state -> state.copy(highScore = it) }
        }

        game.gameStatusUpdate = {
            _state.update { state -> state.copy(state = it) }
        }

        game.solveUpdate = { _, solvedCrunchCount, score ->
            _state.update { state ->
                state.copy(
                    crunchesSolved = solvedCrunchCount,
                    currentScore = score
                )
            }
        }

        game.onGameSave = { gameState ->
            Log.i("CrunchrApp - Saving", "Saved game state $gameState")
            saver()?.saveGameState(gameState)
        }

        game.onGameLoad = {
            val gameState = saver()?.loadGameState()
            Log.i("CrunchrApp - Loading", "Loaded game state $gameState")
            gameState
        }
    }

    fun start() {
        game.startNew(viewModelScope)
    }

    fun pause() {
        game.pause(viewModelScope)
    }

    fun quit() {
        game.quit()
    }

    fun resume() {
        game.resume(viewModelScope)
    }

    fun solve() {
        try {
            val solvingInput = _state.value.input.toFloat()
            game.solve(viewModelScope, solvingInput)
        } catch (ex: NumberFormatException) {
            Log.i("Solving", "Can't solve for ${_state.value.input}")
        }
    }

    fun updateInput(input: String) {
        _state.update { state -> state.copy(input = _state.value.input + input) }
    }

    data class UiState(
        val gameOverTimeLeft: String = "",
        val gameOverTime: String = "",
        val crunchTimeLeft: String = "",
        val crunchTime: String = "",
        val crunch: Crunch? = null,
        val state: GameStatus = GameStatus.NOT_STARTED,
        val currentScore: Long = 0L,
        val crunchesSolved: Int = 0,
        val highScore: HighScore? = null,
        val input: String = "",
    )
}
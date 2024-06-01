package de.ka.crunchr.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ka.crunchrgame.Crunch
import de.ka.crunchrgame.CrunchrGame
import de.ka.crunchrgame.GameStatus
import de.ka.crunchrgame.HighScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

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
            _state.update { state -> state.copy(crunch = it) }
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
    }

    fun start() {
        game.startNew(viewModelScope)
    }

    fun pause() {
        game.pause()
    }

    fun quit() {
        game.quit()
    }

    fun resume() {
        game.resume(viewModelScope)
    }

    fun solve(input: Float) {
        game.solve(viewModelScope, input)
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
        val highScore: HighScore? = null
    )
}
package de.ka.crunchrgame

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class CrunchrGame {

    // game vars
    private var gameState = GameState()

    // update listeners
    var gameOverTimerUpdate: (timeLeftMs: Long, maxTimeMs: Long) -> Unit = { _, _ -> }
    var currentCalculationTimerUpdate: (timeLeftMs: Long, maxTimeMs: Long) -> Unit = { _, _ -> }
    var currentCrunchUpdate: (crunch: Crunch) -> Unit = { _ -> }
    var gameEndUpdate: (highScore: HighScore) -> Unit = { _ -> }
    var gameStatusUpdate: (state: GameStatus) -> Unit = { _ -> }
    var solveUpdate: (success: Boolean, solvedCrunchCount: Int, score: Long) -> Unit =
        { _, _, _ -> }

    // internals
    private var gameOverTimerJob: Job? = null
    private var currentCalculationTimerJob: Job? = null

    fun startNew(scope: CoroutineScope) {
        gameState = GameState(status = GameStatus.RUNNING)
        gameStatusUpdate(gameState.status)
        solveUpdate(false, 0, 0)

        startGameOverTimer(scope)
        startCrunchTimer(scope)
    }

    fun pause() {
        gameState = gameState.copy(status = GameStatus.PAUSED)
        gameStatusUpdate(gameState.status)

        gameOverTimerJob?.cancel()
        currentCalculationTimerJob?.cancel()
    }

    fun resume(scope: CoroutineScope) {
        gameState = gameState.copy(status = GameStatus.RUNNING)
        gameStatusUpdate(gameState.status)

        startGameOverTimer(scope)
        startCrunchTimer(scope, createNew = false)
    }

    fun quit() {
        gameState = gameState.copy(status = GameStatus.ENDED)
        gameStatusUpdate(gameState.status)
        gameOverTimerJob?.cancel()
        currentCalculationTimerJob?.cancel()
    }

    fun solve(scope: CoroutineScope, input: Float) {
        gameState.currentCrunch?.let {
            val success = it.solve(input)
            gameState = if (success) {
                gameState.copy(
                    gameOverTimeMs = gameState.gameOverTimeMs + gameState.currentLevel.successTimeBonusMs,
                    currentSolvedCrunchCount = gameState.currentSolvedCrunchCount + 1,
                    currentScore = gameState.currentScore + gameState.currentLevel.pointsPerSuccess
                )
            } else {
                gameState.copy(
                    gameOverTimeMs = gameState.gameOverTimeMs - (gameState.currentLevel.successTimeBonusMs / 2),
                )
            }
            startGameOverTimer(scope)
            startCrunchTimer(scope)

            solveUpdate(success, gameState.currentSolvedCrunchCount, gameState.currentScore)
        }
    }

    fun save() {


    }

    fun load() {

    }

    private fun startGameOverTimer(scope: CoroutineScope) {
        gameOverTimerJob?.cancel()
        gameOverTimerJob = scope.ticker(
            maxDurationMs = gameState.gameOverTimeMs,
            startDurationMs = gameState.gameOverElapsedTimeMs,
            tickUpdate = { timeElapsedMs, timeLeftMs ->
                gameState = gameState.copy(
                    gameOverElapsedTimeMs = timeElapsedMs
                )
                gameOverTimerUpdate(timeLeftMs, gameState.gameOverTimeMs)
            },
            end = { gameOver() }
        )
    }

    private fun startCrunchTimer(scope: CoroutineScope, createNew: Boolean = true) {
        if (createNew) {
            gameState = gameState.copy(
                currentCrunch = Crunch(gameState.currentLevel),
                currentCrunchElapsedTimeMs = 0L
            )
        }
        gameState.currentCrunch?.let(currentCrunchUpdate)
        currentCalculationTimerJob?.cancel()
        currentCalculationTimerJob = scope.ticker(
            maxDurationMs = gameState.currentLevel.timePerCalculationMs,
            startDurationMs = gameState.currentCrunchElapsedTimeMs,
            tickUpdate = { timeElapsedMs, timeLeftMs ->
                gameState = gameState.copy(
                    currentCrunchElapsedTimeMs = timeElapsedMs,
                )
                currentCalculationTimerUpdate(
                    timeLeftMs,
                    gameState.currentLevel.timePerCalculationMs
                )
            },
            end = { startCrunchTimer(scope) }
        )
    }

    private fun gameOver() {
        gameState = gameState.copy(status = GameStatus.ENDED)
        gameStatusUpdate(gameState.status)
        gameEndUpdate(
            HighScore(
                elapsedTimeMs = gameState.gameOverElapsedTimeMs,
                score = gameState.currentScore,
                crunchCount = gameState.currentSolvedCrunchCount
            )
        )
        gameOverTimerJob?.cancel()
        currentCalculationTimerJob?.cancel()
    }
}
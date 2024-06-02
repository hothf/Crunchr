package de.ka.crunchrgame

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A game which lets users [solve] [puzzles called Crunches][Crunch].
 *
 * There is a limited time frame to solve a crunch. A overall timer is started on [startNew] and
 * it's end announces that the game is over. By solving crunches the user can extend or reduce the
 * overall timer.
 *
 * Each solved crunch can lead to increased [Level]s which reward ever increasing points.
 */
class CrunchrGame {

    // public update listeners
    var gameOverTimerUpdate: (timeLeftMs: Long, maxTimeMs: Long) -> Unit = { _, _ -> }
    var currentCalculationTimerUpdate: (timeLeftMs: Long, maxTimeMs: Long) -> Unit = { _, _ -> }
    var currentCrunchUpdate: (crunch: Crunch) -> Unit = { _ -> }
    var gameEndUpdate: (highScore: HighScore) -> Unit = { _ -> }
    var gameStatusUpdate: (state: GameStatus) -> Unit = { _ -> }
    var solveUpdate: (result: CrunchResult, count: Int, score: Long) -> Unit = { _, _, _ -> }
    var onGameSave: suspend (gameState: GameState) -> Unit = { }
    var onGameLoad: suspend () -> GameState? = { null }

    // private game vars
    private var gameState = GameState()

    // absolutely private internals
    private var gameOverTimerJob: Job? = null
    private var currentCalculationTimerJob: Job? = null

    /**
     * Starts a new game.
     */
    fun startNew(scope: CoroutineScope) {
        gameState = GameState(status = GameStatus.RUNNING)
        gameStatusUpdate(gameState.status)

        startGameOverTimer(scope)
        startCrunchTimer(scope)
    }

    /**
     * Pauses the game.
     */
    fun pause(scope: CoroutineScope) {
        if (gameState.status != GameStatus.RUNNING) return
        scope.launch {
            gameState = gameState.copy(status = GameStatus.PAUSED)
            gameStatusUpdate(gameState.status)

            gameOverTimerJob?.cancel()
            currentCalculationTimerJob?.cancel()

            onGameSave(gameState)
        }
    }

    /**
     * Resumes the game, starting from the last known [gameState].
     */
    fun resume(scope: CoroutineScope) {
        scope.launch {
            onGameLoad()?.let { gameState = it }

            if (gameState.status != GameStatus.PAUSED) return@launch

            gameState = gameState.copy(status = GameStatus.RUNNING)
            gameStatusUpdate(gameState.status)

            startGameOverTimer(scope)
            startCrunchTimer(scope, createNew = false)
        }
    }

    /**
     * Quits the game.
     */
    fun quit() {
        gameState = gameState.copy(status = GameStatus.ENDED)
        gameStatusUpdate(gameState.status)
        gameOverTimerJob?.cancel()
        currentCalculationTimerJob?.cancel()
    }

    /**
     * Solves the current [Crunch] with the given [input]. If successful, game time is increased,
     * otherwise reduced.
     */
    fun solve(scope: CoroutineScope, input: Float) {
        if (gameState.status != GameStatus.RUNNING) return
        gameState.currentCrunch?.let {
            val success = it.solve(input)
            val neededTime = gameState.currentCrunchElapsedTimeMs
            gameState = if (success) {
                gameState.copy(
                    gameOverTimeMs = gameState.gameOverTimeMs + gameState.currentLevel.successTimeBonusMs,
                    currentSolvedCrunchCount = gameState.currentSolvedCrunchCount + 1
                )
            } else {
                gameState.copy(
                    gameOverTimeMs = gameState.gameOverTimeMs - (gameState.currentLevel.successTimeBonusMs / 2),
                )
            }
            startGameOverTimer(scope)
            startCrunchTimer(scope)

            val result = CrunchResult(success, gameState.currentLevel, neededTime)
            gameState = gameState.copy(currentScore = gameState.currentScore + result.score)
            solveUpdate(result, gameState.currentSolvedCrunchCount, gameState.currentScore)
        }
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
                currentCrunch = Crunch.createNew(gameState.currentLevel),
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
            end = {
                failCrunch()
                startCrunchTimer(scope)
            }
        )
    }

    private fun failCrunch() {
        val result = CrunchResult(false, gameState.currentLevel, 0)
        solveUpdate(result, gameState.currentSolvedCrunchCount, gameState.currentScore)
    }

    private fun gameOver() {
        failCrunch()
        val result = CrunchResult(false, gameState.currentLevel, 0)
        solveUpdate(result, gameState.currentSolvedCrunchCount, gameState.currentScore)
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
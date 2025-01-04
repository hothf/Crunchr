package de.ka.crunchrgame

import de.ka.crunchrgame.models.crunch.Result
import de.ka.crunchrgame.models.Listeners
import de.ka.crunchrgame.models.Savers
import de.ka.crunchrgame.models.Score
import de.ka.crunchrgame.models.State
import de.ka.crunchrgame.models.Status
import de.ka.crunchrgame.models.Level
import de.ka.crunchrgame.utils.launchTicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A game which lets users [solve] puzzles called 'Crunches'.
 *
 * There is a limited time frame to solve a crunch. A overall timer is started on [startNew] and
 * it's end announces that the game is over. By solving crunches the user can extend the timer or
 * reduce the overall score.
 *
 * Each solved crunch can lead to increased [Level]s which reward ever increasing points.
 */
class CrunchrGame(private var coroutineScope: CoroutineScope?) {

    // public update listeners
    var listener = Listeners()

    // public game persistence
    var saver = Savers()

    // the current game state
    private var state = State()

    // private internal coroutine jobs
    private var gameOverTimerJob: Job? = null
    private var currentCalculationTimerJob: Job? = null

    /**
     * Starts a new game.
     */
    fun startNew(level: Level = Level()) {
        state = State(status = Status.RUNNING, startLevel = level)

        notifyCurrentScore()
        notifyGameStateStatus()

        notifyGameOverTimer(stop = true)
        notifyCrunchTimer(stop = true)

        startGameOverTimer()
        startCrunchTimer()
    }

    /**
     * Loads and checks the last game if possible.
     */
    fun loadLast() {
        coroutineScope?.launch {
            val lastGameIncomplete = loadLastGameAndUpdateIncomplete()

            if (lastGameIncomplete) {
                notifyGameOverTimer(stop = true)
                notifyCrunchTimer(stop = true)
                notifyCurrentCrunch()
                notifyCurrentScore()
            } else {
                state = state.copy(status = Status.NOT_STARTED)
            }
            notifyGameStateStatus()
        }
    }

    /**
     * Pauses the game.
     */
    fun pause() {
        if (state.status != Status.RUNNING) return

        state = state.copy(status = Status.PAUSED)

        notifyGameStateStatus()
        notifyCurrentScore()

        gameOverTimerJob?.cancel()
        notifyGameOverTimer(stop = true)
        currentCalculationTimerJob?.cancel()
        notifyCrunchTimer(stop = true)

        coroutineScope?.launch { saver.onGameSave(state) }
    }

    /**
     * Resumes the game, starting from the last known [state].
     */
    fun resume() {
        state = state.copy(status = Status.RUNNING)
        notifyGameStateStatus()

        solve(null)
    }

    /**
     * Forces starting of a new crunch, if the game is running.
     */
    fun newCrunch() {
        if (state.status != Status.RUNNING) return

        failCrunch(true)
    }

    /**
     * Forfeits the game.
     */
    fun forfeit() {
        state = state.copy(status = Status.ENDED)
        notifyGameStateStatus()

        gameOverTimerJob?.cancel()
        notifyGameOverTimer(stop = true)
        currentCalculationTimerJob?.cancel()
        notifyCrunchTimer(stop = true)
    }

    /**
     * Quits the game and resets to the not started status.
     */
    fun quit() {
        state = state.copy(status = Status.NOT_STARTED)
        notifyGameStateStatus()

        gameOverTimerJob?.cancel()
        notifyGameOverTimer(stop = true)
        currentCalculationTimerJob?.cancel()
        notifyCrunchTimer(stop = true)
    }

    /**
     * Releases game resources.
     * You can no longer safely call any methods after this point before re-calling the constructor.
     */
    fun release() {
        coroutineScope = null
    }

    /**
     * Solves the current crunch with the given [input]. If successful, game time is increased,
     * otherwise reduced. Awards success with points and levels.
     */
    fun solve(input: Float? = null) {
        if (state.status != Status.RUNNING) return

        state.currentCrunch?.let {
            val result = Rules.determineCrunchResult(
                input = input,
                expected = it.expected,
                neededTimeMs = state.currentCrunchElapsedTimeMs,
                lastSolvingTimeMs = state.lastCrunchSolvedTimeMs,
                streakCount = state.streakCount,
                currentLevel = state.currentLevel
            )

            val newScore = (state.currentScore + result.finalPoints).coerceIn(Rules.MAX_POINTS)

            state = state.copy(
                gameOverTimeMs = Rules.determineTimeUpdate(
                    success = result.successful,
                    gameOverTimeMs = state.gameOverTimeMs,
                    gameOverElapsedTimeMs = state.gameOverElapsedTimeMs,
                    level = state.currentLevel,
                ),
                currentScore = newScore,
                currentLevel = Rules.determineLevel(state.startLevel, newScore),
                lastCrunchSolvedTimeMs = result.solvingTimeMs,
                overallCrunchCount = state.overallCrunchCount + 1,
                streakCount = result.streakCount,
                bestStreakCount = if (state.bestStreakCount < result.streakCount) result.streakCount else state.bestStreakCount,
                currentSolvedCrunchCount = if (result.successful) state.currentSolvedCrunchCount + 1 else state.currentSolvedCrunchCount,
            )
            notifyCurrentSolve(result)
            notifyCurrentScore()
        }
        startGameOverTimer()
        startCrunchTimer()
    }

    private suspend fun loadLastGameAndUpdateIncomplete(): Boolean {
        saver.onGameLoad()?.let { state = it }
        return state.status != Status.ENDED
    }

    private fun startGameOverTimer() {
        gameOverTimerJob?.cancel()
        notifyGameOverTimer()

        gameOverTimerJob = coroutineScope?.launchTicker(
            maxDurationMs = state.gameOverTimeMs,
            startDurationMs = state.gameOverElapsedTimeMs,
            tickUpdate = { timeElapsedMs, _ ->
                state = state.copy(gameOverElapsedTimeMs = timeElapsedMs)
            },
            end = { gameOver() }
        )
    }

    private fun startCrunchTimer(createNew: Boolean = true) {
        if (createNew) {
            state = state.copy(
                currentCrunch = Rules.determineNewCrunch(state.currentLevel),
                currentCrunchElapsedTimeMs = 0L
            )
        }
        val currentCrunch = state.currentCrunch ?: return

        notifyCurrentCrunch()
        notifyCrunchTimer()

        currentCalculationTimerJob?.cancel()
        currentCalculationTimerJob = coroutineScope?.launchTicker(
            maxDurationMs = currentCrunch.crunchTimeMs.toLong(),
            startDurationMs = state.currentCrunchElapsedTimeMs,
            tickUpdate = { timeElapsedMs, _ ->
                state = state.copy(currentCrunchElapsedTimeMs = timeElapsedMs)
            },
            end = {
                failCrunch()
                startCrunchTimer()
            }
        )
    }

    private fun gameOver() {
        state = state.copy(status = Status.ENDED)
        failCrunch()
        notifyGameStateStatus()

        gameOverTimerJob?.cancel()
        notifyGameOverTimer(stop = true)
        currentCalculationTimerJob?.cancel()
        notifyCrunchTimer(stop = true)

        coroutineScope?.launch { saver.onGameSave(state) }
    }

    private fun failCrunch(startNew: Boolean = false) {
        val crunch = state.currentCrunch
        crunch?.let {
            val result = Rules.determineCrunchResult(expected = crunch.expected)
            state = state.copy(
                lastCrunchSolvedTimeMs = null,
                overallCrunchCount = state.overallCrunchCount + 1,
                streakCount = result.streakCount,
                currentSolvedCrunchCount = state.currentSolvedCrunchCount
            )
            notifyCurrentSolve(result)
            notifyCurrentScore()
        }
        if (startNew) {
            startCrunchTimer(true)
        }
    }

    private fun notifyGameOverTimer(stop: Boolean = false) {
        val leftMs = state.gameOverTimeMs - state.gameOverElapsedTimeMs
        listener.gameOverTimerUpdate(stop, if (leftMs < 0) 0 else leftMs, Rules.MAX_TIME_MS)
    }

    private fun notifyCrunchTimer(stop: Boolean = false) {
        val timeMs = state.currentCrunch?.crunchTimeMs?.toLong() ?: return
        val leftMs = timeMs - state.currentCrunchElapsedTimeMs
        listener.currentCalculationTimerUpdate(stop, if (leftMs < 0) 0 else leftMs, timeMs)
    }

    private fun notifyGameStateStatus() {
        listener.gameStatusUpdate(state.status)
    }

    private fun notifyCurrentScore() {
        listener.currentScoreUpdate(
            Score(
                elapsedTimeMs = state.gameOverElapsedTimeMs,
                score = state.currentScore,
                overallCrunchCount = state.overallCrunchCount,
                crunchCount = state.currentSolvedCrunchCount,
                level = state.currentLevel,
                bestStreakCount = state.bestStreakCount
            )
        )
    }

    private fun notifyCurrentCrunch() {
        state.currentCrunch?.let(listener.currentCrunchUpdate)
    }

    private fun notifyCurrentSolve(result: Result) {
        listener.solveUpdate(result)
    }
}
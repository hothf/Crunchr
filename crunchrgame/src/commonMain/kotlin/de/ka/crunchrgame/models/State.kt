package de.ka.crunchrgame.models

import de.ka.crunchrgame.Rules
import de.ka.crunchrgame.models.crunch.Crunch

/**
 * The game stane contains all [CrunchrGame] relevant game variables.
 */
data class State(
    val gameOverTimeMs: Long = Rules.MAX_TIME_MS,
    val gameOverElapsedTimeMs: Long = 0,
    val currentCrunchElapsedTimeMs: Long = 0,
    val startLevel: Level = Level(),
    val currentLevel: Level = startLevel,
    val currentCrunch: Crunch? = null,
    val currentScore: Long = 0L,
    val currentSolvedCrunchCount: Int = 0,
    val overallCrunchCount: Int = 0,
    val status: Status = Status.NOT_STARTED,
    val lastCrunchSolvedTimeMs: Long? = null,
    val streakCount: Int = 0,
    val bestStreakCount: Int = 0
)

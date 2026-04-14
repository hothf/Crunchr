package de.ka.crunchr.domain

import de.ka.crunchrgame.models.Level

/**
 * Represents a high score of a game. A `null` value means that there has been no high score so far
 * for that value.
 */
class HighScore(
    val score: Long? = null,
    val streakCount: Int? = null,
    val solveCount: Int? = null,
    val level: Level? = null,
    val averageTimeMs: Long? = null,
    val time: Long? = null
)
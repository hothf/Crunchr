package de.ka.crunchr.domain

import de.ka.crunchrgame.models.Score

/**
 * Can load, find and save HighScores.
 */
fun interface HighScoreSaver {

    /**
     * Finds the current HighScore by comparing it with the newest [Score].
     */
    suspend fun findHighScore(score: Score): HighScore
}

class StubHighScoreSaver: HighScoreSaver {

    override suspend fun findHighScore(score: Score): HighScore {
        return HighScore()
    }
}
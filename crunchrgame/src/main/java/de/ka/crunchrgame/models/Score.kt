package de.ka.crunchrgame.models

/**
 * Represents the score of a game.
 */
data class Score(
    val elapsedTimeMs: Long,
    val score: Long,
    val overallCrunchCount: Int,
    val bestStreakCount: Int,
    val crunchCount: Int,
    val level: Level
) {

    /**
     * Retrieves the average solving time, if there was any solving before. Otherwise
     * just returns the complete time.
     */
    fun getAverageSolvingTimeMs(): Long {
        if (crunchCount <= 0) {
            return elapsedTimeMs
        }
        return  elapsedTimeMs / crunchCount
    }

    companion object {

        /**
         * Offers convenience for giving preview values of a game score.
         */
        fun preview() = Score(
            elapsedTimeMs = 100000,
            score = 1000,
            overallCrunchCount = 42,
            crunchCount = 20,
            bestStreakCount = 5,
            level = Level(value = 1)
        )
    }
}
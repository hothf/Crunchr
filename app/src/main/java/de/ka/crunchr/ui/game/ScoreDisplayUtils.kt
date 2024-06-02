package de.ka.crunchr.ui.game

import de.ka.crunchr.ui.composables.Score
import de.ka.crunchrgame.CrunchResult
import java.text.DecimalFormat

object ScoreDisplayUtils {

    /**
     * Creates a score for the given [result].
     */
    fun createScoreFor(result: CrunchResult): Score? {
        val performance = when {
            result.multiplier < 1.0f -> "Correct"
            result.multiplier < 1.25 -> "Okay"
            result.multiplier < 1.5f -> "Great!"
            else -> "Perfect"
        }
        val multiplierDisplay = if (result.multiplier >= 1f) "Multiplier: ${result.points} * ${
            DecimalFormat("#.00").format(result.multiplier)
        }" else null
        return Score(
            successful = result.successful,
            performance = performance,
            score = result.score.toString(),
            multiplier = multiplierDisplay
        )
    }
}
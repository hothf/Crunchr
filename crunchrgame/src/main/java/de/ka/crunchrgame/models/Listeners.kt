package de.ka.crunchrgame.models

import de.ka.crunchrgame.models.crunch.Crunch
import de.ka.crunchrgame.models.crunch.Result

data class Listeners(
    val gameOverTimerUpdate: (stop: Boolean, timeLeftMs: Long, maxTimeMs: Long) -> Unit =
        { _, _, _ -> },
    val currentCalculationTimerUpdate: (stop: Boolean, timeLeftMs: Long, maxTimeMs: Long) -> Unit =
        { _, _, _ -> },
    val currentCrunchUpdate: (crunch: Crunch) -> Unit = { _ -> },
    val gameStatusUpdate: (state: Status) -> Unit = { _ -> },
    val currentScoreUpdate: (score: Score) -> Unit = { _ -> },
    val solveUpdate: (result: Result) -> Unit = { _ -> }
)
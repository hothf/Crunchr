package de.ka.crunchrgame.models.crunch

class Result(
    val successful: Boolean,
    val actual: Float? = null,
    val expected: Float,
    val streakCount: Int,
    val multiplier: Float,
    val finalPoints: Long,
    val solvingTimeMs: Long?
)
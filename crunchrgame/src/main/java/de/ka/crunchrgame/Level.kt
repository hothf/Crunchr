package de.ka.crunchrgame

data class Level(
    val value: Int = 1,
    val timePerCalculationMs: Long = 10_000,
    val successTimeBonusMs: Long = 5_000,
    val pointsPerSuccess: Long = 100L
)
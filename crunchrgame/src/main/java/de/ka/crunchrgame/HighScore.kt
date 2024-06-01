package de.ka.crunchrgame

data class HighScore(
    val elapsedTimeMs: Long,
    val score: Long,
    val crunchCount: Int
)
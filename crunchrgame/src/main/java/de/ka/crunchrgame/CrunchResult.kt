package de.ka.crunchrgame

class CrunchResult(
    val successful: Boolean,
    level: Level,
    solvedTimeMs: Long
) {
    val multiplier = when {
        solvedTimeMs < 1500L -> 1.5f
        solvedTimeMs <= 2500L -> 1.25f
        solvedTimeMs <= 4000L -> 1f
        else -> 0.75f
    }
    val points = if (successful) level.pointsPerSuccess else 0L
    val score = (points * multiplier).toLong()
}
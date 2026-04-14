package de.ka.crunchrgame

import de.ka.crunchrgame.models.crunch.Crunch
import de.ka.crunchrgame.models.crunch.Result
import de.ka.crunchrgame.models.crunch.Symbols
import de.ka.crunchrgame.models.Level
import kotlin.math.abs
import kotlin.math.min
import kotlin.random.Random

/**
 * Offers all rules for the [CrunchrGame].
 */
internal object Rules {

    const val MAX_TIME_MS = 20_000L
    val MAX_POINTS = 0L..999999L

    private const val OKAY_DELTA: Float = 0.1f

    /**
     * Creates a new [Crunch] for the given level.
     */
    fun determineNewCrunch(level: Level): Crunch {
        var maxNum = 10
        var minNum = 1
        if (level.value >= 5) {
            minNum = -10
        }
        if (level.value >= 10) {
            maxNum = 15
        }
        if (level.value >= 15) {
            minNum = -15
        }
        val symbol = with(level.symbols) {
            this[Random.nextInt(0, this.size)]
        }
        val first = Random.nextInt(minNum, maxNum).takeUnless { it == 0 } ?: 1
        val second = Random.nextInt(minNum, maxNum).takeUnless { it == 0 } ?: 1

        val timeMs = 10_000

        return Crunch(
            firstNum = first,
            secondNum = second,
            symbol = symbol,
            crunchTimeMs = timeMs
        )
    }

    /**
     * Determines the [result][Result] of a crunch.
     *
     * If you do not provide an input, will determine a failed attempt.
     */
    fun determineCrunchResult(
        crunch: Crunch,
        input: Float? = null,
        neededTimeMs: Long? = null,
        lastSolvingTimeMs: Long? = null,
        streakCount: Int = 0,
        currentLevel: Level = Level()
    ): Result {
        val success = input != null && abs(crunch.expected - input) <= OKAY_DELTA

        var count = streakCount
        if (neededTimeMs != null && lastSolvingTimeMs != null && lastSolvingTimeMs < 4000 && neededTimeMs < 4000) {
            count++
        } else {
            count = 0
        }

        if (!success || neededTimeMs == null) {
            return Result(false, input, crunch, count, 0f, -250, null)
        }

        val timeMultiplier = when {
            neededTimeMs < 1500L -> 1.5f
            neededTimeMs <= 2500L -> 1.25f
            neededTimeMs <= 4000L -> 1f
            else -> 0.75f
        }

        val streakMultiplier = when {
            streakCount <= 2 -> 1f
            streakCount <= 5 -> 1.5f
            streakCount <= 10 -> 1.8f
            streakCount <= 20 -> 2.0f
            streakCount < 25 -> 2.5f
            else -> 1f
        }

        val multiplier = timeMultiplier * streakMultiplier

        val pointsWithoutModifier = 100L + 5 * currentLevel.value
        val finalPoints = (pointsWithoutModifier * multiplier).toLong()

        return Result(
            true,
            input,
            crunch,
            count,
            multiplier,
            finalPoints,
            neededTimeMs
        )
    }

    /**
     * Determines the current level for the given [score], starting with [startLevel].
     */
    fun determineLevel(startLevel: Level, score: Long): Level {
        val aLevel = (score / 400).toInt()
        return Level(value = startLevel.value + (score / (400 + aLevel * 10)).toInt())
    }

    /**
     * Determines the symbols for the given [Level][levelValue].
     */
    fun determineSymbols(levelValue: Int): List<Symbols> {
        val symbols = mutableListOf(Symbols.ADD, Symbols.MINUS)
        if (levelValue >= 10) {
            symbols.add(Symbols.MULTIPLY)
        }
        if (levelValue >= 20) {
            symbols.add(Symbols.DIVIDE)
        }
        return symbols
    }

    /**
     * Determines the time awarded for a successful or unsuccessful solving for the given [Level].
     */
    fun determineTimeUpdate(
        success: Boolean,
        gameOverTimeMs: Long,
        gameOverElapsedTimeMs: Long,
        level: Level,
    ): Long {
        return if (success) {
            val maxLevel = min(level.value, 200)
            val successTimeMs = gameOverTimeMs + (10_000 - (maxLevel * 50))
            min(successTimeMs, gameOverElapsedTimeMs + MAX_TIME_MS)
        } else {
            gameOverTimeMs
        }
    }
}
package de.ka.crunchrgame

import de.ka.crunchrgame.models.Level
import org.junit.Assert
import org.junit.Test

class RuleTest {

    @Test
    fun testSymbols() {
        // given, when
        val symbols = Rules.determineSymbols(levelValue = 1)

        // then
        Assert.assertTrue(symbols.isNotEmpty())
    }

    @Test
    fun testCrunch() {
        // given, when
        val crunch1 = Rules.determineNewCrunch(level = Level(1))
        val crunch2 = Rules.determineNewCrunch(level = Level(100))

        // then
        Assert.assertTrue(crunch1.crunchTimeMs != crunch2.crunchTimeMs)
    }

    @Test
    fun testCrunchResult() {
        // given, when
        val crunch1 = Rules.determineNewCrunch(level = Level(1))
        val result1 =
            Rules.determineCrunchResult(
                input = crunch1.expected,
                expected = crunch1.expected,
                neededTimeMs = 100
            )

        // then
        Assert.assertTrue(result1.successful)

        // given, when
        val crunch2 = Rules.determineNewCrunch(level = Level(1))
        val result2 =
            Rules.determineCrunchResult(input = crunch2.expected + 1, expected = crunch2.expected)

        // then
        Assert.assertFalse(result2.successful)
    }

    @Test
    fun testLevel() {
        // given, when
        val level1 = Rules.determineLevel(startLevel = Level(1), score = 1000)

        // then
        Assert.assertEquals(2, level1.value)

        // given, when
        val level2 = Rules.determineLevel(startLevel = level1, score = 1000)

        // then
        Assert.assertEquals(level1.value + 1, level2.value)

        // given, when
        val level3 = Rules.determineLevel(startLevel = level2, score = 900)

        // then
        Assert.assertEquals(level2.value, level3.value)
    }

    @Test
    fun testTimeUpdate() {
        // given, when
        val neededTime = Rules.MAX_TIME_MS / 2
        val timeUpdate1 = Rules.determineTimeUpdate(
            success = true,
            gameOverTimeMs = Rules.MAX_TIME_MS,
            gameOverElapsedTimeMs = neededTime,
            level = Level()
        )

        // then
        Assert.assertTrue(timeUpdate1 > Rules.MAX_TIME_MS)

        // given, when
        val timeUpdate2 = Rules.determineTimeUpdate(
            success = false,
            gameOverTimeMs = Rules.MAX_TIME_MS,
            gameOverElapsedTimeMs = neededTime,
            level = Level()
        )

        // then
        Assert.assertTrue(timeUpdate2 < Rules.MAX_TIME_MS)
    }

}
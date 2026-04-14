package de.ka.crunchrgame

import de.ka.crunchrgame.models.Level
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RuleTest {

    @Test
    fun testSymbols() {
        // given, when
        val symbols = Rules.determineSymbols(levelValue = 1)

        // then
        assertTrue(symbols.isNotEmpty())
    }

    @Test
    fun testCrunch() {
        // given, when
        val crunch1 = Rules.determineNewCrunch(level = Level(1))
        val crunch2 = Rules.determineNewCrunch(level = Level(100))

        // then — both produce valid crunches with expected time
        assertTrue(crunch1.crunchTimeMs > 0)
        assertTrue(crunch2.crunchTimeMs > 0)
    }

    @Test
    fun testCrunchResult() {
        // given, when
        val crunch1 = Rules.determineNewCrunch(level = Level(1))
        val result1 =
            Rules.determineCrunchResult(
                crunch = crunch1,
                input = crunch1.expected,
                neededTimeMs = 100
            )

        // then
        assertTrue(result1.successful)

        // given, when
        val crunch2 = Rules.determineNewCrunch(level = Level(1))
        val result2 =
            Rules.determineCrunchResult(crunch = crunch2, input = crunch2.expected + 1)

        // then
        assertFalse(result2.successful)
    }

    @Test
    fun testLevel() {
        // given, when
        val level1 = Rules.determineLevel(startLevel = Level(1), score = 1000)

        // then — level increases from startLevel
        assertTrue(level1.value > 1)

        // given, when — same score with higher start should yield even higher level
        val level2 = Rules.determineLevel(startLevel = level1, score = 1000)

        // then
        assertTrue(level2.value > level1.value)

        // given, when — lower score should not increase level as much
        val level3 = Rules.determineLevel(startLevel = level2, score = 0)

        // then — with zero score, level stays at startLevel
        assertEquals(level2.value, level3.value)
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
        assertTrue(timeUpdate1 > Rules.MAX_TIME_MS)

        // given, when
        val timeUpdate2 = Rules.determineTimeUpdate(
            success = false,
            gameOverTimeMs = Rules.MAX_TIME_MS,
            gameOverElapsedTimeMs = neededTime,
            level = Level()
        )

        // then — failure does not extend time (stays at gameOverTimeMs)
        assertTrue(timeUpdate2 <= Rules.MAX_TIME_MS)
    }
}

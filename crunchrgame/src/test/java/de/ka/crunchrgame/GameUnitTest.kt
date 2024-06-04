package de.ka.crunchrgame

import de.ka.crunchrgame.models.Level
import de.ka.crunchrgame.models.Listeners
import de.ka.crunchrgame.models.Savers
import de.ka.crunchrgame.models.State
import de.ka.crunchrgame.models.Status
import de.ka.crunchrgame.models.crunch.Crunch
import de.ka.crunchrgame.models.crunch.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class GameUnitTest {

    private var currentStatus: Status? = null
    private val loadedState = Status.PAUSED

    private fun CrunchrGame.setup(): CrunchrGame {
        this.saver = Savers(
            onGameLoad = { State(status = loadedState) }
        )
        this.listener = Listeners(
            gameStatusUpdate = { currentStatus = it }
        )
        assertNull(currentStatus)
        return this
    }

    @Test
    fun runs_expected_status() = runTest {
        // given
        val game = CrunchrGame(this).setup()

        // when
        game.startNew()

        // then
        assertEquals(Status.RUNNING, currentStatus)

        // when
        game.pause()

        // then
        assertEquals(Status.PAUSED, currentStatus)

        // when
        game.resume()

        // then
        assertEquals(loadedState, currentStatus)

        // when
        game.forfeit()

        // then
        assertEquals(Status.ENDED, currentStatus)

        // when
        game.startNew(Level(2))

        // then
        assertEquals(Status.RUNNING, currentStatus)

        // when
        game.quit()

        // then
        assertEquals(Status.NOT_STARTED, currentStatus)
    }


    @Test
    fun solves_expected() = runTest {
        // given
        var currentCrunch: Crunch? = null
        var currentResult: Result? = null
        val game = CrunchrGame(this).setup()
        game.listener = game.listener.copy(currentCrunchUpdate = { currentCrunch = it },
            solveUpdate = { currentResult = it })

        // when
        game.startNew()

        // then
        assertNull(currentResult)
        val expected = currentCrunch?.expected
        assertNotNull(expected)

        // when
        game.solve(expected!!)

        // then
        assertNotNull(currentResult)
        assertTrue(currentResult!!.successful)
        assertEquals(expected, currentResult!!.expected)
    }

    @Test
    fun time_out_expected() = runTest {
        // given
        val game = CrunchrGame(this).setup()

        // when
        game.startNew()
        delay(Rules.MAX_TIME_MS+1) // natural game over

        // then
        assertEquals(Status.ENDED, currentStatus)
    }
}
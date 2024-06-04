package de.ka.crunchr.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.ka.crunchr.domain.AppGameSaver
import de.ka.crunchrgame.models.State
import de.ka.crunchrgame.models.Status
import de.ka.crunchrgame.models.Level
import de.ka.crunchrgame.models.crunch.Crunch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("GameState")

class AppGameSaverImpl(private val context: Context) : AppGameSaver {
    private val gameOverElapsedTimeMs = longPreferencesKey("gameTimeElapsedMs")
    private val lastCrunchSolvedTimeMs = longPreferencesKey("lastSolveTimeMs")
    private val solveElapsedTimeMs = longPreferencesKey("solveTimeElapsedMs")
    private val overallCrunchCount = intPreferencesKey("overallCrunchCount")
    private val bestStreakCount = intPreferencesKey("bestStreakCount")
    private val gameOverTimeMs = longPreferencesKey("gameTimeMs")
    private val streakCount = intPreferencesKey("streakCount")
    private val solveCount = intPreferencesKey("solveCount")
    private val startLevel = intPreferencesKey("startLevel")
    private val crunch = stringPreferencesKey("crunch")
    private val status = intPreferencesKey("status")
    private val score = longPreferencesKey("score")
    private val level = intPreferencesKey("level")

    private val nullLong = 999999L

    override suspend fun saveGameState(state: State) {
        context.dataStore.edit { store ->
            store[gameOverTimeMs] = state.gameOverTimeMs
            store[gameOverElapsedTimeMs] = state.gameOverElapsedTimeMs
            store[solveElapsedTimeMs] = state.currentCrunchElapsedTimeMs
            store[startLevel] = state.startLevel.value
            store[level] = state.currentLevel.value
            store[score] = state.currentScore
            store[solveCount] = state.currentSolvedCrunchCount
            store[status] = state.status.ordinal
            store[streakCount] = state.streakCount
            store[overallCrunchCount] = state.overallCrunchCount
            store[bestStreakCount] = state.bestStreakCount
            state.currentCrunch?.seed?.let {
                store[crunch] = it
            }
            with(state.lastCrunchSolvedTimeMs) {
                store[lastCrunchSolvedTimeMs] = this ?: nullLong
            }
        }
    }

    override suspend fun loadGameState(): State? {
        delay(100) // makes sure saving always has taken place prior to loading
        // there is no other guarantee as if the device is rotated we do not know when this happens
        val fallbackState = State()
        return context.dataStore.data.map { store ->
            val statusIndex = store[status] ?: fallbackState.status.ordinal
            State(
                gameOverTimeMs = store[gameOverTimeMs] ?: fallbackState.gameOverTimeMs,
                gameOverElapsedTimeMs = store[gameOverElapsedTimeMs]
                    ?: fallbackState.gameOverElapsedTimeMs,
                currentCrunchElapsedTimeMs = store[solveElapsedTimeMs]
                    ?: fallbackState.currentCrunchElapsedTimeMs,
                startLevel = Level(value = store[startLevel] ?: fallbackState.startLevel.value),
                currentLevel = Level(store[level] ?: fallbackState.currentLevel.value),
                currentScore = store[score] ?: fallbackState.currentScore,
                overallCrunchCount = store[overallCrunchCount]
                    ?: fallbackState.overallCrunchCount,
                streakCount = store[streakCount] ?: fallbackState.streakCount,
                lastCrunchSolvedTimeMs = if (store[lastCrunchSolvedTimeMs] == nullLong) null else store[lastCrunchSolvedTimeMs]
                    ?: fallbackState.lastCrunchSolvedTimeMs,
                currentSolvedCrunchCount = store[solveCount]
                    ?: fallbackState.currentSolvedCrunchCount,
                bestStreakCount = store[bestStreakCount]
                    ?: fallbackState.bestStreakCount,
                currentCrunch = store[crunch]?.let {
                    Crunch.createFromSeed(it)
                } ?: fallbackState.currentCrunch,
                status = Status.entries[if (statusIndex < Status.entries.size) statusIndex else 0]
            )
        }.firstOrNull()
    }
}
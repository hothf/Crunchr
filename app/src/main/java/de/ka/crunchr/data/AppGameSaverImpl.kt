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
import de.ka.crunchrgame.Crunch
import de.ka.crunchrgame.GameState
import de.ka.crunchrgame.GameStatus
import de.ka.crunchrgame.Level
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("GameState")

class AppGameSaverImpl(private val context: Context) : AppGameSaver {
    private val gameOverElapsedTimeMs = longPreferencesKey("gameTimeElapsedMS")
    private val solveElapsedTimeMs = longPreferencesKey("solveTimeElapsedMS")
    private val gameOverTimeMs = longPreferencesKey("gameTimeMS")
    private val solveCount = intPreferencesKey("solveCount")
    private val crunch = stringPreferencesKey("crunchSeed")
    private val status = intPreferencesKey("status")
    private val score = longPreferencesKey("score")
    private val level = intPreferencesKey("level")

    override suspend fun saveGameState(gameState: GameState) {
        context.dataStore.edit { store ->
            store[gameOverTimeMs] = gameState.gameOverTimeMs
            store[gameOverElapsedTimeMs] = gameState.gameOverElapsedTimeMs
            store[solveElapsedTimeMs] = gameState.currentCrunchElapsedTimeMs
            store[level] = gameState.currentLevel.value
            store[score] = gameState.currentScore
            store[solveCount] = gameState.currentSolvedCrunchCount
            store[status] = gameState.status.ordinal
            gameState.currentCrunch?.let {
                store[crunch] = it.seed
            }
        }
    }

    override suspend fun loadGameState(): GameState? {
        val fallbackGameState = GameState()
        return context.dataStore.data.map { store ->
            val statusIndex = store[status] ?: fallbackGameState.status.ordinal
            GameState(
                gameOverTimeMs = store[gameOverTimeMs] ?: fallbackGameState.gameOverTimeMs,
                gameOverElapsedTimeMs = store[gameOverElapsedTimeMs]
                    ?: fallbackGameState.gameOverElapsedTimeMs,
                currentCrunchElapsedTimeMs = store[solveElapsedTimeMs]
                    ?: fallbackGameState.currentCrunchElapsedTimeMs,
                currentLevel = Level(store[level] ?: fallbackGameState.currentLevel.value),
                currentCrunch = Crunch.createFromSeed(store[crunch]),
                currentScore = store[score] ?: fallbackGameState.currentScore,
                currentSolvedCrunchCount = store[solveCount]
                    ?: fallbackGameState.currentSolvedCrunchCount,
                status = GameStatus.entries[if (statusIndex < GameStatus.entries.size) statusIndex else 0]
            )
        }.firstOrNull()
    }
}
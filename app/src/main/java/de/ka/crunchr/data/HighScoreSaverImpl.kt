package de.ka.crunchr.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.ka.crunchr.domain.HighScore
import de.ka.crunchr.domain.HighScoreSaver
import de.ka.crunchrgame.models.Score
import de.ka.crunchrgame.models.Level
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.highScoreStore: DataStore<Preferences> by preferencesDataStore("HighScore")

class HighScoreSaverImpl(private val context: Context) : HighScoreSaver {
    private val score = longPreferencesKey("score")
    private val streakCount = intPreferencesKey("streakCount")
    private val solveCount = intPreferencesKey("solveCount")
    private val level = intPreferencesKey("level")
    private val averageTimeMs = longPreferencesKey("averageTime")
    private val timeMs = longPreferencesKey("time")

    override suspend fun findHighScore(score: Score): HighScore {
        val current = load()

        val highScore = HighScore(
            score = if (current.score == null || current.score < score.score) score.score else null,
            streakCount = if (current.streakCount == null || current.streakCount < score.bestStreakCount) score.bestStreakCount else null,
            solveCount = if (current.solveCount == null || current.solveCount < score.crunchCount) score.crunchCount else null,
            level = if (current.level == null || current.level.value < score.level.value) score.level else null,
            averageTimeMs = if (current.averageTimeMs == null || current.averageTimeMs > score.getAverageSolvingTimeMs()) score.getAverageSolvingTimeMs() else null,
            time = if (current.time == null || current.time < score.elapsedTimeMs) score.elapsedTimeMs else null,
        )
        save(highScore)

        return highScore
    }

    private suspend fun save(highScore: HighScore) {
        context.highScoreStore.edit { store ->
            highScore.score?.let {
                store[score] = it
            }
            highScore.streakCount?.let {
                store[streakCount] = it
            }
            highScore.solveCount?.let {
                store[solveCount] = it
            }
            highScore.level?.let {
                store[level] = it.value
            }
            highScore.averageTimeMs?.let {
                store[averageTimeMs] = it
            }
            highScore.time?.let {
                store[timeMs] = it
            }
        }
    }

    private suspend fun load(): HighScore {
        return context.highScoreStore.data.map { store ->
            HighScore(
                score = store[score],
                streakCount = store[streakCount],
                solveCount = store[solveCount],
                level = store[level]?.let { Level(it) },
                averageTimeMs = store[averageTimeMs],
                time = store[timeMs],
            )
        }.first()
    }
}
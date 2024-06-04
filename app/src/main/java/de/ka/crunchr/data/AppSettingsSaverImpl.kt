package de.ka.crunchr.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.ka.crunchr.domain.AppSettingsSaver
import de.ka.crunchrgame.models.Level
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.settingsStore: DataStore<Preferences> by preferencesDataStore("AppSettings")

class AppSettingsSaverImpl(private val context: Context) : AppSettingsSaver {

    private val vibrationsEnabled = booleanPreferencesKey("vibration")
    private val soundEnabled = booleanPreferencesKey("sounds")
    private val chosenLevel = intPreferencesKey("chosenLevel")

    override suspend fun setChosenLevel(level: Level) {
        context.settingsStore.edit { store ->
            store[chosenLevel] = level.value
        }
    }

    override  suspend fun getChosenLevel(): Level {
        return context.settingsStore.data.map { store ->
            Level(value = store[chosenLevel] ?: Level().value)
        }.firstOrNull() ?: Level()
    }

    override suspend fun setEnabled(toggle: AppSettingsSaver.Toggle, enabled: Boolean) {
        context.settingsStore.edit { store ->
            store[getKeyForToggle(toggle)] = enabled
        }
    }

    override suspend fun getEnabled(toggle: AppSettingsSaver.Toggle): Boolean {
        return context.settingsStore.data.map { store ->
            store[getKeyForToggle(toggle)]
        }.firstOrNull() ?: true
    }

    private fun getKeyForToggle(toggle: AppSettingsSaver.Toggle): Preferences.Key<Boolean> {
        return when (toggle) {
            AppSettingsSaver.Toggle.VibrationEnabled -> vibrationsEnabled
            AppSettingsSaver.Toggle.SoundEnabled -> soundEnabled
        }
    }
}
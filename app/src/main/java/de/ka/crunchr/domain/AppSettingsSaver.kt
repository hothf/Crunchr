package de.ka.crunchr.domain

import de.ka.crunchrgame.models.Level

/**
 * Handles saving & loading of the app's settings.
 */
interface AppSettingsSaver {

    /**
     * Sets the chosen level.
     */
    suspend fun setChosenLevel(level: Level)

    /**
     * Retrieves the chosen level.
     */
    suspend fun getChosenLevel(): Level

    /**
     * Set the enabled value for the given toggle.
     */
    suspend fun setEnabled(toggle: Toggle, enabled: Boolean)

    /**
     * Get the enabled value for the given toggle.
     */
    suspend fun getEnabled(toggle: Toggle): Boolean

    /**
     * Contains all possible toggles of the settings.
     */
    enum class Toggle {
        /**
         * The sound enabled toggle.
         */
        SoundEnabled,

        /**
         * The vibration enabled toggle.
         */
        VibrationEnabled
    }
}

class StubAppSettingsSaver : AppSettingsSaver {
    override suspend fun setEnabled(toggle: AppSettingsSaver.Toggle, enabled: Boolean) {
        // do nothing
    }

    override suspend fun getEnabled(toggle: AppSettingsSaver.Toggle): Boolean {
        return true
    }

    override suspend fun setChosenLevel(level: Level) {
        // do nothing
    }

    override suspend fun getChosenLevel(): Level {
        return Level()
    }
}
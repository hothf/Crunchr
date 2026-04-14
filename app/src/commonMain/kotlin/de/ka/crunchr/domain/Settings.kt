package de.ka.crunchr.domain

data class Settings(
    val isVibrationEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true
) {

    /**
     * Saves this [Settings] with the given [saver].
     */
    suspend fun save(saver: () -> AppSettingsSaver?) {
        saver()?.apply {
            setEnabled(AppSettingsSaver.Toggle.SoundEnabled, isSoundEnabled)
            setEnabled(AppSettingsSaver.Toggle.VibrationEnabled, isVibrationEnabled)
        }
    }

    companion object {

        /**
         * Loads [Settings] with the given [saver].
         */
        suspend fun load(saver: () -> AppSettingsSaver?): Settings? {
            val isVibrationEnabled =
                saver()?.getEnabled(AppSettingsSaver.Toggle.VibrationEnabled)
            val isSoundEnabled = saver()?.getEnabled(AppSettingsSaver.Toggle.SoundEnabled)
            return if (isVibrationEnabled != null && isSoundEnabled != null) {
                Settings(isVibrationEnabled = isVibrationEnabled, isSoundEnabled = isSoundEnabled)
            } else null
        }
    }
}
package de.ka.crunchr.data

import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.VibratorManager
import de.ka.crunchr.domain.Vibrator

class VibratorImpl(context: Context) : Vibrator {
    private var vibrator: VibratorManager? = null
    private var deprecatedVibrator: android.os.Vibrator? = null

    init {
        val applicationContext = context.applicationContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vibrator =
                applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager?
        } else {
            deprecatedVibrator =
                applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
        }
    }

    override fun vibrate(long: Boolean) {
        vibrator?.let {
            val effect = VibrationEffect.createOneShot(
                if (long) LONG_TIME_MS else SHORT_TIME_MS,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
            it.vibrate(CombinedVibration.createParallel(effect))
        }

        deprecatedVibrator?.vibrate(if (long) LONG_TIME_MS else SHORT_TIME_MS)
    }

    companion object {
        const val LONG_TIME_MS = 1_000L
        const val SHORT_TIME_MS = 500L
    }

}
package de.ka.crunchr.domain

/**
 * Allows for vibrating.
 */
fun interface Vibrator {

    /**
     * Vibrate either [long] or short.
     */
    fun vibrate(long: Boolean)
}

class StubVibrator: Vibrator {
    override fun vibrate(long: Boolean) {
        // do nothing
    }
}
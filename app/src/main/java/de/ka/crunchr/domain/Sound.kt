package de.ka.crunchr.domain

/**
 * Allows for playing sounds.
 */
interface Sound {

    /**
     * Called to prepare every sound. Should be called once at app startup.
     *
     * Remember to call [release] when done to release those resources.
     */
    suspend fun prepare()

    /**
     * Releases every sound file. Should be called once the app no longer needs the sounds.
     *
     * Needs calling to [prepare] after this.
     */
    fun release()

    /**
     * Plays a [sound][Sounds].
     */
    fun play(sound: Sounds)

    /**
     * Lists all possible sounds.
     */
    enum class Sounds {
        SOLVE_SUCCESS, SOLVE_FAIL, GAME_OVER, NEW_LEVEL
    }
}

class StubSound : Sound {
    override suspend fun prepare() {
        // do nothing
    }

    override fun release() {
        // do nothing
    }

    override fun play(sound: Sound.Sounds) {
        // do nothing
    }
}
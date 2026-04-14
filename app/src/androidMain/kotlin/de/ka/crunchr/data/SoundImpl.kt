package de.ka.crunchr.data

import android.content.Context
import android.media.MediaPlayer
import de.ka.crunchr.R
import de.ka.crunchr.domain.Sound

class SoundImpl(private val context: Context) : Sound {
    private val sounds = mutableMapOf<String, List<MediaPlayer>>()

    private var successIndex = 0
    private var failIndex = 0

    override suspend fun prepare() {
        sounds[Sound.Sounds.SOLVE_SUCCESS.name] = listOf(
            createSound(R.raw.success_1),
            createSound(R.raw.success_2),
            createSound(R.raw.success_3),
            createSound(R.raw.success_4)
        )
        sounds[Sound.Sounds.SOLVE_FAIL.name] = listOf(
            createSound(R.raw.fail_1),
            createSound(R.raw.fail_2),
            createSound(R.raw.fail_3)
        )
        sounds[Sound.Sounds.GAME_OVER.name] =
            listOf(createSound(R.raw.over))
        sounds[Sound.Sounds.NEW_LEVEL.name] =
            listOf(createSound(R.raw.level))
    }

    override fun release() {
        sounds.values.forEach { sounds -> sounds.forEach { it.release() } }
        successIndex = 0
        failIndex = 0
    }

    override fun play(sound: Sound.Sounds) {
        if (sound == Sound.Sounds.SOLVE_SUCCESS) {
            sounds[sound.name]?.get(successIndex)?.start()
            successIndex++
            if (successIndex >= (sounds[sound.name]?.size ?: 0)) {
                successIndex = 0
            }
        } else if (sound == Sound.Sounds.SOLVE_FAIL) {
            sounds[sound.name]?.get(failIndex)?.start()
            failIndex++
            if (failIndex >= (sounds[sound.name]?.size ?: 0)) {
                failIndex = 0
            }
        } else {
            sounds[sound.name]?.first()?.start()
        }
    }

    private fun createSound(rawResId: Int): MediaPlayer {
        return MediaPlayer.create(context.applicationContext, rawResId)
    }
}
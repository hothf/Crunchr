package de.ka.crunchrgame.saving

import de.ka.crunchrgame.GameState

/**
 * A game save to save & load a [GameState].
 */
interface GameSaver {

    /**
     * Saves the given [gameState].
     */
    fun save(gameState: GameState)

    /**
     * Loads a [GameState].
     */
    fun load(): GameState
}
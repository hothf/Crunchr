package de.ka.crunchr.domain

import de.ka.crunchrgame.GameState

/**
 * Handles saving & loading of [GameState]s.
 */
interface AppGameSaver {

    /**
     * Responsible for saving a complete [GameState].
     */
    suspend fun saveGameState(gameState: GameState)

    /**
     * Loads a complete [GameState].
     */
    suspend fun loadGameState(): GameState?
}
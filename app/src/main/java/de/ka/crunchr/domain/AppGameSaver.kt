package de.ka.crunchr.domain

import de.ka.crunchrgame.GameState

interface AppGameSaver {

    suspend fun saveGameState(gameState: GameState)

    suspend fun loadGameState(): GameState?
}
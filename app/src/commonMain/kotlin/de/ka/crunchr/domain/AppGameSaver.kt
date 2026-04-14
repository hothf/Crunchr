package de.ka.crunchr.domain

import de.ka.crunchrgame.models.State

/**
 * Handles saving & loading of [State]s.
 */
interface AppGameSaver {

    /**
     * Responsible for saving a complete [State].
     */
    suspend fun saveGameState(state: State)

    /**
     * Loads a complete [State].
     */
    suspend fun loadGameState(): State?
}

class StubAppGameSaver : AppGameSaver {
    override suspend fun saveGameState(state: State) {
        // do nothing
    }

    override suspend fun loadGameState(): State? {
        return null
    }
}
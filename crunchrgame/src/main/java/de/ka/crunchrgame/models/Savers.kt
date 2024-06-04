package de.ka.crunchrgame.models

data class Savers(
    val onGameSave: suspend (state: State) -> Unit = { },
    val onGameLoad: suspend () -> State? = { null }
)
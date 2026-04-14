package de.ka.crunchrgame.models

import de.ka.crunchrgame.Rules
import de.ka.crunchrgame.models.crunch.Symbols

data class Level(
    val value: Int = 1,
    val symbols: List<Symbols> = Rules.determineSymbols(value)
)
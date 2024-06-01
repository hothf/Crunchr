package de.ka.crunchrgame

import kotlin.random.Random

/**
 * Creates new [Crunch]es.
 */
object CrunchGenerator {

    /**
     * Create a new [Crunch] seed for the given [Level].
     */
    fun createForLevel(level: Level): Crunch {
        val symbols = mutableListOf(Crunch.CrunchSymbols.ADD, Crunch.CrunchSymbols.MINUS)
        var maxNum = 10
        var minNum = 0
        if (level.value > 10) {
            symbols.add(Crunch.CrunchSymbols.MULTIPLY)
        }
        if (level.value > 20) {
            maxNum = 100
            symbols.add(Crunch.CrunchSymbols.DIVIDE)
        }
        if (level.value > 30) {
            maxNum = 1000
        }
        if (level.value > 40) {
            minNum = -1000
        }
        val symbol = symbols[Random.nextInt(0, symbols.size - 1)]
        val first = Random.nextInt(minNum, maxNum)
        val second = Random.nextInt(minNum, maxNum)

        val seed = "${symbol.seed}.${Integer.toHexString(first)}.${Integer.toHexString(second)}"

        return Crunch(
            firstNum = first,
            secondNum = second,
            symbol = symbol,
            seed = seed
        )
    }

    /**
     * Reads the seed and creates a crunch from it.
     */
    fun readSeed(seed: String?): Crunch? {
        if (seed.isNullOrBlank()) return null

        val split = seed.split(".")
        if (split.size != 3) return null

        val symbol = Crunch.CrunchSymbols.entries.firstOrNull { it.seed == split[0] } ?: return null
        val first = Integer.parseInt(split[1], 16)
        val second = Integer.parseInt(split[2], 16)
        
        return Crunch(
            firstNum = first,
            secondNum = second,
            symbol = symbol,
            seed = seed
        )
    }
}
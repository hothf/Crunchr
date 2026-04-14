package de.ka.crunchrgame.models.crunch


/**
 * A crunch that [displays][display] a mathematical question to be solved to match a [expected]
 * result.
 */
class Crunch(
    val crunchTimeMs: Int,
    firstNum: Int,
    secondNum: Int,
    symbol: Symbols
) {
    /**
     * Offers a handy way to display this crunch.
     */
    val display = "$firstNum ${symbol.stringRepresentation} $secondNum"

    /**
     * Contains the expected result of the crunch.
     */
    val expected = when (symbol) {
        Symbols.ADD -> firstNum + secondNum.toFloat()
        Symbols.MINUS -> firstNum - secondNum.toFloat()
        Symbols.MULTIPLY -> firstNum * secondNum.toFloat()
        Symbols.DIVIDE -> firstNum / secondNum.toFloat()
    }

    /**
     * The seed of a crunch. Useful for easier saving and recreation of the crunch.
     */
    val seed = "${symbol.stringRepresentation}.$crunchTimeMs.$firstNum.$secondNum"

    companion object {

        /**
         * Creates a crunch from the given [seed].
         */
        fun createFromSeed(seed: String): Crunch? {
            val values = seed.split(".")
            if (values.size < 4) return null

            val symbol =
                Symbols.entries.firstOrNull { it.stringRepresentation == values[0] } ?: return null

            return runCatching<Crunch?> {
                Crunch(
                    symbol = symbol,
                    crunchTimeMs = values[1].toInt(),
                    firstNum = values[2].toInt(),
                    secondNum = values[3].toInt()
                )
            }.getOrNull()
        }
    }
}
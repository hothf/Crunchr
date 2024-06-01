package de.ka.crunchrgame

import kotlin.math.abs

/**
 * A crunch that displays a mathematical question to be [solve].
 *
 * There is a [OKAY_DELTA] to
 */
class Crunch(
    val seed: String,
    private val firstNum: Int,
    private val secondNum: Int,
    private val symbol: CrunchSymbols,
) {
    val display = "$firstNum ${symbol.stringRepresentation} $secondNum"

    /**
     * Tries to solve the crunch with the given [input]. Retruns `true` is correct or near it with
     * a possible delta of [OKAY_DELTA].
     * Returns `false` otherwise.
     */
    fun solve(input: Float): Boolean {
        val result = when (symbol) {
            CrunchSymbols.ADD -> firstNum + secondNum.toFloat()
            CrunchSymbols.MINUS -> firstNum - secondNum.toFloat()
            CrunchSymbols.MULTIPLY -> firstNum * secondNum.toFloat()
            CrunchSymbols.DIVIDE -> firstNum / secondNum.toFloat()
        }
        return abs(result - input) <= OKAY_DELTA
    }

    override fun toString(): String {
        return "$seed => $display"
    }

    companion object {

        /**
         * Okay-ish delta if the answer is not perfect.
         */
        const val OKAY_DELTA: Float = 0.1f

        /**
         * Creates a new [Crunch] for the given level.
         */
        fun createNew(level: Level): Crunch {
            return CrunchGenerator.createForLevel(level)
        }

        /**
         * Creates a [Crunch] from a given [seed]. If it is was not possible to restore it, returns
         * `null`.
         */
        fun createFromSeed(seed: String?): Crunch? {
            return CrunchGenerator.readSeed(seed)
        }
    }

    /**
     * Represents all possible [Crunch] symbols.
     */
    enum class CrunchSymbols(
        val stringRepresentation: String,
        val seed: String
    ) {
        ADD("+", "AA"),
        MINUS("-", "BB"),
        MULTIPLY("*", "CC"),
        DIVIDE("/", "DD"),
    }

}
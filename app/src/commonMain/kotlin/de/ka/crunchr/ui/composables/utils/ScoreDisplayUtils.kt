package de.ka.crunchr.ui.composables.utils

import java.lang.IllegalArgumentException
import java.text.DecimalFormat

object ScoreDisplayUtils {

    /**
     * Retrieves a displayable representation of this [Float] rounded to 1 digit. If the number
     * ends wit .00, it displays a integer representation instead.
     *
     * Additionally, you can add symbols after the text with [addSymbols] set to `true`.
     */
    fun Float.getDisplayableFloat(addSymbols: Boolean = false): String? {
        val withSymbols = if (addSymbols) " = " else ""
        return try {
            val formatted = DecimalFormat("#0.0").format(this)
            if (formatted.endsWith(",0") || formatted.endsWith(".0")) {
                return this.toInt().toString() + withSymbols
            }
            if (formatted.startsWith(",") || formatted.startsWith(".")) {
                return "0$formatted"
            }
            return formatted + withSymbols
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
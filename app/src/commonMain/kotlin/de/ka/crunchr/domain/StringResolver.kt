package de.ka.crunchr.domain

/**
 * Keys for strings that need to be resolved outside of a Composable context.
 */
enum class StringKey {
    PERFORMANCE_STREAK,
    PERFORMANCE_MULTIPLIER,
    PERFORMANCE_POINTS_POS,
    PERFORMANCE_POINTS_NEG,
}

/**
 * Allows for resolving strings outside of a Composable context (e.g., in ViewModels).
 */
fun interface StringResolver {

    /**
     * Resolves the string for the given [key]. Optional [args] are substituted into format placeholders.
     */
    fun string(key: StringKey, vararg args: Any): String

}

class StubStringResolver : StringResolver {
    override fun string(key: StringKey, vararg args: Any): String {
        return key.name
    }
}

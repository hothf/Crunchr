package de.ka.crunchr.domain

/**
 * Allows for resolving strings.
 */
fun interface StringResolver {

    /**
     * Resolves the string with the given [res] id. Optional [args] may be used.
     */
    fun string(res: Int, vararg args: Any): String

}

class StubStringResolver : StringResolver {
    override fun string(res: Int, vararg args: Any): String {
        return res.toString()
    }
}

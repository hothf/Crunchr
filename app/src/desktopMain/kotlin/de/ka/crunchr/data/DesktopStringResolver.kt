package de.ka.crunchr.data

import de.ka.crunchr.domain.StringKey
import de.ka.crunchr.domain.StringResolver

class DesktopStringResolver : StringResolver {

    private val strings = mapOf(
        StringKey.PERFORMANCE_STREAK to "%d-Streak",
        StringKey.PERFORMANCE_MULTIPLIER to "x%s",
        StringKey.PERFORMANCE_POINTS_POS to "+%d",
        StringKey.PERFORMANCE_POINTS_NEG to "%d",
    )

    override fun string(key: StringKey, vararg args: Any): String {
        val format = strings[key] ?: return key.name
        return String.format(format, *args)
    }
}

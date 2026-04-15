package de.ka.crunchr.data

import android.content.Context
import de.ka.crunchr.domain.StringKey
import de.ka.crunchr.domain.StringResolver

class StringResolverImpl(private val context: Context) : StringResolver {

    override fun string(key: StringKey, vararg args: Any): String {
        val resId = keyToResId[key] ?: return key.name
        return context.applicationContext.getString(resId, *args)
    }

    companion object {
        private val keyToResId = mapOf(
            StringKey.PERFORMANCE_STREAK to de.ka.crunchr.R.string.performance_streak,
            StringKey.PERFORMANCE_MULTIPLIER to de.ka.crunchr.R.string.performance_multiplier,
            StringKey.PERFORMANCE_POINTS_POS to de.ka.crunchr.R.string.performance_points_pos,
            StringKey.PERFORMANCE_POINTS_NEG to de.ka.crunchr.R.string.performance_points_neg,
        )
    }
}

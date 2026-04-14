package de.ka.crunchr.data

import android.content.Context
import de.ka.crunchr.domain.StringResolver

class StringResolverImpl(private val context: Context) : StringResolver {

    override fun string(res: Int, vararg args: Any): String {
        return context.applicationContext.getString(res, *args)
    }
}
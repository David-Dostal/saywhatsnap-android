package cz.mendelu.pef.xdostal8.saywhatsnap.utils

import java.util.*

object LanguageUtils {

    private val CZECH = "cs"
    private val SLOVAK = "sk"

    fun isLanguageCzech(): Boolean {
        val language = Locale.getDefault().language
        return language.equals(CZECH) || language.equals(SLOVAK)
    }
}
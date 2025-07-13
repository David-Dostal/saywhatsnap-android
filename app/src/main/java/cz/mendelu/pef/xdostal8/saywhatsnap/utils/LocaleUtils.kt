package cz.mendelu.pef.xdostal8.saywhatsnap.utils

import android.content.Context
import android.content.res.Configuration
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import java.util.*


object LocaleUtils {
    suspend fun setLocale(c: Context, dataStore: IDataStoreRepository) {

        updateResources(c, dataStore.getLanguage()) // use locale codes
    }

    private fun updateResources(context: Context, language: String) {
        context.resources.apply {
            val locale = Locale(language)
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)
            context.resources.updateConfiguration(config, displayMetrics)
        }
    }
}
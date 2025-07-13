@file:Suppress("SpellCheckingInspection")

package cz.mendelu.pef.xdostal8.saywhatsnap.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    override suspend fun setFirstRun() {
        val preferencesKey = booleanPreferencesKey(DataStoreConstants.FIRST_RUN)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = false
        }
    }

    override suspend fun getFirstRun(): Boolean {
        return try {
            val preferencesKey = booleanPreferencesKey(DataStoreConstants.FIRST_RUN)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey)) true
            else preferences[preferencesKey]!!
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    private val languageKey = stringPreferencesKey(DataStoreConstants.LANGUAGE_KEY)

    override suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language
        }
    }

    override suspend fun getLanguage(): String {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[languageKey] ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private val originalLanguageKey = stringPreferencesKey(DataStoreConstants.ORIGINAL_LANGUAGE)


    override suspend fun setOriginalLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[originalLanguageKey] = language
        }
    }

    override suspend fun getOriginalLanguage(): String {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[originalLanguageKey] ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private val translationLanguageKey =
        stringPreferencesKey(DataStoreConstants.TRANSLATION_LANGUAGE)


    override suspend fun setTranslationLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[translationLanguageKey] = language
        }
    }

    override suspend fun getTranslationLanguage(): String {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[translationLanguageKey] ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


}
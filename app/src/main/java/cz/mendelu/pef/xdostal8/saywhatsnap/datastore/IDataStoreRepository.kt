package cz.mendelu.pef.xdostal8.saywhatsnap.datastore

interface IDataStoreRepository {
    suspend fun setFirstRun()
    suspend fun getFirstRun(): Boolean

    suspend fun setLanguage(language: String)
    suspend fun getLanguage(): String

    suspend fun setOriginalLanguage(language: String)
    suspend fun getOriginalLanguage(): String

    suspend fun setTranslationLanguage(language: String)
    suspend fun getTranslationLanguage(): String
}
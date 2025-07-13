package cz.mendelu.pef.xdostal8.saywhatsnap.fake

import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.ServerMock
import javax.inject.Inject

class FakeDatastoreRepositoryImpl @Inject constructor() : IDataStoreRepository {
    private var firstRun = false
    private var appLanguage = "en"
    private var originalLanguage = "en"
    private var translationLanguage = "cs"

    override suspend fun setFirstRun() {
        firstRun = false
    }

    override suspend fun getFirstRun(): Boolean {
        return firstRun
    }

    override suspend fun setLanguage(language: String) {
        if (language != ServerMock.changedAppLanguage) {
            Error()
        } else {
            appLanguage = language
        }
    }

    override suspend fun getLanguage(): String {
        return appLanguage
    }

    override suspend fun setOriginalLanguage(language: String) {
        originalLanguage = language
    }

    override suspend fun getOriginalLanguage(): String {
        return originalLanguage
    }

    override suspend fun setTranslationLanguage(language: String) {
        if (language != ServerMock.changedTransLanguage) {
            Error()
        } else {
            translationLanguage = language
        }
    }

    override suspend fun getTranslationLanguage(): String {
        return translationLanguage
    }


}
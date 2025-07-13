package cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations

import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.CommunicationResult
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionResponse
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LibreTranslateRemoteRepositoryImpl
@Inject constructor(private val libreTranslateAPI: LibreTranslateAPI) :
    ILibreTranslateRemoteRepository {

    override suspend fun translate(translationRequest: TranslationRequest): CommunicationResult<TranslationResponse> {
        return processResponse(
            withContext(Dispatchers.IO) {
                libreTranslateAPI.translate(translationRequest)
            }
        )
    }

    override suspend fun getLanguages(): CommunicationResult<List<Language>> {
        return processResponse(
            withContext(Dispatchers.IO) {
                libreTranslateAPI.getLanguages()
            }
        )    }


    override suspend fun detectLanguage(detectionRequest: DetectionRequest): CommunicationResult<DetectionResponse> {
        return processResponse(
            withContext(Dispatchers.IO) {
                libreTranslateAPI.detectLanguage(detectionRequest)
            }
        )
    }
}

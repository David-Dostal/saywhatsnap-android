package cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations

import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.CommunicationResult
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.IBaseRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionResponse
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationResponse

interface ILibreTranslateRemoteRepository : IBaseRemoteRepository {
    suspend fun translate(translationRequest: TranslationRequest) : CommunicationResult<TranslationResponse>


    suspend fun getLanguages() : CommunicationResult<List<Language>>

    suspend fun detectLanguage(detectionRequest: DetectionRequest) : CommunicationResult<DetectionResponse>
}
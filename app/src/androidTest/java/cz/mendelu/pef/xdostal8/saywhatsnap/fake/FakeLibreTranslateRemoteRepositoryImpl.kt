package cz.mendelu.pef.xdostal8.saywhatsnap.fake

import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.CommunicationResult
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.ILibreTranslateRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.ServerMock
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionResponse
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationResponse
import javax.inject.Inject

class FakeLibreTranslateRemoteRepositoryImpl @Inject constructor() :
    ILibreTranslateRemoteRepository {
    override suspend fun translate(translationRequest: TranslationRequest): CommunicationResult<TranslationResponse> {
        return CommunicationResult.Success(ServerMock.appleMockTranslationResponse)
    }

    override suspend fun getLanguages(): CommunicationResult<List<Language>> {
        return CommunicationResult.Success(ServerMock.csEnMockLanguages)
    }

    override suspend fun detectLanguage(detectionRequest: DetectionRequest): CommunicationResult<DetectionResponse> {
        return CommunicationResult.Success(ServerMock.appleMockDetectionResponse)
    }


}
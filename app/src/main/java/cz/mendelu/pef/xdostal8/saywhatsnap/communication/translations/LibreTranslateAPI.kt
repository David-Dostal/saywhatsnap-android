package cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations

import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionResponse
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LibreTranslateAPI {
    @GET("languages")
    suspend fun getLanguages(): Response<List<Language>>

    @POST("translate")
    suspend fun translate(
        @Body translationRequest: TranslationRequest
    ): Response<TranslationResponse>

    @POST("detect")
    suspend fun detectLanguage(
        @Body detectionRequest: DetectionRequest
    ): Response<DetectionResponse>
}
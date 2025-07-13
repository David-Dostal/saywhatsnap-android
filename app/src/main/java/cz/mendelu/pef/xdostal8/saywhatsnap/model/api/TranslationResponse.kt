package cz.mendelu.pef.xdostal8.saywhatsnap.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranslationResponse(
    @Json(name = "detectedLanguage")
    val detectedLanguage: DetectedLanguage?,

    @Json(name = "translatedText")
    val translatedText: String
)

data class DetectedLanguage(
    val confidence: Double,
    val language: String
)

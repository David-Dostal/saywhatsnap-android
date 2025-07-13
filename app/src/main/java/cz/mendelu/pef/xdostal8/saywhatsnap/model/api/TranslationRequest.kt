package cz.mendelu.pef.xdostal8.saywhatsnap.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranslationRequest(
    @Json(name = "q")
    var textToTranslate: String,

    @Json(name = "source")
    var sourceLanguage: String? = "auto",

    @Json(name = "target")
    var targetLanguage: String,

    @Json(name = "format")
    var format: String = "text",

    @Json(name = "api_key")
    var apiKey: String? = null
)
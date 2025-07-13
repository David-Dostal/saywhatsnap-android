package cz.mendelu.pef.xdostal8.saywhatsnap.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetectionRequest(
    @Json(name = "q")
    var textToDetect: String,

    @Json(name = "api_key")
    var apiKey: String? = null
)
package cz.mendelu.pef.xdostal8.saywhatsnap.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetectionResult(
    @Json(name = "language")
    val language: String,

    @Json(name = "confidence")
    val confidence: Double
)

typealias DetectionResponse = List<DetectionResult>

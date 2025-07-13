package cz.mendelu.pef.xdostal8.saywhatsnap.mock

import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectedLanguage
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionResult
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationResponse

object ServerMock {
    // as defined in datastore
    const val extractedText = "apple"

    const val translatedText = "jablko"

    val csEnMockLanguages = listOf(
        Language(id = 1, code = "cs"),
        Language(id = 2, code = "en")
    )

    var initAppLanguage = "en"

    var changedAppLanguage = "cs"

    const val initTransLanguage = "cs"
    const val changedTransLanguage = "en"


    val appleMockTranslationResponse = TranslationResponse(
        detectedLanguage = DetectedLanguage(confidence = 1.0, language = "cs"),
        translatedText = translatedText
    )
    val appleMockDetectionResponse = listOf(
        DetectionResult(language = "en", confidence = 1.0)
    )
    val jablkoMockDetectionResponse = listOf(
        DetectionResult(language = "cs", confidence = 1.0)
    )
}
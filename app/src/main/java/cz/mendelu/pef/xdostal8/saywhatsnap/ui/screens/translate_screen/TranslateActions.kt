package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen

import android.location.Location
import android.net.Uri

interface TranslateActions {

    fun onOriginalLanguageChange(text: String)
    fun onOriginalStringChange(text: String)

    fun loadPreferredLanguage()

    fun getPlaceholderContentText(
    ): Int?

    fun saveTranslation()

    fun onTranslatedLanguageChange(text: String)
    fun onTranslatedStringChange(text: String)

    fun onImageChanged(image: Uri?)

    fun onSaveOptionChanged(option: Int)

    fun saveTranslationToList(): Long?
    fun detectLanguage()

    fun onCoordChange(coords: Location)
    fun translate()
    fun addTranslationToList(): Long

    fun addTranslationToMap(): Long

    fun discardTranslation()


    fun getUserCoords()
}
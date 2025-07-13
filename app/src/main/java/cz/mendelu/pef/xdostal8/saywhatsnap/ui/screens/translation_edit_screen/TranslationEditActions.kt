package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen

import android.net.Uri

interface TranslationEditActions {

    fun onOriginalStringChange(text: String)

    fun onTranslatedStringChange(text: String)

    fun onOriginalLanguageChange(text: String)

    fun onTranslatedLanguageChange(text: String)

    fun onVisibleOnMapChange(boolean: Boolean)

    fun onNameChange(text: String)

    fun onCategoryChange(text: String)

    fun onDescriptionChange(text: String)

    fun onImageChanged(image: Uri?)

    fun forwardTranslation()
    fun saveTranslation()
}
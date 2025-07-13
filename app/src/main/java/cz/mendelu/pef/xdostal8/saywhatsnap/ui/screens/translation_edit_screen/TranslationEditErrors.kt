package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen

data class TranslationEditErrors(
    var originalLanguageError: Int? = null,
    var originalStringError: Int? = null,
    var translatedLanguageError: Int? = null,
    var translatedStringError: Int? = null,
    )
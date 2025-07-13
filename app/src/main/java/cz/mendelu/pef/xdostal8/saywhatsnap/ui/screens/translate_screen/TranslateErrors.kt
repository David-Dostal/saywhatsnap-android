package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen


data class TranslateErrors(
    var originalLanguageError: Int? = null,
    var originalStringError: Int? = null,
    var translatedLanguageError: Int? = null,
    var translatedStringError: Int? = null,
)

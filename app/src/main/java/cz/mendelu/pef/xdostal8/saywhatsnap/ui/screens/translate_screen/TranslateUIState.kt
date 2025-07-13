package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen


sealed class TranslateUIState {
    object Loading : TranslateUIState()
    object Default : TranslateUIState()
    object TranslationChanged : TranslateUIState()
    object TranslationEnded : TranslateUIState()

    object SavedToList : TranslateUIState()
    object SavedToMap : TranslateUIState()
    object Discarded : TranslateUIState()
    object ErrorOccurred : TranslateUIState()
}

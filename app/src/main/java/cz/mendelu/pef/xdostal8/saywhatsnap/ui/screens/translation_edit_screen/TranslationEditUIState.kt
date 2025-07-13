package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen

sealed class TranslationEditUIState {
    object Loading : TranslationEditUIState()
    object Default : TranslationEditUIState()
    object ForwardTranslation : TranslationEditUIState()

    object TranslationChanged : TranslationEditUIState()

    object TranslationSaved : TranslationEditUIState()

    object ErrorOccurred : TranslationEditUIState()
}

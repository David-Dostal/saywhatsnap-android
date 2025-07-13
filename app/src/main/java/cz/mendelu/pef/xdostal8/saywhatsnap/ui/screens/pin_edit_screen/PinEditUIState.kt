package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen

sealed class PinEditUIState {
    object Loading : PinEditUIState()
    object Default : PinEditUIState()
    object TranslationSaved : PinEditUIState()
    object TranslationChanged : PinEditUIState()
    object ErrorOccurred : PinEditUIState()
}

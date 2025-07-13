package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen



sealed class SettingsScreenUIState {
    object Default : SettingsScreenUIState()
    object Loading : SettingsScreenUIState()
    object SettingsChanged : SettingsScreenUIState()
    object ErrorOccurred : SettingsScreenUIState()
    object Saved : SettingsScreenUIState()

}
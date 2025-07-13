package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen

data class SettingErrors(
    var appLanguageError: Int? = null,
    var translateLanguageError: Int? = null,
    val communicationError: Int? = null,

    )
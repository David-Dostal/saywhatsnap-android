package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen


class SettingsData {
    var language: String = ""
    var originalLanguage: String = ""
    var translationLanguage: String = ""
    var languageCodes: List<String>? = null
    var errors: SettingErrors = SettingErrors()

    var availableLocalizations: List<String> = listOf("en", "cs")

}
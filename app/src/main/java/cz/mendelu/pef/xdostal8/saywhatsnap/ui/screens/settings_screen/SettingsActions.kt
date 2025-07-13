package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen

interface SettingsActions {

    fun changeAppLanguage(language: String)

    fun loadPreferredLanguage()

    fun changeAppTranslationLanguage(language: String)
    fun onLanguageChange(language: String)
    fun onTranslatedLanguageChange(language: String)
    fun saveChanges()
    fun loadLanguage()

    fun getLanguages()
}
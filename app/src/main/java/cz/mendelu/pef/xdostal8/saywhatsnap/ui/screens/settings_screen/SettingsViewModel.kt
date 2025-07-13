package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.CommunicationResult
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.ILibreTranslateRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.UiState
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.MyAPIErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val libreTranslateRemoteRepository: ILibreTranslateRemoteRepository,
    private val dataStoreRepository: IDataStoreRepository
) : BaseViewModel(),
    SettingsActions {


    var screenUIState: MutableState<SettingsScreenUIState> =
        mutableStateOf(SettingsScreenUIState.Loading)

    var uiData: SettingsData = SettingsData()

    var uiErrors: SettingErrors = SettingErrors()

    val translateLanguagesUIState: MutableState<UiState<List<Language>, MyAPIErrors>> =
        mutableStateOf(UiState())


    override fun changeAppLanguage(language: String) {
        launch {
            if (dataStoreRepository.getLanguage() != language) {
                dataStoreRepository.setLanguage(language)
            }
        }
    }

    override fun changeAppTranslationLanguage(language: String) {
        launch {
            if (dataStoreRepository.getTranslationLanguage() != language) {
                dataStoreRepository.setTranslationLanguage(language)
            }
        }
    }

    override fun onLanguageChange(language: String) {
        uiData.language = language
        screenUIState.value = SettingsScreenUIState.SettingsChanged
    }

    override fun onTranslatedLanguageChange(language: String) {
        uiData.translationLanguage = language
        screenUIState.value = SettingsScreenUIState.SettingsChanged
    }

    override fun loadPreferredLanguage() {
        launch {
            uiData.translationLanguage = dataStoreRepository.getTranslationLanguage()
        }
    }

    override fun saveChanges() {
        uiErrors = SettingErrors()
        var save = true
        if (uiData.language.isEmpty()) {
            uiErrors.appLanguageError = R.string.cannot_be_empty
            save = false
        }

        if (!uiData.availableLocalizations.contains(uiData.language)) {
            uiErrors.appLanguageError = R.string.invalid_app_language
            save = false
        }
        // can be empty...
        /*
        if (uiData.translationLanguage.isEmpty()) {
            uiErrors.translateLanguageError = R.string.cannot_be_empty
            save = false
        }

         */
        // if I see button with this functionality, the languages have been already loaded
        if (uiData.languageCodes?.contains(uiData.translationLanguage) == false
            && uiData.translationLanguage != ""
        ) {
            uiErrors.translateLanguageError = R.string.not_available_translation_language
            save = false
        }
        if (save) {
            launch {
                changeAppTranslationLanguage(uiData.translationLanguage)
                changeAppLanguage(uiData.language)
                screenUIState.value = SettingsScreenUIState.Saved
            }
        } else {
            screenUIState.value = SettingsScreenUIState.ErrorOccurred
        }
    }

    override fun loadLanguage() {
        launch {
            uiData.language = dataStoreRepository.getLanguage()
        }
    }

    override fun getLanguages() {
        launch {
            try {

                val result = withContext(Dispatchers.IO) {
                    libreTranslateRemoteRepository.getLanguages()
                }

                when (result) {
                    is CommunicationResult.ConnectionError -> translateLanguagesUIState.value =
                        UiState(
                            loading = false,
                            null,
                            errors = MyAPIErrors(communicationError = R.string.no_internet_connection)
                        )

                    is CommunicationResult.Error -> translateLanguagesUIState.value = UiState(
                        loading = false,
                        null,
                        MyAPIErrors(communicationError = R.string.loading_available_languages_failed)
                    )

                    is CommunicationResult.Exception -> translateLanguagesUIState.value = UiState(
                        loading = false,
                        null,
                        MyAPIErrors(communicationError = R.string.unknown_error)

                    )

                    is CommunicationResult.Success -> {

                        translateLanguagesUIState.value =
                            UiState(loading = false, data = result.data, errors = null)

                        uiData.languageCodes = result.data.map { it.code.orEmpty() }

                    }
                }

            } catch (e: Exception) {
                // Handle general exceptions
                translateLanguagesUIState.value = UiState(
                    loading = false,
                    null,
                    MyAPIErrors(communicationError = R.string.unknown_error)
                )

            }
        }


    }
}

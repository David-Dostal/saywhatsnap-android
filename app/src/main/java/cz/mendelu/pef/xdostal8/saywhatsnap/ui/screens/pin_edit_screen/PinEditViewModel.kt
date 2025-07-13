package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinEditViewModel @Inject constructor(
    private val translationsRepository: ITranslationsRepository,

    ) : BaseViewModel(), PinEditActions {

    var screenUIState: MutableState<PinEditUIState> = mutableStateOf(PinEditUIState.Loading)

    var uiData: PinEditData = PinEditData()

    var uiErrors: PinEditErrors = PinEditErrors()


    fun loadTranslation(translationID: Long) {
        launch {
            uiData.translation = translationsRepository.getTranslationById(translationID)
            screenUIState.value = PinEditUIState.TranslationChanged
        }
    }

    fun deleteTranslation() {
        val translationID = uiData.translation.id
        if (translationID != null) {
            launch {
                translationsRepository.deleteTranslationById(translationID)
            }
        }
    }

    override fun onNameChange(text: String) {
        uiData.translation.name = text
        screenUIState.value = PinEditUIState.TranslationChanged
    }

    override fun onCategoryChange(text: String) {
        uiData.translation.category = text
        screenUIState.value = PinEditUIState.TranslationChanged
    }

    override fun onDescriptionChange(text: String) {
        uiData.translation.description = text
        screenUIState.value = PinEditUIState.TranslationChanged
    }

    override fun saveTranslation() {
        uiErrors = PinEditErrors()
        var save = true
        if (uiData.translation.category.isEmpty()) {
            uiErrors.categoryError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.name.isEmpty()) {
            uiErrors.nameError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.description.isEmpty()) {
            uiErrors.descriptionError = R.string.cannot_be_empty
            save = false
        }
        if (save) {
            launch {
                translationsRepository.update(uiData.translation)
                screenUIState.value = PinEditUIState.TranslationSaved
            }
        } else {
            screenUIState.value = PinEditUIState.ErrorOccurred
        }
    }
}


package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationEditViewModel @Inject constructor(
    private val translationsRepository: ITranslationsRepository,

    ) : BaseViewModel(), TranslationEditActions {

    var uiData: TranslationEditData = TranslationEditData()

    var uiErrors: TranslationEditErrors = TranslationEditErrors()

    var screenUIState: MutableState<TranslationEditUIState> =
        mutableStateOf(TranslationEditUIState.Loading)


    fun loadTranslation(translationID: Long) {
        launch {
            uiData.translation = translationsRepository.getTranslationById(translationID)
            screenUIState.value = TranslationEditUIState.TranslationChanged
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

    override fun onImageChanged(image: Uri?) {
        if (image != null) {
            uiData.selectedImageURI = image
            uiData.translation.image = image.toString()
        } else {
            uiData.selectedImageURI = Uri.EMPTY
            uiData.translation.image = Uri.EMPTY.toString()
        }
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }

    override fun onOriginalStringChange(text: String) {
        uiData.translation.originalString = text
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }

    override fun onTranslatedStringChange(text: String) {
        uiData.translation.translatedString = text
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }

    override fun onOriginalLanguageChange(text: String) {
        uiData.translation.originalLanguage = text
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }

    override fun onTranslatedLanguageChange(text: String) {
        uiData.translation.translatedLanguage = text
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }

    override fun onVisibleOnMapChange(boolean: Boolean) {
        uiData.translation.visible = boolean
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }


    override fun onNameChange(text: String) {
        uiData.translation.name = text
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }


    override fun onCategoryChange(text: String) {
        uiData.translation.category = text
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }

    override fun onDescriptionChange(text: String) {
        uiData.translation.description = text
        screenUIState.value = TranslationEditUIState.TranslationChanged
    }


    override fun saveTranslation() {
        uiErrors = TranslationEditErrors()
        var save = true
        if (uiData.translation.originalLanguage.isEmpty()) {
            uiErrors.originalLanguageError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.originalString.isEmpty()) {
            uiErrors.originalStringError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.translatedLanguage.isEmpty()) {
            uiErrors.translatedLanguageError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.translatedString.isEmpty()) {
            uiErrors.translatedStringError = R.string.cannot_be_empty
            save = false
        }
        if (save) {
            launch {
                translationsRepository.update(uiData.translation)
                screenUIState.value = TranslationEditUIState.TranslationSaved
            }
        } else {
            screenUIState.value = TranslationEditUIState.ErrorOccurred
        }
    }

    override fun forwardTranslation() {
        uiErrors = TranslationEditErrors()
        var save = true
        if (uiData.translation.originalLanguage.isEmpty()) {
            uiErrors.originalLanguageError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.originalString.isEmpty()) {
            uiErrors.originalStringError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.translatedLanguage.isEmpty()) {
            uiErrors.translatedLanguageError = R.string.cannot_be_empty
            save = false
        }
        if (uiData.translation.translatedString.isEmpty()) {
            uiErrors.translatedStringError = R.string.cannot_be_empty
            save = false
        }
        if (save) {
            launch {
                translationsRepository.update(uiData.translation)
                screenUIState.value = TranslationEditUIState.ForwardTranslation
            }
        } else {
            screenUIState.value = TranslationEditUIState.ErrorOccurred
        }
    }
}



package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen

import android.location.Location
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.CommunicationResult
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.location.ILocationTracker
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.ILibreTranslateRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.UiState
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.DetectionResponse
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.MyAPIErrors
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationRequest
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.TranslationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val libreTranslateRemoteRepositoryImpl: ILibreTranslateRemoteRepository,
    private val translationsRepository: ITranslationsRepository,
    private val locationTracker: ILocationTracker,
    private val dataStoreRepository: IDataStoreRepository

) : BaseViewModel(), TranslateActions {

    var uiData: TranslateData = TranslateData()

    var uiErrors: TranslateErrors = TranslateErrors()

    val screenUIState: MutableState<TranslateUIState> =
        mutableStateOf(TranslateUIState.Loading)

    val translateUIState: MutableState<UiState<TranslationResponse, MyAPIErrors>> =
        mutableStateOf(UiState(loading = false))


    val detectUIState: MutableState<UiState<DetectionResponse, MyAPIErrors>> =
        mutableStateOf(UiState())

    val translateLanguagesUIState: MutableState<UiState<List<Language>, MyAPIErrors>> =
        mutableStateOf(UiState())


    fun loadLanguageOptions() {
        getLanguages()
        screenUIState.value = TranslateUIState.Default
    }

    override fun loadPreferredLanguage() {
        launch {
            uiData.translation.translatedLanguage = dataStoreRepository.getTranslationLanguage()
        }
    }

    override fun onOriginalLanguageChange(text: String) {
        uiData.translation.originalLanguage = text
        screenUIState.value = TranslateUIState.TranslationChanged

    }

    override fun onOriginalStringChange(text: String) {
        uiData.translation.originalString = text
        screenUIState.value = TranslateUIState.TranslationChanged
    }


    override fun onTranslatedLanguageChange(text: String) {
        uiData.translation.translatedLanguage = text
        screenUIState.value = TranslateUIState.TranslationChanged
    }

    override fun onTranslatedStringChange(text: String) {
        uiData.translation.translatedString = text
        screenUIState.value = TranslateUIState.TranslationChanged
    }

    override fun onImageChanged(image: Uri?) {
        if (image != null) {
            uiData.selectedImageURI = image
            uiData.translation.image = image.toString()
        } else {
            uiData.selectedImageURI = Uri.EMPTY
            uiData.translation.image = Uri.EMPTY.toString()
        }
        screenUIState.value = TranslateUIState.TranslationChanged

    }

    override fun onSaveOptionChanged(option: Int) {
        uiData.selectedSaveOption = option
        screenUIState.value = TranslateUIState.TranslationChanged
    }

    override fun getPlaceholderContentText(
    ): Int? {
        val translateLanguageError = translateLanguagesUIState.value.errors?.communicationError

        return when {
            translateLanguageError != null -> {

                translateLanguageError

            }

            else -> null
        }
    }

    override fun saveTranslationToList(): Long? {
        uiErrors = TranslateErrors()
        var save = true
        // can be empty
        /*
        if (uiData.translation.originalLanguage.isEmpty()) {
            uiErrors.originalLanguageError = R.string.cannot_be_empty
            save = false
        }
         */
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
        return if (save) {
            addTranslationToList()
        } else {
            screenUIState.value = TranslateUIState.ErrorOccurred
            null
        }
    }


    override fun saveTranslation() {
        when (uiData.selectedSaveOption) {
            0 -> {
                uiData.translation.visible = false
                screenUIState.value = TranslateUIState.SavedToList
            }

            1 -> {
                uiData.translation.visible = true
                screenUIState.value = TranslateUIState.SavedToMap
            }

            2 -> {
                screenUIState.value = TranslateUIState.Discarded
            }
        }
    }


    override fun detectLanguage() {
        launch {
            try {
                val request = DetectionRequest(
                    textToDetect = uiData.translation.originalString,
                )

                val result = withContext(Dispatchers.IO) {
                    libreTranslateRemoteRepositoryImpl.detectLanguage(request)
                }
                when (result) {
                    is CommunicationResult.ConnectionError -> {
                        detectUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = MyAPIErrors(communicationError = R.string.no_internet_connection)
                        )
                    }

                    is CommunicationResult.Error -> {
                        detectUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = MyAPIErrors(communicationError = R.string.language_detection_fail)
                        )
                    }

                    is CommunicationResult.Exception -> {
                        detectUIState.value = UiState(
                            loading = false,
                            data = null,
                            errors = MyAPIErrors(communicationError = R.string.unknown_error)
                        )
                    }

                    is CommunicationResult.Success -> {
                        // Directly assign the list of DetectionResult to detectUIState
                        detectUIState.value =
                            UiState(loading = false, data = result.data, errors = null)
                        // Optionally, if you want to use the most confident language
                        val mostConfidentResult = result.data.maxByOrNull { it.confidence }

                        mostConfidentResult?.let {
                            onOriginalLanguageChange(it.language)
                        }

                    }
                }
            } catch (e: Exception) {
                // Handle general exceptions
                detectUIState.value = UiState(
                    loading = false,
                    null,
                    MyAPIErrors(communicationError = R.string.unknown_error)
                )

            }
            screenUIState.value = TranslateUIState.TranslationChanged

        }
        screenUIState.value = TranslateUIState.TranslationChanged

    }

    override fun translate() {
        uiErrors = TranslateErrors()
        var translate = true
        if (uiData.translation.originalString.isEmpty()) {
            uiErrors.originalStringError = R.string.cannot_be_empty
            translate = false
        }
        if (uiData.translation.translatedLanguage.isEmpty()) {
            uiErrors.translatedLanguageError = R.string.cannot_be_empty
            translate = false
        }
        if (!translate) {
            screenUIState.value = TranslateUIState.ErrorOccurred
        } else {
            uiErrors = TranslateErrors()
            screenUIState.value = TranslateUIState.ErrorOccurred
            launch {
                translateUIState.value = UiState(loading = true)
                try {
                    val sourceLang = uiData.translation.originalLanguage.ifEmpty {
                        "auto"
                    }

                    val request = TranslationRequest(
                        sourceLanguage = sourceLang,
                        textToTranslate = uiData.translation.originalString,
                        targetLanguage = uiData.translation.translatedLanguage
                    )

                    val result = withContext(Dispatchers.IO) {
                        libreTranslateRemoteRepositoryImpl.translate(request)
                    }


                    when (result) {
                        is CommunicationResult.ConnectionError -> {
                            translateUIState.value = UiState(
                                loading = false,
                                null,
                                MyAPIErrors(communicationError = R.string.no_internet_connection)
                            )
                        }

                        is CommunicationResult.Error -> {
                            translateUIState.value = UiState(
                                loading = false,
                                null,
                                MyAPIErrors(communicationError = R.string.translate_failed)
                            )
                        }

                        is CommunicationResult.Exception -> {
                            translateUIState.value = UiState(
                                loading = false,
                                null,
                                errors = MyAPIErrors(communicationError = R.string.unknown_error)
                            )
                        }

                        is CommunicationResult.Success -> {
                            translateUIState.value =
                                UiState(loading = false, data = result.data, errors = null)
                            onTranslatedStringChange(result.data.translatedText)
                        }
                    }
                } catch (e: Exception) {
                    // Handle general exceptions
                    translateUIState.value = UiState(
                        loading = false,
                        null,
                        MyAPIErrors(communicationError = R.string.unknown_error)
                    )

                }
                screenUIState.value = TranslateUIState.TranslationEnded
            }
        }
    }


    override fun addTranslationToList(): Long {


        var translationId = 0L // Initialize a variable to store the ID
        setTime()
        runBlocking {
            val deferred = async {
                translationsRepository.insert(uiData.translation)
            }
            translationId = deferred.await()
        }

        return translationId
    }

    private fun setTime() {
        uiData.translation.date = Calendar.getInstance().timeInMillis
        screenUIState.value = TranslateUIState.TranslationChanged
    }


    override fun addTranslationToMap(): Long {
        uiData.translation.visible = true
        return addTranslationToList()
    }

    override fun discardTranslation() {
    }


    override fun getUserCoords() {
        launch {
            val coords: Location? = locationTracker.getCurrentLocation()
            if (coords != null) {
                onCoordChange(coords)
            }
        }
        screenUIState.value = TranslateUIState.TranslationChanged

    }

    override fun onCoordChange(coords: Location) {
        uiData.translation.latitude = coords.latitude
        uiData.translation.longitude = coords.longitude
        screenUIState.value = TranslateUIState.TranslationChanged
    }

    private fun getLanguages() {
        launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    libreTranslateRemoteRepositoryImpl.getLanguages()
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
                        errors = MyAPIErrors(communicationError = R.string.language_options_fail)
                    )

                    is CommunicationResult.Exception -> translateLanguagesUIState.value = UiState(
                        loading = false,
                        null,
                        errors = MyAPIErrors(communicationError = R.string.unknown_error)

                    )

                    is CommunicationResult.Success -> {

                        translateLanguagesUIState.value =
                            UiState(loading = false, data = result.data, errors = null)
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


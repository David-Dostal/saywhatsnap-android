package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.map_screen

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.location.ILocationTracker
import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.UiState
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.MyDatabaseErrors
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val translationsRepository: ITranslationsRepository,
    private val locationTracker: ILocationTracker
) : BaseViewModel(), MapActions {

    var uiData: MapScreenData = MapScreenData()

    val screenUIState: MutableState<MapUIState> =
        mutableStateOf(MapUIState.Loading)

    val mapScreenUIState: MutableState<UiState<List<TranslationEntity>, MyDatabaseErrors>> =
        mutableStateOf(
            UiState(loading = true)
        )

    val categoriesUIState: MutableState<UiState<List<String>, MyDatabaseErrors>> = mutableStateOf(
        UiState(loading = true)
    )


    fun loadAllPlaces() {
        try {
            launch {
                translationsRepository.getVisibleTranslations().collect {
                    mapScreenUIState.value = UiState(data = it, errors = null, loading = false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mapScreenUIState.value = UiState(
                data = null,
                errors = MyDatabaseErrors(communicationError = R.string.error_loading_data),
                loading = false
            )
        }
    }

    override fun getPlaceholderContentText(
    ): Int? {
        val getCategoriesError = categoriesUIState.value.errors?.communicationError
        val getTranslationError = mapScreenUIState
            .value.errors?.communicationError

        return when {
            getCategoriesError != null -> {

                getCategoriesError

            }

            getTranslationError != null -> {
                getTranslationError
            }

            else -> null
        }
    }

    override fun getUserCoords() {
        launch {
            val coords: Location? = locationTracker.getCurrentLocation()
            if (coords != null) {
                onCoordChange(coords)
            }
        }
    }

    override fun getLongitude(): Double {
        return  uiData.longitude
    }

    override fun getLatitude(): Double {
        return uiData.latitude
    }

    override fun onCoordChange(coords: Location) {
        uiData.latitude = coords.latitude
        uiData.longitude = coords.longitude
        screenUIState.value = MapUIState.FilterChanged
    }

    fun getAllCategories() {
        try {
            launch {
                translationsRepository.getVisibleCategories().collect {
                    categoriesUIState.value = UiState(data = it, errors = null, loading = false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            categoriesUIState.value = UiState(
                data = null,
                errors = MyDatabaseErrors(communicationError = R.string.error_loading_data),
                loading = false
            )
        }
    }

    override fun changeSelectedCategory(category: String) {
        uiData.selectedCategory = category
        getPlacesPerSelectedCategory()
    }

    private fun getPlacesPerSelectedCategory() {
        if (uiData.selectedCategory == "") {
            loadAllPlaces()
        } else {
            launch {
                try {
                    translationsRepository.getVisibleTranslationsByCategory(category = uiData.selectedCategory)
                        .collect {
                            mapScreenUIState.value =
                                UiState(data = it, errors = null, loading = false)
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                    mapScreenUIState.value = UiState(
                        data = null,
                        errors = MyDatabaseErrors(communicationError = R.string.error_loading_data),
                        loading = false
                    )
                }
            }
        }
    }
}
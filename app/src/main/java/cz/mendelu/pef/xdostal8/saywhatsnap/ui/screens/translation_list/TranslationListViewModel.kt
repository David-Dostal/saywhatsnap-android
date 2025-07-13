package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xdostal8.saywhatsnap.model.UiState
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TranslationListViewModel @Inject constructor(
    private val translationsRepository: ITranslationsRepository,
) : BaseViewModel(), TranslationListActions {

    var uiData: TranslationListData = TranslationListData()
    val transUIState: MutableState<UiState<List<TranslationEntity>, Error>> = mutableStateOf(
        UiState(loading = true)
    )


    init {
        launch {
            //addRandomTranslations()
            loadTranslations()
        }
    }

    private fun loadTranslations() {
        launch {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = uiData.date
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val dayStart = calendar.timeInMillis

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            val dayEnd = calendar.timeInMillis

            translationsRepository.getTranslationsForDay(dayStart, dayEnd).collect {
                transUIState.value = UiState(data = it, errors = null, loading = false)
            }
        }
    }

    override fun changeSelectedDate(date: Long) {
        launch {
            uiData.date = date
            loadTranslations()
        }
    }
    /*
     fun addRandomTranslations() {
        launch(Dispatchers.IO) {
            val randomTranslations = (1..10).map {
                TranslationEntity(
                    originalString = "Original ${Random.nextInt()}",
                    originalLanguage = "cz",
                    translatedString = "Translated ${Random.nextInt()}",
                    translatedLanguage = "en",
                    date = Calendar.getInstance().timeInMillis,
                    image = Uri.EMPTY.toString(),
                    name = "Name ${Random.nextInt()}",
                    latitude = Random.nextDouble(
                        48.55, 51.06
                    ), // Approximate range for Czech Republic
                    longitude = Random.nextDouble(
                        12.09, 18.86
                    ), // Approximate range for Czech Republic
                    category = "Category ${Random.nextInt()}",
                    description = "Description ${Random.nextInt()}",
                    visible = true
                )
            }
            randomTranslations.forEach { translationsRepository.insert(it) }


        }


    }



     */
}
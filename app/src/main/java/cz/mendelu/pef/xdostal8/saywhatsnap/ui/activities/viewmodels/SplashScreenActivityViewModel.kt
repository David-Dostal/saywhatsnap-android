package cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.viewmodels

import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.states.SplashScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenActivityViewModel @Inject constructor(
    private val dataStoreRepository: IDataStoreRepository
) : BaseViewModel() {

    private val _splashScreenState =
        MutableStateFlow<SplashScreenUiState>(SplashScreenUiState.Default)
    val splashScreenState: StateFlow<SplashScreenUiState> = _splashScreenState

    fun checkAppState() {
        launch {
            if (dataStoreRepository.getFirstRun()) {
                if (dataStoreRepository.getLanguage().isEmpty()) {
                    dataStoreRepository.setLanguage("en")
                }
                if (dataStoreRepository.getTranslationLanguage().isEmpty()) {
                    dataStoreRepository.setLanguage("cs")
                }

                _splashScreenState.value = SplashScreenUiState.RunForAFirstTime
            } else {
                _splashScreenState.value = SplashScreenUiState.ContinueToApp
            }

        }
    }
}

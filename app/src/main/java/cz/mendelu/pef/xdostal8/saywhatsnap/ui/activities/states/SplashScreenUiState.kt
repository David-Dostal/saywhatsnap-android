package cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.states

sealed class SplashScreenUiState {
    object Default : SplashScreenUiState()
    object RunForAFirstTime : SplashScreenUiState()
    object ContinueToApp : SplashScreenUiState()

}
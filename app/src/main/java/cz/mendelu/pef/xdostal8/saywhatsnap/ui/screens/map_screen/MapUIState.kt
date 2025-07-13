package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.map_screen

sealed class MapUIState {
    object Loading : MapUIState()
    object Default : MapUIState()
    object FilterChanged : MapUIState()
}

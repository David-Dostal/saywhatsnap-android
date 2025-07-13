package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.map_screen

import android.location.Location

interface MapActions {
    fun changeSelectedCategory(category: String)
    fun getPlaceholderContentText(
    ): Int?

    fun onCoordChange(coords: Location)
    fun getUserCoords()

    fun getLongitude(): Double
    fun getLatitude(): Double
}
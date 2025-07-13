package cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xFF6650a4)
val SecondaryColor = Color(0xFFD0BCFF)

// Dark mode primary colors
val PrimaryColorDark = Color(0xFFD0BCFF)
val SecondaryColorDark = Color(0xFF6650a4)


// Basic colors
val LightTextColor = Color(0xFF000000)
val DarkTextColor = Color(0xFFFFFFFF)


/**
 * The methods for returning colors. Usually, the color is return
 * based on dark mode.
 */
@Composable
fun getTintColor() = if (isSystemInDarkTheme()) Color.White else Color.Black

@Composable
fun getCurrentPrimaryColor(): Color = if (isSystemInDarkTheme()) PrimaryColor else PrimaryColorDark


@Composable
fun getCurrentSecondaryColor(): Color =
    if (isSystemInDarkTheme()) SecondaryColorDark else SecondaryColor

@Composable
fun getBackGroundColor(): Color = if (isSystemInDarkTheme()) LightTextColor else DarkTextColor


@Composable
fun getBasicTextColor(): Color = if (isSystemInDarkTheme()) DarkTextColor else LightTextColor

@Composable
fun getBarTitleColor(): Color = if (isSystemInDarkTheme()) PrimaryColorDark else PrimaryColor

package cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.MapScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TextRecognitionScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination


@Composable
fun MyBottomBar(currentDestinationPosition: Int, navigator: DestinationsNavigator) {
    NavigationBar {
        BottomBarDestination.values().forEach { destination ->
            NavigationBarItem(
                selected = currentDestinationPosition == destination.destinationPosition,
                onClick = {
                    navigator.navigate(destination.direction)
                },
                icon = { destination.IconContent() },
                label = { Text(stringResource(destination.label)) }
            )
        }
    }
}


enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: Any,
    val label: Int,
    val destinationPosition: Int,
    val isDrawable: Boolean = false
) {
    List(TranslationListScreenDestination, Icons.Default.List, R.string.list, 0),
    Translate(
        TextRecognitionScreenDestination,
        R.drawable.translate_icon,
        R.string.translate,
        1,
        isDrawable = true
    ),
    Map(MapScreenDestination, Icons.Default.Place, R.string.map, 2);

    @Composable
    fun IconContent() {
        if (isDrawable) {
            Icon(
                painter = painterResource(id = icon as Int),
                contentDescription = stringResource(label)
            )
        } else {
            Icon(imageVector = icon as ImageVector, contentDescription = stringResource(label))
        }
    }
}
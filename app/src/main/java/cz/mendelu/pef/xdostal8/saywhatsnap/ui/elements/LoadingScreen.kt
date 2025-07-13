package cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.getBackGroundColor
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.getCurrentPrimaryColor

@Composable
fun LoadingScreen(
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(getBackGroundColor()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = getCurrentPrimaryColor(),
            strokeWidth = 5.dp
        )
    }
}
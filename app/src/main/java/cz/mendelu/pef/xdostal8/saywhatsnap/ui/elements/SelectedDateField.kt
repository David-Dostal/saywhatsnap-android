package cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun SelectedDateField(
    modifier: Modifier = Modifier,
    value: String?,
    onClick: () -> Unit,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    LocalFocusManager.current

    if (isPressed) {
        LaunchedEffect(isPressed) {
            onClick()
        }
    }

    TextButton(
        onClick = { }, modifier = Modifier.fillMaxWidth(), interactionSource = interactionSource
    ) {
        Text(
            text = value ?: "",
            textDecoration = TextDecoration.Underline,
        )
    }

}
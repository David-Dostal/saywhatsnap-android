package cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.smallMargin

const val TestTextFieldWithinAutocompleteTag = "TestTextFieldWithinAutocompleteTag"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchableDropdownMenu(
    options: List<String>?,
    currentValue: String,
    onValueChange: (String) -> Unit,
    label: Int,
    error: String,
    toastText: String? = null,
    testTag: String? = null,
    modifier: Modifier? = Modifier

) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(currentValue) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    val isTextFieldFocused by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = expanded) {
        if (!expanded && isTextFieldFocused) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,


            onExpandedChange = {
                onValueChange(selectedText)
                keyboardController?.hide()
                expanded = !expanded
            }
        ) {
            MyTextField(
                value = selectedText,
                label = stringResource(label),
                onValueChange = { selectedText = it },
                readOnly = false,
                testTag = testTag + TestTextFieldWithinAutocompleteTag,
                error = "",
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag(testTag + TestTextFieldWithinAutocompleteTag)
            )

            val filteredOptions =
                options?.filter { it.contains(selectedText, ignoreCase = true) }
            if (filteredOptions != null) {
                if (filteredOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                        },

                    ) {
                        filteredOptions.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = item,
                                    )
                                },
                                onClick = {
                                    selectedText = item
                                    expanded = false
                                    onValueChange(item)
                                    keyboardController?.hide()
                                    if (toastText != null) {
                                        Toast.makeText(
                                            context,
                                            "$toastText: $item",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
    Text(
        text = error,
        modifier = Modifier
            .alpha(if (error.isNotEmpty()) 1f else 0f)
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, smallMargin()),
        color = Color.Red,
        textAlign = TextAlign.Start,
        fontSize = 11.sp
    )
}

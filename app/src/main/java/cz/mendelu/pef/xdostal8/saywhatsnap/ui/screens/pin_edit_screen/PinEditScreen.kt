package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyBaseScreen
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.basicMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.roundedCorner

const val TestSaveButton = "TestSaveButton"
const val TestNameTextField = "TestNameTextField"
const val TestDescriptionTextField = "TestDescriptionTextField"
const val TestCategoryTextField = "TestCategoryTextField"

@Destination
@Composable
fun PinEditScreen(
    navigator: DestinationsNavigator, translationID: Long
) {

    val viewModel = hiltViewModel<PinEditViewModel>()

    var uiData: PinEditData by remember { mutableStateOf(viewModel.uiData) }

    var uiErrors: PinEditErrors by remember { mutableStateOf(viewModel.uiErrors) }

    viewModel.screenUIState.value.let {
        when (it) {
            PinEditUIState.Default -> {

            }

            PinEditUIState.Loading -> {
                LaunchedEffect(it) {
                    viewModel.loadTranslation(translationID = translationID)
                }
            }

            PinEditUIState.TranslationChanged -> {
                uiData = viewModel.uiData
                viewModel.screenUIState.value = PinEditUIState.Default
            }

            PinEditUIState.TranslationSaved -> {
                LaunchedEffect(it) {
                    uiErrors = viewModel.uiErrors
                    navigator.navigate(TranslationListScreenDestination)
                }
            }

            PinEditUIState.ErrorOccurred -> {
                uiErrors = viewModel.uiErrors
                viewModel.screenUIState.value = PinEditUIState.Default
            }
        }
    }

    MyBaseScreen(topBarText = stringResource(id = R.string.title_edit_pin_info),
        showLoading = viewModel.screenUIState.value == PinEditUIState.Loading,
        drawFullScreenContent = false,
        onBackClick = { navigator.popBackStack() },
        currentDestinationPosition = 1,
        navigator = navigator,
        actions = {
            FilledIconButton(onClick = {
                navigator.navigate(TranslationListScreenDestination)
                viewModel.deleteTranslation()


            }) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete_translation_description)
                )
            }
        }) {
        PinEditContent(
            translation = uiData.translation,
            actions = viewModel,
            uiErrors = uiErrors
        )
    }
}


@Composable
fun PinEditContent(
    translation: TranslationEntity,
    actions: PinEditActions,
    uiErrors: PinEditErrors?

) {
    MyTextField(
        value = translation.name,
        label = stringResource(R.string.hint_name),
        onValueChange = {
            actions.onNameChange(it)
        },
        testTag = TestNameTextField,
        modifier = Modifier.testTag(TestNameTextField),
        error = if (uiErrors?.nameError != null) stringResource(id = uiErrors.nameError.hashCode()) else ""
    )


    MyTextField(
        value = translation.category,
        label = stringResource(R.string.hint_category),
        onValueChange = {
            actions.onCategoryChange(it)
        },
        testTag = TestCategoryTextField,
        modifier = Modifier.testTag(TestCategoryTextField),
        error = if (uiErrors?.categoryError != null) stringResource(id = uiErrors.categoryError.hashCode()) else ""
    )

    MyTextField(
        value = translation.description,
        label = stringResource(R.string.hint_description),
        modifier = Modifier.testTag(TestDescriptionTextField),
        testTag = TestDescriptionTextField,
        onValueChange = {
            actions.onDescriptionChange(it)
        },
        error = if (uiErrors?.descriptionError != null) stringResource(id = uiErrors.descriptionError.hashCode()) else ""
    )

    Spacer(modifier = Modifier.height(basicMargin()))


    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestSaveButton),
        onClick = {
            actions.saveTranslation()
        }, shape = RoundedCornerShape(roundedCorner()), colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(R.string.save))
    }

    Spacer(modifier = Modifier.height(basicMargin()))

    Spacer(modifier = Modifier.height(basicMargin()))


}

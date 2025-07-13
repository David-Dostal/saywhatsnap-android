package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyBaseScreen
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.PinEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.basicMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.halfMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.roundedCorner

const val TestOrigLangTextField = "TestOrigLangTextField"
const val TestOrigStringTextField = "TestOrigStringTextField"
const val TestTransLangTextField = "TestTransLangTextField"
const val TestTransStringTextField = "TestTransStringTextField"
const val TestSaveButton = "TestSaveButton"

@Destination
@Composable
fun TranslationEditScreen(
    navigator: DestinationsNavigator, translationID: Long
) {
    val viewModel = hiltViewModel<TranslationEditViewModel>()

    var uiData: TranslationEditData by remember { mutableStateOf(viewModel.uiData) }

    var uiErrors: TranslationEditErrors by remember { mutableStateOf(viewModel.uiErrors) }

    viewModel.screenUIState.value.let {
        when (it) {
            TranslationEditUIState.Default -> {

            }

            TranslationEditUIState.Loading -> {
                LaunchedEffect(it) {
                    viewModel.loadTranslation(translationID = translationID)
                }
            }

            TranslationEditUIState.TranslationChanged -> {
                uiData = viewModel.uiData
                viewModel.screenUIState.value = TranslationEditUIState.Default
            }


            TranslationEditUIState.TranslationSaved -> {
                LaunchedEffect(it) {
                    uiErrors = viewModel.uiErrors
                    navigator.navigate(TranslationListScreenDestination)
                }
            }

            TranslationEditUIState.ErrorOccurred -> {
                uiErrors = viewModel.uiErrors
                viewModel.screenUIState.value = TranslationEditUIState.Default
            }

            TranslationEditUIState.ForwardTranslation -> {
                LaunchedEffect(it) {
                    uiErrors = viewModel.uiErrors
                    navigator.navigate(PinEditScreenDestination(translationID = viewModel.uiData.translation.id!!))
                }
            }
        }
    }


    MyBaseScreen(topBarText = stringResource(id = R.string.title_edit_trans),
        showLoading = viewModel.screenUIState.value == TranslationEditUIState.Loading,
        drawFullScreenContent = false,
        onBackClick = { navigator.popBackStack() },
        placeholderScreenContent = null,
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
        TranslationEditScreenContent(
            translationParams = uiData.translation,
            errors = uiErrors,
            imageUri = uiData.translation.image,
            actions = viewModel
        )
    }
}

@Composable
fun TranslationEditScreenContent(
    translationParams: TranslationEntity,
    imageUri: String,
    errors: TranslationEditErrors,
    actions: TranslationEditActions,
) {
    MyTextField(
        value = translationParams.originalLanguage,
        modifier = Modifier.testTag(TestOrigLangTextField),
        label = stringResource(R.string.hint_original_language),
        onValueChange = {
            actions.onOriginalLanguageChange(it)
        },
        testTag = TestOrigLangTextField,
        error = if (errors.originalLanguageError != null) stringResource(id = errors.originalLanguageError.hashCode()) else ""

    )


    MyTextField(
        value = translationParams.originalString,
        modifier = Modifier.testTag(TestOrigStringTextField),
        label = stringResource(R.string.hint_original_string),
        onValueChange = {
            actions.onOriginalStringChange(it)
        },
        testTag = TestOrigStringTextField,
        error = if (errors.originalStringError != null) stringResource(id = errors.originalStringError.hashCode()) else ""
    )

    MyTextField(
        value = translationParams.translatedLanguage,
        modifier = Modifier.testTag(TestTransLangTextField),
        label = stringResource(R.string.hint_translated_language),
        onValueChange = {
            actions.onTranslatedLanguageChange(it)
        },
        testTag = TestTransLangTextField,
        error = if (errors.translatedLanguageError != null) stringResource(id = errors.translatedLanguageError.hashCode()) else ""
    )

    MyTextField(
        value = translationParams.translatedString,
        modifier = Modifier.testTag(TestTransStringTextField),
        label = stringResource(R.string.hint_translated_string),
        onValueChange = {
            actions.onTranslatedStringChange(it)
        },
        testTag = TestTransStringTextField,
        error = if (errors.translatedStringError != null) stringResource(id = errors.translatedStringError.hashCode()) else ""
    )
    Spacer(modifier = Modifier.height(basicMargin()))


    SelectImage(
        context = LocalContext.current, actions = actions, currentImageString = imageUri
    )
    Spacer(modifier = Modifier.height(basicMargin()))
    Spacer(modifier = Modifier.height(basicMargin()))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            stringResource(id = R.string.map_visibility),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(halfMargin()))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { actions.onVisibleOnMapChange(false) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = if (translationParams.visible) Color.Gray else Color.Red)
            ) {
                Text(text = stringResource(R.string.hint_not_visible))
            }
            Switch(checked = translationParams.visible,
                onCheckedChange = { actions.onVisibleOnMapChange(it) })
            OutlinedButton(
                onClick = { actions.onVisibleOnMapChange(true) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = if (translationParams.visible) Color.Green else Color.Gray)
            ) {
                Text(text = stringResource(R.string.hint_visible))
            }
        }
    }
    Spacer(modifier = Modifier.height(basicMargin()))


    Spacer(modifier = Modifier.height(basicMargin()))

    if (translationParams.visible) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            OutlinedButton(

                onClick = {
                    actions.forwardTranslation()
                },
                shape = RoundedCornerShape(roundedCorner()),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = stringResource(R.string.edit_pin_info_button))
            }
        }
        Spacer(modifier = Modifier.height(basicMargin()))
        Spacer(modifier = Modifier.height(basicMargin()))
    }

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestSaveButton),
        onClick = { actions.saveTranslation() },
        shape = RoundedCornerShape(roundedCorner()), colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(R.string.save))
    }

}


@Composable
fun DisplayImageFromGallery(
    imageUri: Uri, onUnselectImage: () -> Unit
) {
    Spacer(modifier = Modifier.height(basicMargin()))

    Image(
        painter = rememberImagePainter(imageUri),
        contentDescription = stringResource(id = R.string.image_from_gallery),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.FillWidth
    )

    Spacer(modifier = Modifier.height(basicMargin()))

    OutlinedButton(
        onClick = { onUnselectImage() },
        shape = RoundedCornerShape(roundedCorner()),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text(text = stringResource(R.string.unselect_image_button))
    }
}

@Composable
fun SelectImage(context: Context, actions: TranslationEditActions, currentImageString: String) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    if (currentImageString != Uri.EMPTY.toString()) {
        selectedImageUri = Uri.parse(currentImageString)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                val contentResolver = context.contentResolver
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(it, takeFlags)
                selectedImageUri = it
                actions.onImageChanged(uri)
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedImageUri == null) {
            OutlinedButton(
                onClick = {
                    launcher.launch(arrayOf("image/*"))
                },
                shape = RoundedCornerShape(roundedCorner()),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = stringResource(R.string.select_image_button))
            }
        }

        selectedImageUri?.let { uri ->
            DisplayImageFromGallery(uri) {
                selectedImageUri = null // Unselect the image
                actions.onImageChanged(null) // Notify the parent composable about the unselect event
            }
        }
    }
}
package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.Language
import cz.mendelu.pef.xdostal8.saywhatsnap.model.api.MyAPIErrors
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyBaseScreen
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MySearchableDropdownMenu
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.PermissionsRequestDialog
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.PinEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.SettingsScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.basicMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.roundedCorner

const val TestCordsTextField = "TestCordsTextField"
const val TestCordsButton = "TestCordsButton"
const val TestTranslateButton = "TestTranslateButton"
const val TestContinueButton = "TestContinueButton"
const val TestOrigStringTextField = "TestOrigStringTextField"
const val TestTransStringTextField = "TestTransStringTextField"
const val TestOrigLangAutocomplete = "TestOrigLangAutocomplete"
const val TestTransLangAutocomplete = "TestOTransLangAutocomplete"

@Destination
@Composable
fun TranslateScreen(
    navigator: DestinationsNavigator,
    extractedText: String,
    imageUri: Uri?
) {
    val viewModel = hiltViewModel<TranslateViewModel>()

    val languagesUIState by remember { mutableStateOf(viewModel.translateLanguagesUIState) }

    val detectUiState by remember { mutableStateOf(viewModel.detectUIState) }

    val translateUIState by remember { mutableStateOf(viewModel.translateUIState) }

    var uiData: TranslateData by remember { mutableStateOf(viewModel.uiData) }

    var uiErrors: TranslateErrors by remember { mutableStateOf(viewModel.uiErrors) }

    viewModel.screenUIState.value.let {
        when (it) {
            TranslateUIState.Default -> {

            }

            TranslateUIState.Loading -> {
                LaunchedEffect(it) {
                    viewModel.loadPreferredLanguage()
                    viewModel.onOriginalStringChange(extractedText)
                    viewModel.onImageChanged(imageUri)
                    viewModel.loadLanguageOptions()
                    viewModel.detectLanguage()

                }
            }

            TranslateUIState.TranslationChanged -> {
                uiData = viewModel.uiData
                viewModel.screenUIState.value = TranslateUIState.Default
            }

            TranslateUIState.Discarded -> {
                navigator.navigate(TranslationListScreenDestination)
            }

            TranslateUIState.SavedToList -> {
                val savedID = viewModel.saveTranslationToList()
                if (savedID != null) {
                    navigator.navigate(TranslationListScreenDestination)
                }
            }

            TranslateUIState.SavedToMap -> {
                val savedID = viewModel.saveTranslationToList()
                if (savedID != null) {
                    navigator.navigate(PinEditScreenDestination(translationID = savedID))
                }
            }

            TranslateUIState.ErrorOccurred -> {
                uiErrors = viewModel.uiErrors
                viewModel.screenUIState.value = TranslateUIState.Default
            }

            TranslateUIState.TranslationEnded -> {
                viewModel.detectLanguage()
            }
        }

    }

    MyBaseScreen(topBarText = stringResource(id = R.string.parameters_title),
        showLoading = languagesUIState.value.loading || detectUiState.value.loading,
        drawFullScreenContent = false,
        onBackClick = { navigator.popBackStack() },
        placeholderScreenContent =
        if (viewModel.getPlaceholderContentText() != null) {
            PlaceholderScreenContent(
                null,
                text = stringResource(id = viewModel.getPlaceholderContentText()!!)
            )
        } else null,
        currentDestinationPosition = 1,
        navigator = navigator,
        actions = {
            FilledIconButton(onClick = {
                navigator.navigate(SettingsScreenDestination)
            }) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.settings_description)
                )
            }
        }) {
        TranslateScreenContent(
            translationParams = uiData.translation,
            languageOptions = languagesUIState.value.data,
            saveOption = uiData.selectedSaveOption,
            uiErrors = uiErrors,
            detectErrors = detectUiState.value.errors,
            languageErrors = languagesUIState.value.errors,
            isTranslating = translateUIState.value.loading,
            actions = viewModel
        )
    }
}

@Composable
fun TranslateScreenContent(
    translationParams: TranslationEntity,
    saveOption: Int,
    languageOptions: List<Language>?,
    uiErrors: TranslateErrors,
    detectErrors: MyAPIErrors?,
    languageErrors: MyAPIErrors?,
    isTranslating: Boolean,
    actions: TranslateActions

) {
    val languageCodes = languageOptions?.map { it.code.orEmpty() } ?: listOf()

    PermissionsRequestDialog(
        icon = Icons.Filled.LocationOn,
        title = stringResource(R.string.permissions_location),
        message = stringResource(R.string.permissions_location_message),
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        onPermissionsGranted = {
            actions.getUserCoords()
        }
    )

    MyTextField(
        value = translationParams.latitude.toString() + ". " + translationParams.longitude.toString(),
        label = stringResource(R.string.coords),
        onValueChange = {
            actions.onOriginalStringChange(it)
        },
        readOnly = true,
        testTag = TestCordsTextField,
        error = "",
        modifier = Modifier.testTag(TestCordsTextField)
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedButton(
            modifier = Modifier.testTag(TestCordsButton),
            onClick = { actions.getUserCoords() },
            shape = RoundedCornerShape(roundedCorner()),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White, containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = stringResource(R.string.get_coords))
        }
    }

    Spacer(modifier = Modifier.height(basicMargin()))

    languageOptions?.let {


        MySearchableDropdownMenu(
            options = listOf("") + languageCodes,
            currentValue = translationParams.originalLanguage,
            onValueChange = { newLang ->
                translationParams.originalLanguage = newLang
                actions.onOriginalLanguageChange(newLang)
            },
            testTag = TestOrigLangAutocomplete,
            modifier = Modifier.testTag(TestOrigLangAutocomplete),
            label = R.string.hint_original_language,
            error = if (detectErrors?.communicationError != null) stringResource(id = detectErrors.communicationError.hashCode()) else ""
        )
    }


    MyTextField(
        value = translationParams.originalString,
        testTag = TestOrigStringTextField,
        modifier = Modifier.testTag(TestOrigStringTextField),
        label = stringResource(R.string.hint_extracted),
        onValueChange = {
            actions.onOriginalStringChange(it)
        },
        error = if (uiErrors.originalStringError != null) stringResource(id = uiErrors.originalStringError.hashCode()) else ""
    )


    languageOptions?.let {

        MySearchableDropdownMenu(
            options = languageCodes,
            testTag = TestTransLangAutocomplete,
            modifier = Modifier.testTag(TestTransLangAutocomplete),
            currentValue = translationParams.translatedLanguage,
            onValueChange = { newLang ->
                translationParams.translatedLanguage = newLang
                actions.onTranslatedLanguageChange(newLang)
            },
            label = R.string.hint_translated_language,
            error =
            if (uiErrors.translatedLanguageError != null) stringResource(id = uiErrors.translatedLanguageError.hashCode())
            else if (languageErrors?.communicationError != null) stringResource(id = languageErrors.communicationError.hashCode())
            else ""
        )

    }




    MyTextField(
        value = translationParams.translatedString,
        modifier = Modifier.testTag(TestTransStringTextField),
        testTag = TestTransStringTextField,
        label = stringResource(R.string.hint_translated_string),
        onValueChange = {
            actions.onTranslatedStringChange(it)
        },
        // on translation api error placeholder will be shown
        error = if (uiErrors.translatedStringError != null) stringResource(id = uiErrors.translatedStringError.hashCode())
        else ""

    )

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTranslateButton),
        onClick = {
            actions.translate()
        },
        shape = RoundedCornerShape(roundedCorner()),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (isTranslating) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            // Display text when isLoading is false
            Text(text = stringResource(R.string.translate))
        }
    }


    Spacer(modifier = Modifier.height(basicMargin()))
    Spacer(modifier = Modifier.height(basicMargin()))


    SelectImage(
        context = LocalContext.current,
        actions = actions,
        currentImageString = translationParams.image
    )


    Spacer(modifier = Modifier.height(basicMargin()))
    Spacer(modifier = Modifier.height(basicMargin()))

    // Options for the segmented buttons
    val options = listOf(
        stringResource(id = R.string.list),
        stringResource(id = R.string.map),
        stringResource(id = R.string.discard)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            stringResource(id = R.string.save_to_button),
            modifier = Modifier.align(Alignment.Start)
        )


        // Custom styled segmented buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val cornerRadius = 16.dp
            options.forEachIndexed { index, item ->
                val isSelected = when (index) {
                    0 -> saveOption == 0 || saveOption == 1 // "List" is selected if saveOption is 0 or 1
                    1 -> saveOption == 1 // "Map" is selected if saveOption is 1
                    else -> saveOption == 2 // "Discard" is selected if saveOption is 2
                }
                val isDiscard = index == 2
                val testSegmentedButtonTag =
                    "TestSegmentedButton$index" // Unique test tag for each button
                OutlinedButton(
                    onClick = {
                        actions.onSaveOptionChanged(index) // Assuming onSaveOptionChanged updates the saveOption variable
                    },
                    modifier = Modifier
                        .offset((-1 * index).dp, 0.dp)
                        .zIndex(if (isSelected) 1f else 0f)
                        .testTag(testSegmentedButtonTag), // Assign test tag

                    shape = when (index) {
                        0 -> RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius)
                        options.size - 1 -> RoundedCornerShape(
                            topEnd = cornerRadius,
                            bottomEnd = cornerRadius
                        )

                        else -> RoundedCornerShape(0.dp)
                    },
                    border = BorderStroke(
                        1.dp, if (isSelected) {
                            if (isDiscard) Color.Red else MaterialTheme.colorScheme.primary
                        } else {
                            if (isDiscard) Color.Red.copy(alpha = 0.75f) else MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.75f
                            )
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) {
                            if (isDiscard) Color.Red.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            )
                        } else MaterialTheme.colorScheme.surface,
                        contentColor = if (isDiscard) Color.Red else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(item)
                }
            }
        }

        Spacer(modifier = Modifier.height(basicMargin()))
        Spacer(modifier = Modifier.height(basicMargin()))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestContinueButton),
            onClick = {
                actions.saveTranslation()
            }, shape = RoundedCornerShape(roundedCorner()), colors = ButtonDefaults.buttonColors(
                contentColor = Color.White, containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.str_continue))
        }
    }
    Spacer(modifier = Modifier.height(basicMargin()))
    Spacer(modifier = Modifier.height(basicMargin()))

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
fun SelectImage(context: Context, actions: TranslateActions, currentImageString: String) {
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
                selectedImageUri = null
                actions.onImageChanged(null)
            }
        }
    }
}
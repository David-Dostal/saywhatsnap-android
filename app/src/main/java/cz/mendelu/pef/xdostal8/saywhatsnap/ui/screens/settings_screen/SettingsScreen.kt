package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyBaseScreen
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MySearchableDropdownMenu
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.basicMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.roundedCorner
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.smallText
import cz.mendelu.pef.xdostal8.saywhatsnap.BuildConfig
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.PlaceholderScreenContent


import java.util.Locale

const val TestSaveButton = "TestSaveButton"
const val TestAppLangAutocomplete = "TestAppLangAutocomplete"
const val TestTransLangAutocomplete = "TestTransLangAutocomplete"
@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<SettingsViewModel>()

    val languagesUIState by remember { mutableStateOf(viewModel.translateLanguagesUIState) }

    var uiData: SettingsData by remember { mutableStateOf(viewModel.uiData) }

    var uiErrors: SettingErrors by remember { mutableStateOf(viewModel.uiErrors) }


    viewModel.screenUIState.value.let {
        when (it) {
            SettingsScreenUIState.Loading -> {

                LaunchedEffect(it) {
                    viewModel.loadPreferredLanguage()
                    viewModel.loadLanguage()
                    viewModel.getLanguages()
                }

            }

            SettingsScreenUIState.Saved -> {
                SetAppLanguage(language = uiData.language)
                LaunchedEffect(it) {
                    navigator.popBackStack()
                }
            }

            SettingsScreenUIState.SettingsChanged -> {
                uiData = viewModel.uiData
                viewModel.screenUIState.value = SettingsScreenUIState.Default
            }

            SettingsScreenUIState.Default -> {}
            SettingsScreenUIState.ErrorOccurred -> {
                uiErrors = viewModel.uiErrors
                viewModel.screenUIState.value = SettingsScreenUIState.Default
            }
        }
    }


    MyBaseScreen(
        topBarText = stringResource(id = R.string.title_settings),
        showLoading = languagesUIState.value.loading,
        drawFullScreenContent = false,
        onBackClick = { navigator.popBackStack() },
        navigator = navigator,
        placeholderScreenContent = if (languagesUIState.value.errors != null && languagesUIState.value.errors!!.communicationError != null) {
            PlaceholderScreenContent(
                null,
                stringResource(id = languagesUIState.value.errors!!.communicationError!!)
            )
        } else
            null
    ) {
        SettingsScreenContent(
            data = uiData,
            actions = viewModel,
            errors = uiErrors
        )

    }
}


@Composable
fun SettingsScreenContent(
    actions: SettingsActions,
    data: SettingsData?,
    errors: SettingErrors,
) {

    if (data != null) {
        MySearchableDropdownMenu(
            options = data.availableLocalizations,
            currentValue = data.language,
            testTag = TestAppLangAutocomplete,
            modifier = Modifier.testTag(TestAppLangAutocomplete),
            onValueChange = { actions.onLanguageChange(it) },
            label = R.string.hint_language,
            toastText = stringResource(R.string.app_language_selected),
            error = if (errors.appLanguageError != null) stringResource(id = errors.appLanguageError.hashCode()) else ""
        )

        data.languageCodes?.let {
            Spacer(modifier = Modifier.height(basicMargin()))
            MySearchableDropdownMenu(
                options = data.languageCodes!!,
                currentValue = data.translationLanguage,
                testTag = TestTransLangAutocomplete,
                modifier = Modifier.testTag(TestTransLangAutocomplete),
                toastText = stringResource(R.string.translated_language_selected),
                onValueChange = { newLang ->
                    actions.onTranslatedLanguageChange(newLang)
                },
                label = R.string.hint_translated_language,
                error = if (errors.translateLanguageError != null) stringResource(id = errors.translateLanguageError.hashCode()) else ""

            )
        }
    }

    Spacer(modifier = Modifier.height(basicMargin()))
    Spacer(modifier = Modifier.height(basicMargin()))

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestSaveButton),
        onClick = {
            actions.saveChanges()
        },
        shape = RoundedCornerShape(roundedCorner()), colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(R.string.save))
    }

    Spacer(modifier = Modifier.height(basicMargin()))
    Spacer(modifier = Modifier.height(basicMargin()))

    DisplayVersion()
}

@Composable
fun SetAppLanguage(language: String) {
    val context = LocalContext.current
    val locale = Locale(language)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    context.createConfigurationContext(configuration)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}


@Composable
fun DisplayVersion() {
    Column(
        modifier = Modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.version_text),

            textAlign = TextAlign.Center,
            style = smallText(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        val version =
            stringResource(R.string.version_name) + " " + BuildConfig.VERSION_NAME + "\n" + stringResource(
                R.string.version_code
            ) + " " + BuildConfig.VERSION_CODE.toString()

        Text(
            text = version, textAlign = TextAlign.Center, style = smallText(),


            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}
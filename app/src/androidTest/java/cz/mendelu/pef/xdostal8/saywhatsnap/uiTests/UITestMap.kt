package cz.mendelu.pef.xdostal8.saywhatsnap.uiTests

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.ServerMock
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.TranslationsMock
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.MainActivity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestErrorMsgTag
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestTextFieldWithinAutocompleteTag
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.MapScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.SettingsScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestCategoryTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestDescriptionTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestNameTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen.TestAppLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen.TestTransLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestOrigLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestOrigStringTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TestSaveButton
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.SayWhatSnapTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestMap {

    private lateinit var navController: NavHostController

    private var errorMsg: String = "Cannot be empty"


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    // no need to test permissions module...
    @get:Rule(order = 2)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun launchMapDirectly() {
        composeRule.activity.setContent {
            MaterialTheme {
                SayWhatSnapTheme {
                    // Setup the NavHost with the Destinations library
                    navController = rememberNavController()
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.root
                    )
                    // Navigate to the TranslationEditScreen with the desired translationID
                    LaunchedEffect(Unit) {
                        navController.navigate(MapScreenDestination())
                    }
                }
            }
        }
    }

    @Test
    fun catchAndShowUpTranslation() {
        launchMapDirectly()

        with(composeRule) {
            waitForIdle()
            Thread.sleep(4000)

/*
            val selectedTranslation = TranslationsMock.todayTranslation

        composeRule.onNodeWithTag("map").assert(hasText(selectedTranslation.name))

            waitForIdle()


 */
    }
    }


}
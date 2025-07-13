package cz.mendelu.pef.xdostal8.saywhatsnap.uiTests

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.ServerMock
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.MainActivity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestErrorMsgTag
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestTextFieldWithinAutocompleteTag
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.PinEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslateScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestCategoryTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestDescriptionTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestNameTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestContinueButton
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestOrigLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestOrigStringTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestTransLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestTransStringTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestTranslateButton
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
class UITestTranslate {

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

    private fun launchTranslateScreenDirectly() {
        composeRule.activity.setContent {
            MaterialTheme {
                SayWhatSnapTheme {
                    navController = rememberNavController()
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.root
                    )
                    LaunchedEffect(Unit) {
                        navController
                            .navigate(
                                TranslateScreenDestination(
                                    extractedText = ServerMock.extractedText,
                                    imageUri = null
                                )
                            )
                    }
                }
            }
        }
    }

    @Test
    fun testTranslation() {
        launchTranslateScreenDirectly()
        with(composeRule) {
            waitForIdle()

            // assert, that the translation language parameters are loaded from API and Datastore
            onNodeWithTag(TestOrigStringTextField)
                .assert(hasText(ServerMock.extractedText))
            onNodeWithTag(TestOrigLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assert(hasText(ServerMock.appleMockDetectionResponse[0].language))
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assert(hasText(ServerMock.initTransLanguage))

            // translate
            onNodeWithTag(TestTranslateButton).assertIsDisplayed()
            onNodeWithTag(TestTranslateButton).performClick()
            waitForIdle()

            // verify translation result - would not translate if any parameters incorrect
            onNodeWithTag(TestTransStringTextField)
                .assert(hasText(ServerMock.translatedText))

            waitForIdle()
            Thread.sleep(1000)
        }
    }

    @Test
    fun testListSaveIfNoEmptyParams() {
        // assert loaded data are correct, translate
        testTranslation()

        val testSaveToListButtonTag = "TestSegmentedButton" + 0
        with(composeRule) {
            waitForIdle()

            // scroll, continue button is displayed
            onNodeWithTag(TestContent).performScrollToNode(hasTestTag(TestContinueButton))
            onNodeWithTag(testSaveToListButtonTag).assertExists()

            // save
            onNodeWithTag(testSaveToListButtonTag).performClick()
            onNodeWithTag(TestContinueButton).performClick()
            waitForIdle()
            Thread.sleep(1000)

            // navigation occurs - occurs only when all translation components not empty
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(TranslationListScreenDestination.route, currentRoute)
        }
    }

    @Test
    fun testMapSaveIfNoEmptyParams() {
        // assert data are correct, translate
        testTranslation()

        val testSaveToMapButtonTag = "TestSegmentedButton" + 1
        with(composeRule) {
            waitForIdle()

            // scroll to continue button - is displayed
            onNodeWithTag(TestContent).performScrollToNode(hasTestTag(TestContinueButton))
            onNodeWithTag(testSaveToMapButtonTag).assertExists()

            // save
            onNodeWithTag(testSaveToMapButtonTag).performClick()
            onNodeWithTag(TestContinueButton).performClick()
            waitForIdle()
            Thread.sleep(1000)

            // navigation occurs - occurs only when all translation components not empty
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(PinEditScreenDestination.route, currentRoute)
        }
    }

    @Test
    fun testDontSaveIfEmptyParams() {
        val testSaveToListButtonTag = "TestSegmentedButton" + 0
        val testSaveToMapButtonTag = "TestSegmentedButton" + 1
        val testDiscardButtonTag = "TestSegmentedButton" + 2

        launchTranslateScreenDirectly()
        with(composeRule) {
            waitForIdle()

            // scroll to continue button
            onNodeWithTag(TestContent).performScrollToNode(hasTestTag(TestContinueButton))
            onNodeWithTag(TestContinueButton).assertExists()


            // visible radio buttons?
            onNodeWithTag(testSaveToListButtonTag).assertIsDisplayed()
            onNodeWithTag(testSaveToMapButtonTag).assertIsDisplayed()
            onNodeWithTag(testDiscardButtonTag).assertIsDisplayed()

            // try saving to any
            onNodeWithTag(testSaveToListButtonTag).performClick()
            onNodeWithTag(TestContinueButton).performClick()
            waitForIdle()

            // error messages displayed - only translation is empty
            onNodeWithTag(TestTransStringTextField + TestErrorMsgTag)
                .assert(hasText(errorMsg))

            Espresso.closeSoftKeyboard()
            waitForIdle()
            Thread.sleep(1000)

            // error messages displayed - only translation is empty
            onNodeWithTag(TestTransStringTextField + TestErrorMsgTag)
                .assert(hasText(errorMsg))

            Thread.sleep(1000)

            // try saving
            onNodeWithTag(testSaveToMapButtonTag).performClick()
            onNodeWithTag(TestContinueButton).performClick()
            waitForIdle()
            Thread.sleep(1000)

            // did not save, errors displayed -> try discarding
            onNodeWithTag(testDiscardButtonTag).performClick()
            onNodeWithTag(TestContinueButton).performClick()
            waitForIdle()
            Thread.sleep(1000)

            // translation discarded, navigation navigated to translation list
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(TranslationListScreenDestination.route, currentRoute)
        }
    }
}
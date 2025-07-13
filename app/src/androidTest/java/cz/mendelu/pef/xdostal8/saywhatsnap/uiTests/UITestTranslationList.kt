package cz.mendelu.pef.xdostal8.saywhatsnap.uiTests

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.MainActivity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.SettingsScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_list.TestTagSettingsButton
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.SayWhatSnapTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class UITestTranslationList {

    private lateinit var navController: NavHostController


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    //  1 - HIGH
    private fun launchTransListScreenWithNavigation() {
        composeRule.activity.setContent {
            MaterialTheme {
                SayWhatSnapTheme {
                    navController = rememberNavController()
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController
                    )
                }
            }
        }
    }


    @Test
    fun testLoadTodayTranslationAndNavigateOnEdit() {
        // as per mock - today translation - id 1
        val transTestTag = "TestTagTranslationCard1"

        // Launch the screen
        launchTransListScreenWithNavigation()
        with(composeRule) {
            waitForIdle()
            // Perform the click action on the translation card
            onNodeWithTag(transTestTag).assertIsDisplayed()
            composeRule.onNodeWithTag(transTestTag).performClick()
            waitForIdle()

            val currentRoute =
                navController.currentBackStackEntry?.destination?.route

            assertEquals(TranslationEditScreenDestination.route, currentRoute)
            Thread.sleep(1000)
        }
    }

    @Test
    fun testNavigateToSettings() {
        launchTransListScreenWithNavigation()
        with(composeRule) {
            waitForIdle()
            onNodeWithTag(TestTagSettingsButton).assertIsDisplayed()
            composeRule.onNodeWithTag(TestTagSettingsButton).performClick()
            waitForIdle()

            val currentRoute =
                navController.currentBackStackEntry?.destination?.route

            assertEquals(SettingsScreenDestination.route, currentRoute)
            Thread.sleep(1000)

        }
    }
}


/*
private fun launchTransListScreenWithYesterdayDateAndNavigation() {
    composeRule.activity.setContent {
        MaterialTheme {
            SayWhatSnapTheme {
                // Inject the ViewModel
                val viewModel: TranslationListViewModel = hiltViewModel()
                navController = rememberNavController()
                DestinationsNavHost(navGraph = NavGraphs.root, navController = navController)

                // Set the date in the ViewModel to yesterday
                LaunchedEffect(Unit) {
                    viewModel.uiData.date = 2
                }
            }
        }
    }
}
    @Test
    fun testLoadYesterdayTranslationAndNavigateOnEdit() {
        // as per mock - today translation - id 2
        val transTestTag = "TestTagTranslationCard2"
        launchTransListScreenWithYesterdayDateAndNavigation()
        with(composeRule) {
            waitForIdle()
            Thread.sleep(1000)

            // Perform the click action on the translation card
            onNodeWithTag(transTestTag).assertIsDisplayed()
            composeRule.onNodeWithTag(transTestTag).performClick()
            waitForIdle()

            val currentRoute =
                navController.currentBackStackEntry?.destination?.route

            assertEquals(TranslationEditScreenDestination.route, currentRoute)
            Thread.sleep(1000)   // Just for better visibility

        }
    }
 */
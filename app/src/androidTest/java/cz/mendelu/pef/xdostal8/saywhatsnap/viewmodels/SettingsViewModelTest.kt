package cz.mendelu.pef.xdostal8.saywhatsnap.viewmodels

import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.CommunicationResult
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.ILibreTranslateRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.ServerMock
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen.SettingsScreenUIState
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen.SettingsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private val mockLibreTranslate: ILibreTranslateRemoteRepository = mockk()
    private val mockDatastore: IDataStoreRepository = mockk()
    private val mockLanguageCodes = ServerMock.csEnMockLanguages.map { it.code }


    @Before
    fun setUp() {
        coEvery { mockLibreTranslate.getLanguages() } returns CommunicationResult.Success(ServerMock.csEnMockLanguages)
        coEvery { mockLibreTranslate.translate(any()) } returns CommunicationResult.Success(
            ServerMock.appleMockTranslationResponse
        )
        coEvery { mockLibreTranslate.detectLanguage(any()) } returns CommunicationResult.Success(
            ServerMock.jablkoMockDetectionResponse
        )

        coEvery { mockDatastore.getLanguage() } returns "en"
        coEvery { mockDatastore.getTranslationLanguage() } returns "cs"


        viewModel = SettingsViewModel(
            libreTranslateRemoteRepository = mockLibreTranslate,
            dataStoreRepository = mockDatastore
        )
        viewModel.getLanguages()

        viewModel.loadLanguage()
        viewModel.loadPreferredLanguage()

    }

    @Test
    fun testSettingsViewModel() = runTest {
        testApiLanguageOptions()
        testDatastoreItems()
        testLanguageChanges()
        testErrors()
        testSave()
    }

    private fun testApiLanguageOptions() {
        viewModel.getLanguages()
        assert(viewModel.uiData.errors.communicationError == null)
        assertEquals(viewModel.uiData.languageCodes, mockLanguageCodes)
        assertEquals(viewModel.translateLanguagesUIState.value.data, ServerMock.csEnMockLanguages)
    }

    private fun testDatastoreItems() {
        assertEquals(viewModel.uiData.language, "en")
        assertEquals(viewModel.uiData.translationLanguage, "cs")
    }

    private fun testLanguageChanges() {
        val newAppLang = "en"
        viewModel.onLanguageChange(newAppLang)

        val newTransLang = "cs"
        viewModel.onTranslatedLanguageChange(newTransLang)

        assertEquals(viewModel.uiData.language, newAppLang)
        assertEquals(viewModel.uiData.translationLanguage, newTransLang)
    }

    private fun testErrors() {
        assert(viewModel.uiErrors.communicationError == null)
        assert(viewModel.uiErrors.appLanguageError == null)
        assert(viewModel.uiErrors.translateLanguageError == null)
        assert(viewModel.translateLanguagesUIState.value.errors == null)
    }

    private fun testSave() {
        viewModel.saveChanges()
        assert(viewModel.screenUIState.value == SettingsScreenUIState.SettingsChanged)
    }
}
package cz.mendelu.pef.xdostal8.saywhatsnap.viewmodels

import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.PinEditActions
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.PinEditUIState
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.PinEditViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PinEditViewModelTest {
    private lateinit var viewModel: PinEditViewModel
    private lateinit var actions: PinEditActions
    private val mockRepository: ITranslationsRepository = mockk()

    private lateinit var testTranslation: TranslationEntity

    private lateinit var finalTranslation: TranslationEntity


    @Before
    fun setup() {
        finalTranslation = TranslationEntity(
            originalString = "Final String",
            originalLanguage = "Final Original Language",
            translatedString = "Final Translated String",
            translatedLanguage = "Final Translated Language",
            visible = false,
            image = "final_image_uri",
            date = 20,
            latitude = 50.0,
            longitude = 15.0,
            // only changing values
            category = "Final Category",
            description = "Final Description",
            name = "Final Name",
        )

        testTranslation = TranslationEntity(
            originalString = "Final String",
            originalLanguage = "Final Original Language",
            translatedString = "Final Translated String",
            translatedLanguage = "Final Translated Language",
            image = "final_image_uri",
            visible = false,
            date = 20,
            latitude = 50.0,
            longitude = 15.0,
            // only changing values
            name = "Test Name",
            category = "Test Category",
            description = "Test Description",
        )

        // any() - database is tested separately...
        coEvery { mockRepository.getTranslationById(any()) } returns testTranslation
        coEvery { mockRepository.update(any()) } returns Unit
        coEvery { mockRepository.deleteTranslationById(any()) }

        viewModel = PinEditViewModel(mockRepository)
        actions = viewModel
        viewModel.loadTranslation(0)
    }

    @Test
    fun testLoad() = runTest {
        val loadedTranslation = viewModel.uiData.translation
        assertEquals(loadedTranslation, testTranslation)
    }

    @Test
    fun testEditState() = runTest {

        actions.onNameChange(finalTranslation.name)

        actions.onCategoryChange(finalTranslation.category)

        actions.onDescriptionChange(finalTranslation.description)

        assertEquals(viewModel.uiData.translation, finalTranslation)


        actions.saveTranslation()
        assertNull(viewModel.uiErrors.nameError)
        assertNull(viewModel.uiErrors.descriptionError)
        assertNull(viewModel.uiErrors.categoryError)
        assert(viewModel.screenUIState.value != PinEditUIState.ErrorOccurred)
    }

    @Test
    fun deleteEditState() = runTest {
        viewModel.deleteTranslation()
        assert(viewModel.screenUIState.value != PinEditUIState.ErrorOccurred)
    }

}
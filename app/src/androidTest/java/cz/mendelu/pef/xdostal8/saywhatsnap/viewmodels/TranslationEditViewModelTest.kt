package cz.mendelu.pef.xdostal8.saywhatsnap.viewmodels

import androidx.core.net.toUri
import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TranslationEditActions
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TranslationEditUIState
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TranslationEditViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TranslationEditViewModelTest {
    private lateinit var viewModel: TranslationEditViewModel
    private lateinit var actions: TranslationEditActions
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
            category = "Test Category",
            description = "Test Description",
            visible = false,
            name = "Final Name",
            image = "final_image_uri",
            // not changing
            date = 10,
            latitude = 50.0,
            longitude = 15.0,

            )

        testTranslation = TranslationEntity(
            originalString = "Test String",
            originalLanguage = "Test Original Language",
            translatedString = "Test Translated String",
            translatedLanguage = "Test Translated Language",
            image = "final_image_uri",
            visible = true,
            name = "Test Name",
            category = "Final Category",
            description = "Final Description",
            // not changing
            date = 10,
            latitude = 50.0,
            longitude = 15.0,
        )

        // any() - database is tested separately...
        coEvery { mockRepository.getTranslationById(any()) } returns testTranslation
        coEvery { mockRepository.update(any()) } returns Unit
        coEvery { mockRepository.deleteTranslationById(any()) }

        viewModel = TranslationEditViewModel(mockRepository)
        actions = viewModel
        viewModel.loadTranslation(0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testLoad() = runTest {
        val loadedTranslation = viewModel.uiData.translation
        advanceUntilIdle()
        assertEquals(loadedTranslation, testTranslation)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testEditState() = runTest {

        actions.onImageChanged(finalTranslation.image.toUri())

        actions.onOriginalStringChange(finalTranslation.originalString)

        actions.onTranslatedStringChange(finalTranslation.translatedString)

        actions.onOriginalLanguageChange(finalTranslation.originalLanguage)

        actions.onTranslatedLanguageChange(finalTranslation.translatedLanguage)

        actions.onVisibleOnMapChange(finalTranslation.visible)

        actions.onNameChange(finalTranslation.name)

        actions.onCategoryChange(finalTranslation.category)

        actions.onDescriptionChange(finalTranslation.description)
        advanceUntilIdle()

        assertEquals(viewModel.uiData.translation, finalTranslation)
        assertEquals(viewModel.uiData.selectedImageURI.toString(), finalTranslation.image)

        actions.saveTranslation()
        advanceUntilIdle()

        assertNull(viewModel.uiErrors.translatedStringError)
        assertNull(viewModel.uiErrors.translatedLanguageError)
        assertNull(viewModel.uiErrors.originalLanguageError)
        assertNull(viewModel.uiErrors.originalStringError)
        assert(viewModel.screenUIState.value != TranslationEditUIState.ErrorOccurred)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteEditState() = runTest {
        viewModel.deleteTranslation()
        advanceUntilIdle()

        assert(viewModel.screenUIState.value != TranslationEditUIState.ErrorOccurred)
    }

}
package cz.mendelu.pef.xdostal8.saywhatsnap.viewmodels

import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_list.TranslationListActions
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_list.TranslationListViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import io.mockk.coEvery
import io.mockk.mockk


class TranslationListViewModelTest {
    private lateinit var viewModel: TranslationListViewModel
    private lateinit var actions: TranslationListActions
    private val mockRepository: ITranslationsRepository = mockk()
    private lateinit var testTranslation: TranslationEntity


    @Before
    fun setup() {
        testTranslation = TranslationEntity(
            originalString = "Test String",
            originalLanguage = "Test Original Language",
            translatedString = "Test Translated String",
            translatedLanguage = "Test Translated Language",
            date = Calendar.getInstance().timeInMillis,
            image = "test_image_uri",
            name = "Test Name",
            latitude = 49.0, // Example latitude
            longitude = 14.0, // Example longitude
            category = "Test Category",
            description = "Test Description",
            visible = true
        )
        // any() - database is tested separately...
        coEvery { mockRepository.getTranslationsForDay(any(), any()) } returns flowOf(
            listOf(
                testTranslation
            )
        )

        viewModel = TranslationListViewModel(mockRepository)
        actions = viewModel
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testLoadTodayTranslation() = runTest {
        actions.changeSelectedDate(Calendar.getInstance().timeInMillis)

        advanceUntilIdle()

        val data = viewModel.transUIState.value.data
        val translationID = viewModel.transUIState.value.data?.get(0)?.id
        assertNotNull(data)
        assertEquals(translationID, testTranslation.id)
        assertFalse(viewModel.transUIState.value.loading)
    }
}
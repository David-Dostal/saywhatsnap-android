package cz.mendelu.pef.xdostal8.saywhatsnap.database

import android.arch.core.executor.testing.InstantTaskExecutorRule
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import java.util.Calendar
import javax.inject.Named

@HiltAndroidTest
class TranslationsDatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: TranslationsDatabase
    private lateinit var translationsDao: TranslationsDao

    @Before
    fun setup() {
        hiltRule.inject()
        translationsDao = database.translationsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private val todayVisibleTrans = TranslationEntity(
        originalString = "Hello",
        originalLanguage = "English",
        translatedString = "Hola",
        translatedLanguage = "Spanish",
        date = Calendar.getInstance().timeInMillis,
        image = "image_uri1",
        name = "name1",
        latitude = 0.0,
        longitude = 0.0,
        category = "cat1",
        description = "description1",
        visible = true
    )

    private val yesterdayNotVisibleTrans = TranslationEntity(
        originalString = "ahoj",
        originalLanguage = "cs",
        translatedString = "hi",
        translatedLanguage = "en",
        date = System.currentTimeMillis(),
        image = "image_uri1",
        name = "name1",
        latitude = 0.0,
        longitude = 0.0,
        category = "cat2",
        description = "description2",
        visible = false
    )
    @Test
    fun testInsertingTranslations() = runTest {
        // Arrange
        val todayId = translationsDao.insert(todayVisibleTrans)
        val yesterdayId = translationsDao.insert(yesterdayNotVisibleTrans.copy(date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis))

        // Act
        val todayInsertedTrans = translationsDao.getTranslationById(todayId)
        val yesterdayInsertedTrans = translationsDao.getTranslationById(yesterdayId)

        // Assert
        assertEquals(todayVisibleTrans.originalString, todayInsertedTrans.originalString)
        assertEquals(yesterdayNotVisibleTrans.originalString, yesterdayInsertedTrans.originalString)
    }

    @Test
    fun testGettingTranslations() = runTest {
        // Arrange
        translationsDao.insert(todayVisibleTrans)
        translationsDao.insert(yesterdayNotVisibleTrans.copy(date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis))

        // Act & Assert
        val allTranslations = translationsDao.getAll().first()
        assertEquals(2, allTranslations.size)

        val visibleCategories = translationsDao.getVisibleCategories().first()
        assertEquals(1, visibleCategories.size)

        val visibleTranslations = translationsDao.getVisibleTranslations().first()
        assertEquals(1, visibleTranslations.size)

        val allCategories = translationsDao.getAllCategories().first()
        assertEquals(listOf("cat1", "cat2"), allCategories)
    }

    @Test
    fun testGetTranslationsForDay() = runTest {
        // Arrange - insert translations
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
        val todayId = translationsDao.insert(todayVisibleTrans)
        val yesterdayId = translationsDao.insert(yesterdayNotVisibleTrans.copy(date = yesterday))

        // Act - retrieve translations for yesterday
        val translationsForYesterday = translationsDao.getTranslationsForDay(yesterday, yesterday).first()

        // Assert - check if the correct translations are retrieved
        assertTrue(translationsForYesterday.any { it.id == yesterdayId })
        assertFalse(translationsForYesterday.any { it.id == todayId })
    }

    @Test
    fun testUpdatingTranslation() = runTest {
        // Arrange
        val insertId = translationsDao.insert(todayVisibleTrans)
        val getTrans = translationsDao.getTranslationById(insertId)
        getTrans.name = "Updated String"
        // Act
        translationsDao.update(getTrans)
        val updated = translationsDao.getTranslationById(getTrans.id!!)

        // Assert
        assertEquals("Updated String", updated.name)
    }


    @Test
    fun testDeletingTranslation() = runTest {
        // Arrange
        val insertId = translationsDao.insert(todayVisibleTrans)

        // Act
        translationsDao.deleteTranslationById(insertId)
        val retrievedAfterDelete = translationsDao.getTranslationById(insertId)

        // Assert
        assertNull(retrievedAfterDelete)
    }

}
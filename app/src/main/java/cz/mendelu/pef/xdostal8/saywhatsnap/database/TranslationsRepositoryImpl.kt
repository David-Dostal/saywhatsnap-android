package cz.mendelu.pef.xdostal8.saywhatsnap.database

import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import kotlinx.coroutines.flow.Flow

class TranslationsRepositoryImpl(private val translationsDao: TranslationsDao) :
    ITranslationsRepository {

    override fun getAll(): Flow<List<TranslationEntity>> {
        return translationsDao.getAll()
    }

    override fun getVisibleCategories(): Flow<List<String>> {
        return translationsDao.getVisibleCategories()
    }

    override fun getVisibleTranslations(): Flow<List<TranslationEntity>> {
        return translationsDao.getVisibleTranslations()
    }

    override fun getVisibleTranslationsByCategory(category: String): Flow<List<TranslationEntity>> {
        return translationsDao.getVisibleTranslationsByCategory(category)
    }

    override fun getTranslationByDate(date: Long): Flow<List<TranslationEntity>> {
        return translationsDao.getTranslationByDate(date)
    }

    override fun getTranslationsForDay(
        dayStart: Long,
        dayEnd: Long
    ): Flow<List<TranslationEntity>> {
        return translationsDao.getTranslationsForDay(dayStart, dayEnd)
    }

    override fun getTranslationsWithinTimeframe(
        startTimestamp: Long, endTimestamp: Long
    ): Flow<List<TranslationEntity>> {
        return translationsDao.getTranslationsWithinTimeframe(startTimestamp, endTimestamp)
    }

    override fun getAllCategories(): Flow<List<String>> {
        return translationsDao.getAllCategories()
    }


    override fun getTranslationByDay(day: Long): Flow<List<TranslationEntity>> {
        return translationsDao.getTranslationByDay(day)
    }

    override suspend fun insert(translation: TranslationEntity): Long {
        return translationsDao.insert(translation)
    }

    override suspend fun deleteTranslationById(id: Long): Int {
        return translationsDao.deleteTranslationById(id)
    }

    override suspend fun getTranslationById(id: Long): TranslationEntity =
        translationsDao.getTranslationById(id)

    override suspend fun update(translation: TranslationEntity) {
        translationsDao.update(translation)
    }


}
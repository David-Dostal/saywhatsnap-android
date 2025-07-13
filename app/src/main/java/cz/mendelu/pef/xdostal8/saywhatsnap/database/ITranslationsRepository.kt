package cz.mendelu.pef.xdostal8.saywhatsnap.database

import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import kotlinx.coroutines.flow.Flow

interface ITranslationsRepository {
    fun getAll(): Flow<List<TranslationEntity>>
    suspend fun insert(translation: TranslationEntity): Long

    suspend fun deleteTranslationById(id: Long): Int
    suspend fun getTranslationById(id: Long): TranslationEntity

    fun getTranslationByDate(date: Long): Flow<List<TranslationEntity>>

    fun getTranslationsForDay(dayStart: Long, dayEnd: Long):
            Flow<List<TranslationEntity>>

    fun getTranslationsWithinTimeframe(
        startTimestamp: Long, endTimestamp: Long
    ): Flow<List<TranslationEntity>>

    fun getAllCategories(): Flow<List<String>>

    fun getTranslationByDay(day: Long): Flow<List<TranslationEntity>>
    suspend fun update(translation: TranslationEntity)

    fun getVisibleCategories(): Flow<List<String>>

    fun getVisibleTranslations(): Flow<List<TranslationEntity>>

    fun getVisibleTranslationsByCategory(category: String): Flow<List<TranslationEntity>>
}
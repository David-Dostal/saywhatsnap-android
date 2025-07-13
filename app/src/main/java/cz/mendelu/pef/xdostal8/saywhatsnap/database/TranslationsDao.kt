package cz.mendelu.pef.xdostal8.saywhatsnap.database

import androidx.room.*
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationsDao {

    @Query("SELECT category FROM translations WHERE visible = 1")
    fun getVisibleCategories(): Flow<List<String>>

    @Query("SELECT * FROM translations WHERE visible = 1")
    fun getVisibleTranslations(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE category = :category AND visible = 1")
    fun getVisibleTranslationsByCategory(category: String): Flow<List<TranslationEntity>>


    @Query("SELECT * FROM translations WHERE date >= :endTimestamp AND date <= :startTimestamp")
    fun getTranslationsWithinTimeframe(
        startTimestamp: Long, endTimestamp: Long
    ): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations")
    fun getAll(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE date = :date")
    fun getTranslationByDate(date: Long): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE date >= :dayStart AND date <= :dayEnd")
    fun getTranslationsForDay(dayStart: Long, dayEnd: Long): Flow<List<TranslationEntity>>


    @Query("SELECT category FROM translations")
    fun getAllCategories(): Flow<List<String>>


    @Query("SELECT * FROM translations WHERE strftime('%Y-%m-%d', date / 1000, 'unixepoch') = strftime('%Y-%m-%d', :day / 1000, 'unixepoch')")
    fun getTranslationByDay(day: Long): Flow<List<TranslationEntity>>

    @Query("DELETE FROM translations WHERE id = :id")
    suspend fun deleteTranslationById(id: Long): Int


    @Insert
    suspend fun insert(translation: TranslationEntity): Long

    @Update
    suspend fun update(translation: TranslationEntity)


    @Query("SELECT * FROM translations WHERE id = :id")
    suspend fun getTranslationById(id: Long): TranslationEntity


}
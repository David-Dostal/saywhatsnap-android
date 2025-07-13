package cz.mendelu.pef.xdostal8.saywhatsnap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity

@Database(entities = [TranslationEntity::class], version = 9, exportSchema = false)
abstract class TranslationsDatabase : RoomDatabase() {

    abstract fun translationsDao(): TranslationsDao

    companion object {
        private var INSTANCE: TranslationsDatabase? = null
        fun getDatabase(context: Context): TranslationsDatabase {
            if (INSTANCE == null) {
                synchronized(TranslationsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TranslationsDatabase::class.java,
                            "translations_database"
                        ).fallbackToDestructiveMigration()

                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
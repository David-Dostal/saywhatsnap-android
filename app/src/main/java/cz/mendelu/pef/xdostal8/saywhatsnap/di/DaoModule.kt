package cz.mendelu.pef.xdostal8.saywhatsnap.di

import cz.mendelu.pef.xdostal8.saywhatsnap.database.TranslationsDao
import cz.mendelu.pef.xdostal8.saywhatsnap.database.TranslationsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(database: TranslationsDatabase): TranslationsDao {
        return database.translationsDao()
    }

}
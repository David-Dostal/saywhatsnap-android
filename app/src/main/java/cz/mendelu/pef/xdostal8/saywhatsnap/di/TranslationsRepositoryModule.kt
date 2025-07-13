package cz.mendelu.pef.xdostal8.saywhatsnap.di

import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.database.TranslationsDao
import cz.mendelu.pef.xdostal8.saywhatsnap.database.TranslationsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslationsRepositoryModule {
    @Provides
    @Singleton
    fun provideTranslationsRepository(dao: TranslationsDao): ITranslationsRepository {
        return TranslationsRepositoryImpl(dao)
    }
}
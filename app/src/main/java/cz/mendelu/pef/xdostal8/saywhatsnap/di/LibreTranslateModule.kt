package cz.mendelu.pef.xdostal8.saywhatsnap.di

import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.ILibreTranslateRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.LibreTranslateAPI
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.LibreTranslateRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LibreTranslateModule {

    @Provides
    @Singleton
    fun provideLibreTranslateRepository(api: LibreTranslateAPI): ILibreTranslateRemoteRepository =
        LibreTranslateRemoteRepositoryImpl(api)
}

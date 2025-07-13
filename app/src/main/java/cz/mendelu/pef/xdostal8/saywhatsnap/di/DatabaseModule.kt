package cz.mendelu.pef.xdostal8.saywhatsnap.di

import cz.mendelu.pef.xdostal8.saywhatsnap.SayWhatSnapApplication
import cz.mendelu.pef.xdostal8.saywhatsnap.database.TranslationsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(): TranslationsDatabase =
        TranslationsDatabase.getDatabase(SayWhatSnapApplication.appContext)

}

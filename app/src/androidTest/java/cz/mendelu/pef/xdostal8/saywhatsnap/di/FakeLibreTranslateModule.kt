package cz.mendelu.pef.xdostal8.saywhatsnap.di

import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.ILibreTranslateRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.LibreTranslateAPI
import cz.mendelu.pef.xdostal8.saywhatsnap.fake.FakeLibreTranslateRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LibreTranslateModule::class],
)
abstract class FakeLibreTranslateModule {
    @Binds
    abstract fun provideLibreTranslateRepository(api: FakeLibreTranslateRemoteRepositoryImpl): ILibreTranslateRemoteRepository
}

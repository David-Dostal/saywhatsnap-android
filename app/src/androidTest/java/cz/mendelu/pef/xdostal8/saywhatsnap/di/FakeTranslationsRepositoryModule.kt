package cz.mendelu.pef.xdostal8.saywhatsnap.di

import cz.mendelu.pef.xdostal8.saywhatsnap.database.ITranslationsRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.database.TranslationsDao
import cz.mendelu.pef.xdostal8.saywhatsnap.fake.FakeTranslationsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TranslationsRepositoryModule::class],
)
abstract class FakeTranslationsRepositoryModule{
    @Binds
    abstract fun provideTranslationsRepository(impl: FakeTranslationsRepositoryImpl): ITranslationsRepository
}

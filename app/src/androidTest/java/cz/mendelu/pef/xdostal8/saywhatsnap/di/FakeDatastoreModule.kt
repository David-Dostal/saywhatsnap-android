package cz.mendelu.pef.xdostal8.saywhatsnap.di

import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.ILibreTranslateRemoteRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.translations.LibreTranslateAPI
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import cz.mendelu.pef.xdostal8.saywhatsnap.fake.FakeDatastoreRepositoryImpl
import cz.mendelu.pef.xdostal8.saywhatsnap.fake.FakeLibreTranslateRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
abstract class FakeDatastoreModule {
    @Binds
    abstract fun provideDataStoreRepository(api: FakeDatastoreRepositoryImpl): IDataStoreRepository
}

package cz.mendelu.pef.xdostal8.saywhatsnap.di

import android.content.Context
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.DataStoreRepositoryImpl
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context): IDataStoreRepository =
        DataStoreRepositoryImpl(context)
}
    package cz.mendelu.pef.xdostal8.saywhatsnap.di

    import com.squareup.moshi.Moshi
    import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
    import dagger.Module
    import dagger.Provides
    import dagger.hilt.InstallIn
    import dagger.hilt.components.SingletonComponent
    import retrofit2.Retrofit
    import retrofit2.converter.moshi.MoshiConverterFactory
    import javax.inject.Singleton

    @Module
    @InstallIn(SingletonComponent::class)
    object RetrofitModule {

        @Provides
        @Singleton
        fun provideMoshi(): Moshi =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        @Provides
        @Singleton
        fun provideRetrofit(moshi: Moshi): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(MoshiConverterFactory.create(moshi)) // JSON converter
                .build()
        }



    }

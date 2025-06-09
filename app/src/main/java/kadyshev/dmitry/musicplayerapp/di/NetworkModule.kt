package kadyshev.dmitry.musicplayerapp.di

import dagger.Module
import dagger.Provides
import kadyshev.dmitry.core_di.ApplicationScope
import kadyshev.dmitry.data.Mapper
import kadyshev.dmitry.data.network.DeezerApi
import kadyshev.dmitry.data.network.repositories.TrackApiRepositoryImpl
import kadyshev.dmitry.domain.repository.TrackApiRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @ApplicationScope
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.deezer.com/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @ApplicationScope
    @Provides
    fun provideDeezerApi(retrofit: Retrofit): DeezerApi =
        retrofit.create(DeezerApi::class.java)

    @ApplicationScope
    @Provides
    fun provideMapper(): Mapper = Mapper()

    @ApplicationScope
    @Provides
    fun provideTrackApiRepository(
        deezerApi: DeezerApi,
        mapper: Mapper
    ): TrackApiRepository = TrackApiRepositoryImpl(deezerApi, mapper)
}

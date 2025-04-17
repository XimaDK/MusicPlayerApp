package kadyshev.dmitry.musicplayerapp.di

import kadyshev.dmitry.data.network.DeezerApi
import kadyshev.dmitry.data.Mapper
import kadyshev.dmitry.data.network.repositories.TrackApiRepositoryImpl
import kadyshev.dmitry.domain.repository.TrackApiRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<DeezerApi> { get<Retrofit>().create(DeezerApi::class.java) }

    single<TrackApiRepository> { TrackApiRepositoryImpl(get(), get()) }

    single<Mapper> {
        Mapper()
    }
}
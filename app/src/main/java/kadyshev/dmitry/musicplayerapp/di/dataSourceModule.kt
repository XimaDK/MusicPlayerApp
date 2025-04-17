package kadyshev.dmitry.musicplayerapp.di

import kadyshev.dmitry.data.dataSource.AppDataBase
import kadyshev.dmitry.data.dataSource.repositories.TrackDataSourceRepositoryImpl
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import org.koin.dsl.module

val dataSourceModule = module {

    single<TrackDataSourceRepository> {
        TrackDataSourceRepositoryImpl(get(), get(), get())
    }

    single { AppDataBase.getInstance(get()) }


    single { get<AppDataBase>().trackDao() }

}
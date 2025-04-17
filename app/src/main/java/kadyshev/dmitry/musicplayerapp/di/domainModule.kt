package kadyshev.dmitry.musicplayerapp.di

import kadyshev.dmitry.domain.usecases.DownloadTrackUseCase
import kadyshev.dmitry.domain.usecases.GetAllTracksUseCase
import kadyshev.dmitry.domain.usecases.GetChartFromApiUseCase
import kadyshev.dmitry.domain.usecases.SearchTracksFromApiUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        SearchTracksFromApiUseCase(get())
    }

    factory {
        GetChartFromApiUseCase(get())
    }

    factory {
        GetAllTracksUseCase(get())
    }

    factory {
        DownloadTrackUseCase(get())
    }
}
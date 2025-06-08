package kadyshev.dmitry.musicplayerapp.di

import dagger.Module
import dagger.Provides
import kadyshev.dmitry.domain.repository.TrackApiRepository
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import kadyshev.dmitry.domain.usecases.*


//можно ли использовать в domain слое @Inject constructor или это загрязнение?
@Module
class  DomainModule {

    @Provides
    fun provideSearchTracksFromApiUseCase(
        repository: TrackApiRepository
    ): SearchTracksFromApiUseCase = SearchTracksFromApiUseCase(repository)

    @Provides
    fun provideGetChartFromApiUseCase(
        repository: TrackApiRepository
    ): GetChartFromApiUseCase = GetChartFromApiUseCase(repository)

    @Provides
    fun provideDeleteTrackUseCase(
        repository: TrackDataSourceRepository
    ): DeleteTrackUseCase = DeleteTrackUseCase(repository)

    @Provides
    fun provideGetAllTracksUseCase(
        repository: TrackDataSourceRepository
    ): GetAllTracksUseCase = GetAllTracksUseCase(repository)

    @Provides
    fun provideDownloadTrackUseCase(
        repository: TrackDataSourceRepository
    ): DownloadTrackUseCase = DownloadTrackUseCase(repository)
}

package kadyshev.dmitry.musicplayerapp.di

import dagger.Binds
import dagger.Module
import kadyshev.dmitry.core_navigtaion.PlayerNavigation
import kadyshev.dmitry.data.dataSource.repositories.TrackDataSourceRepositoryImpl
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import kadyshev.dmitry.musicplayerapp.PlayerNavigationImpl

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindTrackDataSourceRepository(impl: TrackDataSourceRepositoryImpl): TrackDataSourceRepository

    @Binds
    @ApplicationScope
    fun bindPlayerNavigation(impl: PlayerNavigationImpl): PlayerNavigation
}
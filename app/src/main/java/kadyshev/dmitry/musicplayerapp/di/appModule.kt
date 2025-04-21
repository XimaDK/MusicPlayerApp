package kadyshev.dmitry.musicplayerapp.di

import android.content.Context
import kadyshev.dmitry.core_navigtaion.PlayerNavigation
import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.domain.repository.PlayerServiceInteractor
import kadyshev.dmitry.musicplayerapp.PlayerNavigationImpl
import kadyshev.dmitry.player_service.PlayerServiceInteractorImpl
import kadyshev.dmitry.ui_player.PlayerServiceConnector
import kadyshev.dmitry.ui_player.PlayerViewModel
import kadyshev.dmitry.ui_saved_tracks.SavedTracksViewModel
import kadyshev.dmitry.ui_search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get(), get())
    }

    viewModel<SavedTracksViewModel> {
        SavedTracksViewModel(get())
    }

    viewModel<PlayerViewModel>{
        PlayerViewModel(get())
    }

    single<PlayerNavigation> { PlayerNavigationImpl() }

    single { MusicPlayerManager() }

    single<PlayerServiceInteractor> { PlayerServiceInteractorImpl(get()) }

    single { PlayerServiceConnector(androidContext()) }


}
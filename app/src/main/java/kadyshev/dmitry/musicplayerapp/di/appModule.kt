package kadyshev.dmitry.musicplayerapp.di

import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.ui_saved_tracks.SavedTracksViewModel
import kadyshev.dmitry.ui_search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get(), get())
    }

    viewModel<SavedTracksViewModel> {
        SavedTracksViewModel(get())
    }

    single { MusicPlayerManager() }

}
package kadyshev.dmitry.musicplayerapp.di

import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.ui_search.SearchViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get())
    }

    single { MusicPlayerManager() }

}
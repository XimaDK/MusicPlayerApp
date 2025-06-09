package kadyshev.dmitry.musicplayerapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kadyshev.dmitry.core_di.BaseViewModelFactory
import kadyshev.dmitry.core_di.ViewModelKey
import kadyshev.dmitry.ui_player.PlayerViewModel
import kadyshev.dmitry.ui_saved_tracks.SavedTracksViewModel
import kadyshev.dmitry.ui_search.SearchViewModel

@Module
interface VMModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(vm: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedTracksViewModel::class)
    fun bindSavedTracksViewModel(vm: SavedTracksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindPlayerViewModel(vm: PlayerViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}

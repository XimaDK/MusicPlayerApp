package kadyshev.dmitry.core_di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule {

//    @IntoMap
//    @ViewModelKey(SearchViewModel::class)
//    @Binds
//    fun bindSearchViewModel(impl: SearchViewModel): ViewModel
//
//    @IntoMap
//    @ViewModelKey(SavedTracksViewModel::class)
//    @Binds
//    fun bindSavedTracksViewModel(impl: SavedTracksViewModel): ViewModel
//
//
//    @IntoMap
//    @ViewModelKey(PlayerViewModel::class)
//    @Binds
//    fun bindPlayerViewModel(impl: PlayerViewModel): ViewModel
//
//    @ApplicationScope
//    @Binds
//    fun bindPlayerNavigation(impl: PlayerNavigationImpl): PlayerNavigation
//
//    @ApplicationScope
//    @Binds
//    fun bindPlayerServiceInteractor(impl: PlayerServiceInteractorImpl): PlayerServiceInteractor

//    @Binds
//    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
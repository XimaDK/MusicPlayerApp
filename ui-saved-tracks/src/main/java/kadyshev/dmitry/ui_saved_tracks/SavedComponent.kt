package kadyshev.dmitry.ui_saved_tracks

import dagger.Component
import kadyshev.dmitry.core_di.SavedDependencies

@Component(
    dependencies = [SavedDependencies::class],
//    modules = [SavedTracksBindsModule::class]
)
interface SavedComponent {
    fun inject(fragment: SavedTracksFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: SavedDependencies): SavedComponent
    }
}

//@Module
//interface SavedTracksBindsModule {
//    @IntoMap
//    @ViewModelKey(SavedTracksViewModel::class)
//    @Binds
//    fun bindSavedTracksViewModel(vm: SavedTracksViewModel): ViewModel
//
//}


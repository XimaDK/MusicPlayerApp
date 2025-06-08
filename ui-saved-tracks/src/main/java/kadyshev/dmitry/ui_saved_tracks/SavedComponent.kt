package kadyshev.dmitry.ui_saved_tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import kadyshev.dmitry.core_di.ViewModelKey
import javax.inject.Inject
import javax.inject.Provider

@Subcomponent(modules = [SavedTracksBindsModule::class])
interface SavedComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SavedComponent
    }

    fun inject(fragment: SavedTracksFragment)
}

@Module
interface SavedTracksBindsModule {
    @IntoMap
    @ViewModelKey(SavedTracksViewModel::class)
    @Binds
    fun bindSavedTracksViewModel(impl: SavedTracksViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: SavedTracksViewModelFactory): ViewModelProvider.Factory
}

class SavedTracksViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
        return creator.get() as T
    }
}

package kadyshev.dmitry.ui_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import kadyshev.dmitry.core_di.BaseViewModelFactory
import kadyshev.dmitry.core_di.ViewModelKey

@Subcomponent(modules = [PlayerBindsModule::class])
interface PlayerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PlayerComponent
    }

    fun inject(fragment: PlayerFragment)
}

@Module
interface PlayerBindsModule {
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    @Binds
    fun bindPlayerViewModel(impl: PlayerViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}

interface PlayerComponentProvider {
    fun providePlayerComponentFactory(): PlayerComponent.Factory
}

//class PlayerViewModelFactory @Inject constructor(
//    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        Log.d("PlayerVMFactory", "Creating ViewModel for $modelClass")
//        val creator = creators[modelClass]
//            ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
//            ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
//        return creator.get() as T
//    }
//}

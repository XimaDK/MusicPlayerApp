package kadyshev.dmitry.ui_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import kadyshev.dmitry.core_di.BaseViewModelFactory
import kadyshev.dmitry.core_di.ViewModelKey

@Subcomponent(modules = [SearchBindsModule::class])
interface SearchComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchComponent
    }

    fun inject(fragment: SearchFragment)
}

@Module
interface SearchBindsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}

interface SearchComponentProvider {
    fun provideSearchComponentFactory(): SearchComponent.Factory
}

//class SearchViewModelFactory @Inject constructor(
//    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val creator = creators[modelClass]
//            ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
//            ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
//        return creator.get() as T
//    }
//}

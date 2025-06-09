package kadyshev.dmitry.ui_search

import dagger.Component
import kadyshev.dmitry.core_di.SearchDependencies

@Component(
    dependencies = [SearchDependencies::class],
//    modules = [SearchBindsModule::class]
)
interface SearchComponent {
    fun inject(fragment: SearchFragment)


    @Component.Factory
    interface Factory {
        fun create(deps: SearchDependencies): SearchComponent
    }
}

//@Module
//interface SearchBindsModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(SearchViewModel::class)
//    fun bindSearchViewModel(vm: SearchViewModel): ViewModel
//}

package kadyshev.dmitry.ui_player

import dagger.Component
import kadyshev.dmitry.core_di.PlayerDependencies

@Component(
    dependencies = [PlayerDependencies::class],
//    modules = [PlayerBindsModule::class]
)
interface PlayerComponent {
    fun inject(fragment: PlayerFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: PlayerDependencies): PlayerComponent
    }
}
//
//@Module
//interface PlayerBindsModule {
//    @IntoMap
//    @ViewModelKey(PlayerViewModel::class)
//    @Binds
//    fun bindPlayerViewModel(impl: PlayerViewModel): ViewModel
//
//}


package kadyshev.dmitry.musicplayerapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import kadyshev.dmitry.core_di.ApplicationScope
import kadyshev.dmitry.ui_player.PlayerComponent
import kadyshev.dmitry.ui_saved_tracks.SavedComponent
import kadyshev.dmitry.ui_search.SearchComponent

@ApplicationScope
@Component(
    modules = [DataModule::class, NetworkModule::class, DomainModule::class, DatabaseModule::class, AppModule::class]
)
interface ApplicationComponent {

    fun searchComponentFactory(): SearchComponent.Factory

    fun savedTracksComponentFactory(): SavedComponent.Factory

    fun playerComponentFactory(): PlayerComponent.Factory

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

}

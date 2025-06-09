package kadyshev.dmitry.musicplayerapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import kadyshev.dmitry.core_di.ApplicationScope
import kadyshev.dmitry.core_di.PlayerDependencies
import kadyshev.dmitry.core_di.SavedDependencies
import kadyshev.dmitry.core_di.SearchDependencies

@ApplicationScope
@Component(
    modules = [DataModule::class, NetworkModule::class, DatabaseModule::class, AppModule::class, VMModule::class]
)
interface ApplicationComponent : SearchDependencies, SavedDependencies, PlayerDependencies {


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}

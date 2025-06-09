package kadyshev.dmitry.musicplayerapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import kadyshev.dmitry.core_di.PlayerDependencies
import kadyshev.dmitry.core_di.SavedDependencies
import kadyshev.dmitry.core_di.SearchDependencies
import kadyshev.dmitry.core_navigtaion.PlayerNavigation
import kadyshev.dmitry.domain.repository.TrackApiRepository
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import kadyshev.dmitry.musicplayerapp.di.DaggerApplicationComponent

class App : Application(), SearchDependencies, SavedDependencies, PlayerDependencies {

    private val appComponent = DaggerApplicationComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    override fun context(): Context = this
    override fun playerNavigation(): PlayerNavigation = appComponent.playerNavigation()
    override fun trackApiRepository(): TrackApiRepository = appComponent.trackApiRepository()
    override fun trackDataSourceRepository(): TrackDataSourceRepository =
        appComponent.trackDataSourceRepository()

    override fun viewModelFactory(): ViewModelProvider.Factory = appComponent.viewModelFactory()

}

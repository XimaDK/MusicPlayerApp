package kadyshev.dmitry.musicplayerapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import kadyshev.dmitry.musicplayerapp.di.ApplicationComponent
import kadyshev.dmitry.musicplayerapp.di.DaggerApplicationComponent
import kadyshev.dmitry.ui_player.PlayerComponent
import kadyshev.dmitry.ui_player.PlayerComponentProvider
import kadyshev.dmitry.ui_saved_tracks.SavedComponent
import kadyshev.dmitry.ui_saved_tracks.SavedComponentProvider
import kadyshev.dmitry.ui_search.SearchComponent
import kadyshev.dmitry.ui_search.SearchComponentProvider

class App : Application(),
    SearchComponentProvider,
    SavedComponentProvider,
    PlayerComponentProvider {

    val appComponent: ApplicationComponent = DaggerApplicationComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    override fun provideSearchComponentFactory(): SearchComponent.Factory =
        appComponent.searchComponentFactory()

    override fun provideSavedComponentFactory(): SavedComponent.Factory =
        appComponent.savedTracksComponentFactory()

    override fun providePlayerComponentFactory(): PlayerComponent.Factory =
        appComponent.playerComponentFactory()
}

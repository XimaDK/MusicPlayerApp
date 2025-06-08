package kadyshev.dmitry.musicplayerapp

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import kadyshev.dmitry.core_di.AppComponentProvider
import kadyshev.dmitry.musicplayerapp.di.ApplicationComponent
import kadyshev.dmitry.musicplayerapp.di.DaggerApplicationComponent
import kadyshev.dmitry.ui_player.PlayerFragment
import kadyshev.dmitry.ui_saved_tracks.SavedTracksFragment
import kadyshev.dmitry.ui_search.SearchFragment

class App : Application(), AppComponentProvider {

    val appComponent: ApplicationComponent = DaggerApplicationComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


    }

    override fun <T> inject(fragment: T) where T : Fragment {
        Log.d("AppComponent", "Inject called for fragment: ${fragment::class.java.simpleName}")

        when (fragment) {
            is SearchFragment -> appComponent.searchComponentFactory().create()
                .inject(fragment)

            is SavedTracksFragment -> appComponent.savedTracksComponentFactory().create()
                .inject(fragment)

            is PlayerFragment -> appComponent.playerComponentFactory().create().inject(fragment)


            else -> throw IllegalArgumentException("Unknown fragment type: ${fragment::class.java}")
        }
    }
}
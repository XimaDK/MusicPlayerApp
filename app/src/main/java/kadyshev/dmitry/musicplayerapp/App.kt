package kadyshev.dmitry.musicplayerapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import kadyshev.dmitry.musicplayerapp.di.appModule
import kadyshev.dmitry.musicplayerapp.di.dataSourceModule
import kadyshev.dmitry.musicplayerapp.di.domainModule
import kadyshev.dmitry.musicplayerapp.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        startKoin {
            androidContext(this@App)
            modules(domainModule, networkModule, dataSourceModule, appModule)
        }
    }
}
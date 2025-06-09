package kadyshev.dmitry.musicplayerapp.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import kadyshev.dmitry.core_di.ApplicationScope
import kadyshev.dmitry.core_di.BaseViewModelFactory
import kadyshev.dmitry.core_player.MusicPlayerManager

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @ApplicationScope
    @Provides
    fun provideMusicPlayerManager(): MusicPlayerManager {
        return MusicPlayerManager()
    }

}
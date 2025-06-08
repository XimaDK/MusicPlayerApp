package kadyshev.dmitry.musicplayerapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import kadyshev.dmitry.core_player.MusicPlayerManager

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun provideContext(application: Application): Context = application


    @ApplicationScope
    @Provides
    fun provideMusicPlayerManager(): MusicPlayerManager {
        return MusicPlayerManager()
    }

}
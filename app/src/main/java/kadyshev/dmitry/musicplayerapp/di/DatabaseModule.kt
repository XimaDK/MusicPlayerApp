package kadyshev.dmitry.musicplayerapp.di

import android.app.Application
import dagger.Module
import dagger.Provides
import kadyshev.dmitry.core_di.ApplicationScope
import kadyshev.dmitry.data.dataSource.AppDataBase
import kadyshev.dmitry.data.dataSource.TrackDao

@Module
class DatabaseModule {

    @ApplicationScope
    @Provides
    fun provideAppDatabase(application: Application): AppDataBase {
        return AppDataBase.getInstance(application)
    }

    @ApplicationScope
    @Provides
    fun provideTrackDao(appDataBase: AppDataBase): TrackDao {
        return appDataBase.trackDao()
    }
}

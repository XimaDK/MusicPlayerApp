package kadyshev.dmitry.core_di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import kadyshev.dmitry.core_navigtaion.PlayerNavigation
import kadyshev.dmitry.domain.repository.TrackApiRepository
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository

interface SearchDependencies {
    fun context(): Context
    fun playerNavigation(): PlayerNavigation
    fun trackApiRepository(): TrackApiRepository
    fun trackDataSourceRepository(): TrackDataSourceRepository
    fun viewModelFactory(): ViewModelProvider.Factory

}
package kadyshev.dmitry.core_di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import kadyshev.dmitry.core_navigtaion.PlayerNavigation

interface PlayerDependencies {
    fun context(): Context
    fun playerNavigation(): PlayerNavigation
    fun viewModelFactory(): ViewModelProvider.Factory
}
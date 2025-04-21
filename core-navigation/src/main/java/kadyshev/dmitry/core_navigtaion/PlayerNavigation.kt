package kadyshev.dmitry.core_navigtaion

import androidx.fragment.app.Fragment

interface PlayerNavigation {
    fun openPlayer(fragment: Fragment, playerDataJson: String)
    fun popBackFromPlayer(fragment: Fragment)
}
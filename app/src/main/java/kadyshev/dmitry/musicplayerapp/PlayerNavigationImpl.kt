package kadyshev.dmitry.musicplayerapp

import androidx.core.bundle.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kadyshev.dmitry.core_navigtaion.PlayerNavigation

class PlayerNavigationImpl : PlayerNavigation {
    override fun openPlayer(fragment: Fragment, playerDataJson: String) {
        val bundle = bundleOf("playerData" to playerDataJson)
        fragment.findNavController().navigate(
            R.id.playerFragment,
            bundle,
            NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.nav_graph, false)
                .build()
        )
    }
}

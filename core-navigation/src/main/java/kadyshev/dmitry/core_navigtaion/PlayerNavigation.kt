package kadyshev.dmitry.core_navigtaion

import android.os.Bundle
import androidx.fragment.app.Fragment
import kadyshev.dmitry.domain.entities.PlayerData

interface PlayerNavigation {
    fun openPlayer(fragment: Fragment, playerDataJson: String)
}
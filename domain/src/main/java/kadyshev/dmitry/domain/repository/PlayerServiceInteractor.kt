package kadyshev.dmitry.domain.repository

import kadyshev.dmitry.domain.entities.PlayerData

interface PlayerServiceInteractor {
    fun startService(data: PlayerData, index: Int)
    fun sendAction(action: String)
}
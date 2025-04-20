package kadyshev.dmitry.player_service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.repository.PlayerServiceInteractor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PlayerServiceInteractorImpl(private val context: Context) : PlayerServiceInteractor {

    override fun startService(data: PlayerData, index: Int) {
        val intent = Intent(context, PlayerService::class.java).apply {
            putExtra("playerData", Json.encodeToString(data))
            putExtra("startIndex", index)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    override fun sendAction(action: String) {
        val intent = Intent(context, PlayerService::class.java).apply {
            this.action = action
        }
        context.sendBroadcast(intent)
    }
}

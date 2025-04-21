package kadyshev.dmitry.ui_player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.player_service.PlayerListener
import kadyshev.dmitry.player_service.PlayerService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class PlayerServiceConnector(
    private val context: Context
) {

    private var playerListener: PlayerListener? = null
    private var serviceBound = false
    private var service: PlayerService? = null
    private var pendingStartData: Pair<PlayerData, Int>? = null


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as? PlayerService.PlayerBinder
            service = localBinder?.getService()
            service?.updateListener(playerListener)
            service?.onPlayerReady = { duration ->
                onPlayerReady(duration)
            }
            serviceBound = true

            pendingStartData?.let { (playerData, index) ->
                service?.start(playerData, index)
                pendingStartData = null
            }

            service?.notifyCurrentState()
        }


        fun onPlayerReady(duration: Int) {
            playerListener?.onTrackDurationReceived(duration)
            playerListener?.onPlayStateChanged(true)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service?.updateListener(null)
            service = null
            serviceBound = false
        }
    }

    fun setListener(listener: PlayerListener) {
        this.playerListener = listener
    }

    fun startPlayer(playerData: PlayerData, startIndex: Int) {

        val playerDataJson = Json.encodeToString(playerData)
        val intent = Intent(context, PlayerService::class.java).apply {
            putExtra("playerData", playerDataJson)
            putExtra("startIndex", startIndex)
        }
        ContextCompat.startForegroundService(context, intent)

        // Сохраняем данные, которые отдадим при bind
        pendingStartData = playerData to startIndex

        // Биндимся
        context.bindService(
            Intent(context, PlayerService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun unbind() {
        if (serviceBound) {
            service?.updateListener(null)
            context.unbindService(connection)
            service = null
            serviceBound = false
        }
    }

    fun getCurrentTrackDuration(): Int {
        return service?.getCurrentDuration() ?: 0
    }

    fun togglePlayPause() = service?.togglePlayPause()
    fun nextTrack() = service?.moveToNext()
    fun previousTrack() = service?.moveToPrev()
    fun seekTo(position: Int) = service?.seekTo(position)
}

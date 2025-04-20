package kadyshev.dmitry.ui_player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.player_service.PlayerListener
import kadyshev.dmitry.player_service.PlayerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class PlayerServiceConnector(
    private val context: Context
) : PlayerListener {
    private var service: PlayerService? = null
    private var bound = false

    private val _playerState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val playerState: StateFlow<PlayerUiState> = _playerState

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as PlayerService.PlayerBinder).getService()
            bound = true
            service?.listener = this@PlayerServiceConnector
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            service = null
            _playerState.value = PlayerUiState.Error
        }
    }

    fun start(playerData: PlayerData, index: Int) {
        try {
            _playerState.value = PlayerUiState.Loading
            val intent = Intent(context, PlayerService::class.java)
            ContextCompat.startForegroundService(context, intent)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            service?.start(playerData, index)
        } catch (e: Exception) {
            _playerState.value = PlayerUiState.Error
        }
    }

    fun togglePlayPause() = service?.togglePlayPause()
     fun moveToNext() = service?.moveToNext()
     fun moveToPrev() = service?.moveToPrev()
//     fun seekTo(position: Int) = service?.seekTo(position)

    // PlayerListener callbacks
    override fun onTrackChanged(track: Track, index: Int) {
        val current = _playerState.value
        if (current is PlayerUiState.Content) {
            _playerState.value = current.copy(
                playerData = current.playerData.copy(
                    tracks = current.playerData.tracks.toMutableList().apply {
                        // Обновляем текущий трек в списке
                        if (index in indices) this[index] = track
                    }
                ),
                currentIndex = index
            )
        }
    }

    override fun onProgressChanged(current: Int, total: Int) {
        // Можно добавить прогресс в состояние, если нужно
    }

    override fun onPlayStateChanged(isPlaying: Boolean) {
        val current = _playerState.value
        if (current is PlayerUiState.Content) {
            _playerState.value = current.copy(isPlaying = isPlaying)
        }
    }

    fun unbind() {
        if (bound) {
            service?.listener = null
            context.unbindService(connection)
            bound = false
        }
    }
}
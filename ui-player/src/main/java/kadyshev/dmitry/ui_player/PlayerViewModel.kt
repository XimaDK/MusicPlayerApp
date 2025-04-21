package kadyshev.dmitry.ui_player

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.player_service.PlayerListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(
    private val playerServiceConnector: PlayerServiceConnector
) : ViewModel(), PlayerListener {

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack

    private val _trackDuration = MutableStateFlow(0)
    val trackDuration: StateFlow<Int> = _trackDuration

    private val _currentProgress = MutableStateFlow(0)
    val currentProgress: StateFlow<Int> = _currentProgress

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private var playerData: PlayerData? = null

    init {
        playerServiceConnector.setListener(this)
    }

    fun setPlayerData(data: PlayerData) {
        playerData = data
    }

    fun startPlayer() {
        playerData?.let {
            playerServiceConnector.startPlayer(it, it.currentIndex)
        }
    }

    fun unbindService() {
        playerServiceConnector.unbind()
    }

    fun togglePlayPause() {
        playerServiceConnector.togglePlayPause()
    }

    fun nextTrack() {
        playerServiceConnector.nextTrack()
    }

    fun previousTrack() {
        playerServiceConnector.previousTrack()
    }

    fun seekTo(position: Int) {
        playerServiceConnector.seekTo(position)
    }

    override fun onTrackChanged(track: Track, index: Int) {
        _currentTrack.value = track
        _trackDuration.value = playerServiceConnector.getCurrentTrackDuration()

    }

    override fun onProgressChanged(current: Int, total: Int) {
        _currentProgress.value = current
    }

    override fun onPlayStateChanged(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    override fun onTrackDurationReceived(duration: Int) {
        _trackDuration.value = duration
    }
}

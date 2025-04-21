package kadyshev.dmitry.ui_player

import androidx.lifecycle.ViewModel
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.player_service.PlayerListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(
    private val playerServiceConnector: PlayerServiceConnector
) : ViewModel(), PlayerListener {

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val uiState: StateFlow<PlayerUiState> = _uiState

    private var playerData: PlayerData? = null
    private var currentIndex: Int = 0
    private var isPlaying: Boolean = false
    private var currentTrackDuration: Int = 0
    private var currentProgress: Int = 0

    init {
        playerServiceConnector.setListener(this)
    }

    fun setPlayerData(data: PlayerData) {
        playerData = data
        _uiState.value = PlayerUiState.Loading
    }

    fun startPlayer() {
        playerData?.let {
            playerServiceConnector.startPlayer(it, it.currentIndex)
        } ?: run {
            _uiState.value = PlayerUiState.Error
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

    private fun updateUiState() {
        val data = playerData ?: return
        _uiState.value = PlayerUiState.Content(
            playerData = data,
            currentIndex = currentIndex,
            isPlaying = isPlaying,
            currentProgress = currentProgress,
            trackDuration = currentTrackDuration
        )
    }

    override fun onTrackChanged(track: Track, index: Int) {
        currentIndex = index
        currentTrackDuration = playerServiceConnector.getCurrentTrackDuration()
        updateUiState()
    }

    override fun onProgressChanged(current: Int, total: Int) {
        currentProgress = current
        updateUiState()
    }

    override fun onPlayStateChanged(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        updateUiState()
    }

    override fun onTrackDurationReceived(duration: Int) {
        currentTrackDuration = duration
        updateUiState()
    }
}

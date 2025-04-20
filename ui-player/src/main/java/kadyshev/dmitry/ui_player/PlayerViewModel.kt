package kadyshev.dmitry.ui_player


import androidx.lifecycle.ViewModel
import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.PlayerServiceInteractor
import kadyshev.dmitry.player_service.PlayerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class PlayerViewModel(
    private val playerServiceInteractor: PlayerServiceInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val state: StateFlow<PlayerUiState> = _state

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    fun startService(playerData: PlayerData, currentIndex: Int) {
        playerServiceInteractor.startService(playerData, currentIndex)
        _state.value = PlayerUiState.Content(
            playerData = playerData,
            currentIndex = currentIndex,
            isPlaying = true // Стартуем воспроизведение
        )
    }

    fun togglePlayPause() {
        playerServiceInteractor.sendAction(PlayerService.ACTION_TOGGLE)
        val current = _state.value
        if (current is PlayerUiState.Content) {
            _state.value = current.copy(isPlaying = !current.isPlaying)
        }
    }

    fun moveToNextTrack() {
        playerServiceInteractor.sendAction(PlayerService.ACTION_NEXT)
        updateTrackIndex { it + 1 }
    }

    fun moveToPrevTrack() {
        playerServiceInteractor.sendAction(PlayerService.ACTION_PREV)
        updateTrackIndex { it - 1 }
    }

    fun seekTo(position: Int) {
//        playerServiceInteractor.seekTo(position) // если реализуешь — можно будет использовать
    }

    private fun updateTrackIndex(update: (Int) -> Int) {
        val current = _state.value
        if (current is PlayerUiState.Content) {
            val newIndex = update(current.currentIndex)
                .coerceIn(0, current.playerData.tracks.lastIndex)
            _state.value = current.copy(currentIndex = newIndex)
        }
    }

    fun formatTime(ms: Int): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
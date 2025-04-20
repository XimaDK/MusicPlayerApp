package kadyshev.dmitry.ui_player

import kadyshev.dmitry.domain.entities.PlayerData

sealed class PlayerUiState {
    data object Loading : PlayerUiState()
    data class Content(
        val playerData: PlayerData,
        val currentIndex: Int,
        val isPlaying: Boolean
    ) : PlayerUiState()
    data object Error : PlayerUiState()
}
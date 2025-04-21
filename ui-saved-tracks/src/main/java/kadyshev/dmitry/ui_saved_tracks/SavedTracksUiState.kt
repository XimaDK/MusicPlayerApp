package kadyshev.dmitry.ui_saved_tracks

import kadyshev.dmitry.domain.entities.Track

sealed class SavedTracksUiState {
    data object Loading : SavedTracksUiState()
    data class Content(val tracks: List<Track>) : SavedTracksUiState()
    data class Error(val message: String): SavedTracksUiState()
}

package kadyshev.dmitry.ui_search

import kadyshev.dmitry.domain.entities.Track

sealed class SearchUiState {
    data object Loading: SearchUiState()
    data class Content(val tracks: List<Track>): SearchUiState()
    data class Error(val e: Throwable): SearchUiState()
}
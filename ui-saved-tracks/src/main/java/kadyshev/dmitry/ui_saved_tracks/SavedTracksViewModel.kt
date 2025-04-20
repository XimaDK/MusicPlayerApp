package kadyshev.dmitry.ui_saved_tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.usecases.GetAllTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SavedTracksViewModel(
    private val getAllTracksUseCase: GetAllTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SavedTracksUiState>(SavedTracksUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var allTracks = listOf<Track>()

    init {
        loadTracks()
    }

    private fun loadTracks() {
        viewModelScope.launch {
            getAllTracksUseCase()
                .onStart { _uiState.value = SavedTracksUiState.Loading }
                .catch { _uiState.value = SavedTracksUiState.Error(it) }
                .collect { tracks ->
                    allTracks = tracks
                    _uiState.value = SavedTracksUiState.Content(tracks)
                }
        }
    }

    fun searchTracks(query: String) {
        val filteredTracks = if (query.isBlank()) {
            allTracks
        } else {
            allTracks.filter { it.title.contains(query, ignoreCase = true) }
        }
        _uiState.value = SavedTracksUiState.Content(filteredTracks)
    }

}
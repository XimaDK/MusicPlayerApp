package kadyshev.dmitry.ui_saved_tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.usecases.DeleteTrackUseCase
import kadyshev.dmitry.domain.usecases.DownloadTrackUseCase
import kadyshev.dmitry.domain.usecases.GetAllTracksUseCase
import kadyshev.dmitry.ui_tracks_core.mapErrorToMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SavedTracksViewModel(
    private val getAllTracksUseCase: GetAllTracksUseCase,
    private val deleteTrackUseCase: DeleteTrackUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SavedTracksUiState>(SavedTracksUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var allTracks = listOf<Track>()

    init {
        loadTracks()
    }

    fun refresh() {
        loadTracks()
    }

    fun toggleTrackDownload(track: Track) {
        viewModelScope.launch {

            if (track.isDownloaded) {
                deleteTrackUseCase(track.id)
            } else {
                downloadTrackUseCase(track)
            }
        }
    }

    private fun loadTracks() {
        viewModelScope.launch {
            getAllTracksUseCase()
                .onStart { _uiState.value = SavedTracksUiState.Loading }
                .catch { _uiState.value = SavedTracksUiState.Error(mapErrorToMessage(it)) }
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
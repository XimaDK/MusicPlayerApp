package kadyshev.dmitry.ui_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.usecases.DeleteTrackUseCase
import kadyshev.dmitry.domain.usecases.DownloadTrackUseCase
import kadyshev.dmitry.domain.usecases.GetAllTracksUseCase
import kadyshev.dmitry.domain.usecases.GetChartFromApiUseCase
import kadyshev.dmitry.domain.usecases.SearchTracksFromApiUseCase
import kadyshev.dmitry.ui_tracks_core.mapErrorToMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksFromApiUseCase: SearchTracksFromApiUseCase,
    private val getChartFromApiUseCase: GetChartFromApiUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase,
    private val deleteTrackUseCase: DeleteTrackUseCase,
    private val getAllTracksUseCase: GetAllTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    private var currentQuery: String = ""

    init {
        loadChart()
    }

    private fun loadChart() {
        getChartFromApiUseCase()
            .onStart { _uiState.value = SearchUiState.Loading }
            .onEach { tracks -> updateUiWithSyncedTracks(tracks) }
            .catch { error -> _uiState.value = SearchUiState.Error(mapErrorToMessage(error)) }
            .launchIn(viewModelScope)
    }


    private suspend fun updateUiWithSyncedTracks(tracksFromApi: List<Track>) {
        val downloadedTracks = getAllTracksUseCase().first()
        val synced = tracksFromApi.map { track ->
            val local = downloadedTracks.find { it.id == track.id }
            track.copy(
                isDownloaded = local != null,
                localPath = local?.localPath
            )
        }
        _uiState.update { SearchUiState.Content(synced) }
    }

    fun refresh() {
        if (currentQuery.isBlank()) {
            loadChart()
        } else {
            onSearchQueryChanged(currentQuery)
        }
    }

    fun toggleTrackDownload(track: Track) {
        viewModelScope.launch {
            try {
                if (track.isDownloaded) {
                    deleteTrackUseCase(track.id)
                } else {
                    downloadTrackUseCase(track)
                }

                when (val currentState = _uiState.value) {
                    is SearchUiState.Content -> {
                        updateUiWithSyncedTracks(currentState.tracks)
                    }

                    else -> Unit
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(mapErrorToMessage(e))
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        currentQuery = query
        searchJob?.cancel()

        if (query.isBlank()) {
            loadChart()
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            try {
                searchTracksFromApiUseCase(query).collect { tracks ->
                    updateUiWithSyncedTracks(tracks)
                }
            } catch (e: Exception) {
                _uiState.update { SearchUiState.Error(mapErrorToMessage(e)) }
            }
        }
    }

}

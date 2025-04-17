package kadyshev.dmitry.ui_search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.usecases.DownloadTrackUseCase
import kadyshev.dmitry.domain.usecases.GetChartFromApiUseCase
import kadyshev.dmitry.domain.usecases.SearchTracksFromApiUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksFromApiUseCase: SearchTracksFromApiUseCase,
    private val getChartFromApiUseCase: GetChartFromApiUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {

        loadChart()
    }

    private fun loadChart() {
        viewModelScope.launch {
            try {
                val tracks = getChartFromApiUseCase()
                _uiState.value = SearchUiState.Content(tracks)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e)
                Log.d("tracks", e.toString())

            }
        }
    }

    fun saveTrack(track: Track) {
        viewModelScope.launch {
            try {
                downloadTrackUseCase(track)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error saving track: $e")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.update {
                when (it) {
                    is SearchUiState.Content -> it.copy(tracks = emptyList())
                    else -> it
                }
            }
            return
        }


        searchJob = viewModelScope.launch {
            delay(300)
            try {
                searchTracksFromApiUseCase(query).collect { tracks ->
                    _uiState.update { SearchUiState.Content(tracks) }
                }
            } catch (e: Exception) {
                _uiState.update { SearchUiState.Error(e) }
            }
        }
    }
}

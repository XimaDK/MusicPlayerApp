package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksFromApiUseCase(private val repository: TrackApiRepository) {

    suspend operator fun invoke(query: String): Flow<List<Track>> = flow {
        emit(repository.searchTracks(query))
    }
}
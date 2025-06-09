package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTracksUseCase @Inject constructor (private val repository: TrackDataSourceRepository) {
    operator fun invoke(): Flow<List<Track>> = repository.getAllTracks()
}
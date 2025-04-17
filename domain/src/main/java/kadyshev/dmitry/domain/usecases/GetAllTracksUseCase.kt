package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository

class GetAllTracksUseCase(private val repository: TrackDataSourceRepository) {
    suspend operator fun invoke(): List<Track> = repository.getAllTracks()
}
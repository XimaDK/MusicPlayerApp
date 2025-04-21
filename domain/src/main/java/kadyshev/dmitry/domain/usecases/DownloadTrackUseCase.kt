package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository

class DownloadTrackUseCase(private val trackRepository: TrackDataSourceRepository) {
    suspend operator fun invoke(track: Track) = trackRepository.downloadTrack(track)
}
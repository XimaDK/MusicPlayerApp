package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import javax.inject.Inject

class DownloadTrackUseCase @Inject constructor (private val trackRepository: TrackDataSourceRepository) {
    suspend operator fun invoke(track: Track) = trackRepository.downloadTrack(track)
}
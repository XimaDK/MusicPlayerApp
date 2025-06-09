package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import javax.inject.Inject

class DeleteTrackUseCase @Inject constructor (private val repository: TrackDataSourceRepository) {
    suspend operator fun invoke(trackId: Long) = repository.deleteTrackById(trackId)

}
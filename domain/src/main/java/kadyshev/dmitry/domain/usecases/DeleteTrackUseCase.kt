package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.repository.TrackDataSourceRepository

class DeleteTrackUseCase(private val repository: TrackDataSourceRepository) {
    suspend operator fun invoke(trackId: Long) = repository.deleteTrackById(trackId)

}
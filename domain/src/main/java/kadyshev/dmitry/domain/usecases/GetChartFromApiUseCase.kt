package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackApiRepository

class GetChartFromApiUseCase(private val repository: TrackApiRepository) {
    suspend operator fun invoke(): List<Track> = repository.getChart()
}
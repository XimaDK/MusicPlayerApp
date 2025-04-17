package kadyshev.dmitry.domain.usecases

import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetChartFromApiUseCase(private val repository: TrackApiRepository) {
    operator fun invoke(): Flow<List<Track>> = flow {
        emit(repository.getChart()) // Получаем чарт из репозитория и эмитим в Flow
    }
}
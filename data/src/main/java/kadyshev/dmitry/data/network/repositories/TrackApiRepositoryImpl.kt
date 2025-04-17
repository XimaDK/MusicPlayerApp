package kadyshev.dmitry.data.network.repositories

import android.util.Log
import kadyshev.dmitry.data.network.DeezerApi
import kadyshev.dmitry.data.Mapper
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackApiRepositoryImpl(private val api: DeezerApi, private val mapper: Mapper) :
    TrackApiRepository {

    override suspend fun searchTracks(query: String): Flow<List<Track>> = flow {
        val response = api.search(query)
        val mappedTracks = mapper.mapTracksDtoToDomain(response)
        emit(mappedTracks)
    }

    override suspend fun getChart(): List<Track> {
        val response = api.getChart()
        Log.d("tracks", "response: $response")
        return mapper.mapChartResponseDtoToDomain(response) // маппим в List<Track>
    }

}
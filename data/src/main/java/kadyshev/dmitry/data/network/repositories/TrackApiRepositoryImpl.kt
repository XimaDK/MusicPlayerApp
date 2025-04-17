package kadyshev.dmitry.data.network.repositories

import android.util.Log
import kadyshev.dmitry.data.Mapper
import kadyshev.dmitry.data.network.DeezerApi
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackApiRepository

class TrackApiRepositoryImpl(private val api: DeezerApi, private val mapper: Mapper) :
    TrackApiRepository {

    override suspend fun searchTracks(query: String): List<Track> {
        val response = api.search(query)
        val mappedTracks = mapper.mapTracksDtoToDomain(response)
        return mappedTracks
    }

    override suspend fun getChart(): List<Track> {
        val response = api.getChart()
        Log.d("tracks", "response: $response")
        return mapper.mapChartResponseDtoToDomain(response) // маппим в List<Track>
    }

}
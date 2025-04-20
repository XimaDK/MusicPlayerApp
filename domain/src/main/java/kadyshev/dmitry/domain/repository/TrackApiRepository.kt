package kadyshev.dmitry.domain.repository

import kadyshev.dmitry.domain.entities.Track

interface TrackApiRepository {
    suspend fun searchTracks(query: String): List<Track>
    suspend fun getChart(): List<Track>
}

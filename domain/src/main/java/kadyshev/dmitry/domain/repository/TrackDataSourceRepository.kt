package kadyshev.dmitry.domain.repository

import kadyshev.dmitry.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface TrackDataSourceRepository {

    suspend fun downloadTrack(track: Track)

    fun getAllTracks(): Flow<List<Track>>

    suspend fun deleteTrackById(id: Long)

}
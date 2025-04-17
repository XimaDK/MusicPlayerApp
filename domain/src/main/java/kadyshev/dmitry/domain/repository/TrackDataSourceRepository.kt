package kadyshev.dmitry.domain.repository

import kadyshev.dmitry.domain.entities.Track

interface TrackDataSourceRepository {

    suspend fun downloadTrack(track: Track)

    suspend fun getAllTracks(): List<Track>
}
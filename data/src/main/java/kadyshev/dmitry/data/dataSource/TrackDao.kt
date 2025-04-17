package kadyshev.dmitry.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kadyshev.dmitry.data.dataSource.dbEntity.TrackDBModel

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackDBModel)

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrackById(id: Int): TrackDBModel?

    @Query("SELECT * FROM tracks")
    suspend fun getAllTracks(): List<TrackDBModel>

    @Query("DELETE FROM tracks WHERE id = :id")
    suspend fun deleteTrack(id: Int)

}
package kadyshev.dmitry.data.dataSource.dbEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackDBModel(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val filePath: String,
    val isDownloaded: Boolean,
    val previewUrl: String,
    val album: String? = null,
    val coverUrl: String? = null
)
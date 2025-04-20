package kadyshev.dmitry.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val id: Long,
    val title: String,
    val previewUrl: String,
    val artist: String,
    val coverUrl: String? = null,
    val album: String? = null,
    val isDownloaded: Boolean = false,
    val localPath: String? = null
)

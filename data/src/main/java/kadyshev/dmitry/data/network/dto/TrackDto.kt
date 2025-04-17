package kadyshev.dmitry.data.network.dto

data class TrackDto(
    val id: Long,
    val title: String,
    val preview: String,
    val artist: ArtistDto,
    val album: AlbumDto
)

package kadyshev.dmitry.data.network.dto

data class SearchItemDto(
    val id: Int,
    val title: String,
    val artist: ArtistDto,
    val album: AlbumDto,
    val preview: String
)
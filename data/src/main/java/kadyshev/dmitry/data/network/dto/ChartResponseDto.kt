package kadyshev.dmitry.data.network.dto

data class ChartResponseDto(
    val tracks: TrackListDto,
    val albums: AlbumListDto,
    val artists: ArtistListDto
)

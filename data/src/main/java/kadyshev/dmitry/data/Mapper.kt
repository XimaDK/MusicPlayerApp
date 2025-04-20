package kadyshev.dmitry.data

import kadyshev.dmitry.data.dataSource.dbEntity.TrackDBModel
import kadyshev.dmitry.data.network.dto.ChartResponseDto
import kadyshev.dmitry.data.network.dto.SearchResponseDto
import kadyshev.dmitry.domain.entities.Track

class Mapper {

    fun mapTracksDtoToDomain(dto: SearchResponseDto): List<Track> {
        return dto.data.map { track ->
            Track(
                id = track.id,
                title = track.title,
                previewUrl = track.preview,
                artist = track.artist.name,
                album = track.album.title,
                coverUrl = track.album.cover
            )
        }
    }

    fun mapChartResponseDtoToDomain(dto: ChartResponseDto): List<Track> {
        return dto.tracks.data.map { trackDto ->
            Track(
                id = trackDto.id,
                title = trackDto.title,
                previewUrl = trackDto.preview,
                artist = trackDto.artist.name,
                album = trackDto.album.title,
                coverUrl = trackDto.album.cover
            )
        }
    }

    fun mapTrackEntityToDbModel(track: Track, filePath: String): TrackDBModel {
        return TrackDBModel(
            id = track.id,
            title = track.title,
            artist = track.artist,
            filePath = filePath,
            isDownloaded = true,
            previewUrl = track.previewUrl,
            coverUrl = track.coverUrl
        )
    }

    fun mapTrackDbModelToEntity(dbModel: TrackDBModel): Track {
        return Track(
            id = dbModel.id,
            title = dbModel.title,
            previewUrl = dbModel.previewUrl,
            artist = dbModel.artist,
            coverUrl = dbModel.coverUrl,
            album = dbModel.album,
            isDownloaded = dbModel.isDownloaded,
            localPath = dbModel.filePath
        )
    }

}
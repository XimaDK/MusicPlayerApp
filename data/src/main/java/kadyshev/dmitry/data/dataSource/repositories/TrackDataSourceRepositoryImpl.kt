package kadyshev.dmitry.data.dataSource.repositories

import android.content.Context
import kadyshev.dmitry.data.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kadyshev.dmitry.data.dataSource.TrackDao
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.domain.repository.TrackDataSourceRepository
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class TrackDataSourceRepositoryImpl(
    private val context: Context,
    private val trackDao: TrackDao,
    private val mapper: Mapper
) : TrackDataSourceRepository {

    override suspend fun downloadTrack(track: Track) {
        withContext(Dispatchers.IO) {
            try {
                val filePath = downloadFile(track.previewUrl, track.id)

                val entity = mapper.mapTrackEntityToDbModel(track, filePath)
                trackDao.insertTrack(entity)

            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    private fun downloadFile(urlStr: String, trackId: Long): String {
        val url = URL(urlStr)
        val file = File(context.filesDir, "track_$trackId.mp3")
        url.openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    override suspend fun getAllTracks(): List<Track> {
        return withContext(Dispatchers.IO) {
            trackDao.getAllTracks().map { entity ->
                mapper.mapTrackDbModelToEntity(entity)
            }
        }
    }
}

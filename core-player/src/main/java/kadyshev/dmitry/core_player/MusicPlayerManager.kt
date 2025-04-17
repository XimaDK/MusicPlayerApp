package kadyshev.dmitry.core_player

import android.media.MediaPlayer

class MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null

    fun play(previewUrl: String, onCompletion: () -> Unit = {}) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener { start() }
            setOnCompletionListener {
                onCompletion()
                release()
                mediaPlayer = null
            }
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true
}

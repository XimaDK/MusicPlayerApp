package kadyshev.dmitry.core_player

import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    var onProgressChanged: ((currentPosition: Int, duration: Int) -> Unit)? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // In MusicPlayerManager class
    var currentPosition: Int
        get() = mediaPlayer?.currentPosition ?: 0
        set(value) {
            mediaPlayer?.seekTo(value)
        }

    var duration: Int
        get() = mediaPlayer?.duration ?: 0
        set(value) {}


    fun play(previewUrl: String, onCompletion: () -> Unit = {}) {
        stop()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                start()
                startProgressUpdates()
            }
            setOnCompletionListener {
                stopProgressUpdates()
                onCompletion()
            }
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                stopProgressUpdates()
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                startProgressUpdates()
            }
        }
    }

    fun stop() {
        stopProgressUpdates()
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()
        progressJob = coroutineScope.launch {
            while (isActive) {
                val current = mediaPlayer?.currentPosition ?: 0
                val total = mediaPlayer?.duration ?: 0
                onProgressChanged?.invoke(current, total)
                delay(500L)
            }
        }
    }


    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }
}

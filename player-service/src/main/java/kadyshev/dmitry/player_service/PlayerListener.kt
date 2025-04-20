package kadyshev.dmitry.player_service

import kadyshev.dmitry.domain.entities.Track

interface PlayerListener {
    fun onTrackChanged(track: Track, index: Int)
    fun onProgressChanged(current: Int, total: Int)
    fun onPlayStateChanged(isPlaying: Boolean)
}

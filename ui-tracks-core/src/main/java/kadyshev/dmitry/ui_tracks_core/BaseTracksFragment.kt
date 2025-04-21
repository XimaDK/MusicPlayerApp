package kadyshev.dmitry.ui_tracks_core

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.domain.entities.Track
import org.koin.android.ext.android.inject

abstract class BaseTracksFragment : Fragment() {

    private val playerManager: MusicPlayerManager by inject()
    private var currentPlayingTrackId: Long? = null
    protected lateinit var adapter: TracksAdapter

    abstract val recyclerView: RecyclerView
    abstract fun onAddClick(track: Track)

    open fun setupSearch() {}
    open fun observeTracks() {}
    open fun setupRefreshLayout() {}
    abstract fun onTrackSelected(track: Track, trackList: List<Track>)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TracksAdapter(
            onTrackClick = { track ->
                val source = if (track.isDownloaded && track.localPath != null) {
                    track.localPath
                } else {
                    track.previewUrl
                } ?: return@TracksAdapter

                val trackList = adapter.currentList
                onTrackSelected(track, trackList)

                if (track.id == currentPlayingTrackId && playerManager.isPlaying()) {
                    playerManager.stop()
                    currentPlayingTrackId = null
                } else {
                    playerManager.play(source) {
                        currentPlayingTrackId = null
                        adapter.updatePlayingTrackId(null)
                    }
                    currentPlayingTrackId = track.id
                }

                adapter.updatePlayingTrackId(currentPlayingTrackId)
            },
            onAddClick = { track -> onAddClick(track) }
        )


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        setupSearch()
        observeTracks()
        setupRefreshLayout()

    }
}

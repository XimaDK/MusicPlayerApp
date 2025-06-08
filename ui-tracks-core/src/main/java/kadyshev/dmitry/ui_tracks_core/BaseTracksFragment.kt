package kadyshev.dmitry.ui_tracks_core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.player_service.PlayerService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

abstract class BaseTracksFragment : Fragment() {

    private var playerService: PlayerService? = null
    private var currentPlayingTrackId: Long? = null
    protected lateinit var adapter: TracksAdapter

    abstract val recyclerView: RecyclerView
    abstract fun onAddClick(track: Track)
    abstract fun onTrackSelected(track: Track, trackList: List<Track>)

    open fun setupSearch() {}
    open fun observeTracks() {}
    open fun setupRefreshLayout() {}

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? PlayerService.PlayerBinder
            playerService = binder?.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playerService = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TracksAdapter(
            onTrackClick = { track ->
                val trackList = adapter.currentList
                onTrackSelected(track, trackList)

                if (track.id == currentPlayingTrackId && playerService?.isPlaying() == true) {
                    playerService?.togglePlayPause()
                    currentPlayingTrackId = null
                } else {
                    val startIndex = trackList.indexOf(track)
                    val playerData = PlayerData(trackList, startIndex)

                    val intent = Intent(requireContext(), PlayerService::class.java).apply {
                        putExtra("playerData", Json.encodeToString(playerData))
                        putExtra("startIndex", startIndex)
                    }

                    requireContext().startService(intent)
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

        bindToPlayerService()
    }

    private fun bindToPlayerService() {
        val intent = Intent(requireContext(), PlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unbindService(serviceConnection)
    }
}


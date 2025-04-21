package kadyshev.dmitry.ui_saved_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kadyshev.dmitry.core_navigtaion.PlayerNavigation
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.ui_saved_tracks.databinding.FragmentSavedTracksBinding
import kadyshev.dmitry.ui_tracks_core.BaseTracksFragment
import kadyshev.dmitry.ui_tracks_core.showToast
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SavedTracksFragment : BaseTracksFragment() {

    private var _binding: FragmentSavedTracksBinding? = null
    private val binding: FragmentSavedTracksBinding
        get() = _binding ?: throw RuntimeException("FragmentSavedTracksBinding == null")

    private val viewModel: SavedTracksViewModel by viewModel()

    override val recyclerView get() = binding.tracksRecyclerView

    private val playerNavigation: PlayerNavigation by inject()

    override fun onAddClick(track: Track) {
        viewModel.toggleTrackDownload(track)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedTracksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setupSearch() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.searchTracks(it.toString())
        }
    }

    override fun setupRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun observeTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.swipeRefresh.isRefreshing = false
                when (state) {
                    is SavedTracksUiState.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                    }

                    is SavedTracksUiState.Content -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.tracksRecyclerView.visibility = View.VISIBLE
                        adapter.submitList(state.tracks)
                    }

                    is SavedTracksUiState.Error -> {
                        binding.tracksRecyclerView.visibility = View.GONE
                        showToast(requireContext(), state.message)                    }
                }
            }
        }
    }

    override fun onTrackSelected(track: Track, trackList: List<Track>) {
        val index = trackList.indexOfFirst { it.id == track.id }
        val playerData = PlayerData(trackList, index)

        val playerDataJson = Json.encodeToString(playerData)

        playerNavigation.openPlayer(this, playerDataJson)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
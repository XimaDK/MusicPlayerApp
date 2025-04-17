package kadyshev.dmitry.ui_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.ui_search.databinding.FragmentSearchBinding
import kadyshev.dmitry.ui_tracks_core.TracksAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")

    private val viewModel: SearchViewModel by viewModel()
    private val playerManager: MusicPlayerManager by inject()
    private var currentPlayingTrackId: Long? = null
    private lateinit var adapter: TracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TracksAdapter(
            onTrackClick = { track ->
                if (track.id == currentPlayingTrackId && playerManager.isPlaying()) {
                    playerManager.stop()
                    currentPlayingTrackId = null
                } else {
                    playerManager.play(track.previewUrl) {
                        currentPlayingTrackId = null
                        adapter.updatePlayingTrackId(null)
                    }
                    currentPlayingTrackId = track.id
                }
                adapter.updatePlayingTrackId(currentPlayingTrackId)
            },
            onAddClick = { track ->
                viewModel.saveTrack(track)
            }
        )
        binding.tracksRecyclerView.adapter = adapter
        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.tracksRecyclerView.adapter = adapter
        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchEditText.doAfterTextChanged {
            viewModel.onSearchQueryChanged(it.toString())
        }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is SearchUiState.Content -> adapter.submitList(state.tracks)
                    is SearchUiState.Error -> {
                    }

                    SearchUiState.Loading -> {
                    }
                }
            }
        }
    }

}
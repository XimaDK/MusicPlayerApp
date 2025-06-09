package kadyshev.dmitry.ui_search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kadyshev.dmitry.core_di.SearchDependencies
import kadyshev.dmitry.core_navigtaion.PlayerNavigation
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.ui_search.databinding.FragmentSearchBinding
import kadyshev.dmitry.ui_tracks_core.BaseTracksFragment
import kadyshev.dmitry.ui_tracks_core.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SearchFragment : BaseTracksFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val recyclerView get() = binding.tracksRecyclerView

    @Inject
    lateinit var playerNavigation: PlayerNavigation

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    private lateinit var searchComponent: SearchComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val deps = requireActivity().application as SearchDependencies

        searchComponent = DaggerSearchComponent
            .factory()
            .create(deps)

        searchComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        Log.d("ViewModelSearchFragment", viewModel.toString())
        return binding.root
    }

    override fun setupRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun setupSearch() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.onSearchQueryChanged(it.toString())
        }
    }

    override fun observeTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.swipeRefresh.isRefreshing = false
                when (state) {
                    is SearchUiState.Content -> {
                        adapter.submitList(state.tracks)
                        binding.tracksRecyclerView.visibility = View.VISIBLE
                        binding.swipeRefresh.isRefreshing = false
                    }

                    is SearchUiState.Error -> {
                        binding.tracksRecyclerView.visibility = View.GONE
                        showToast(requireContext(), state.message)
                    }

                    SearchUiState.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                    }
                }
            }
        }
    }


    override fun onAddClick(track: Track) {
        viewModel.toggleTrackDownload(track)
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

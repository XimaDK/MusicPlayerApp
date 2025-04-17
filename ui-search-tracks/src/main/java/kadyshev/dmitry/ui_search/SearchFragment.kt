package kadyshev.dmitry.ui_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.ui_search.databinding.FragmentSearchBinding
import kadyshev.dmitry.ui_tracks_core.BaseTracksFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseTracksFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")

    private val viewModel: SearchViewModel by viewModel()

    override val recyclerView get() = binding.tracksRecyclerView

    override fun onAddClick(track: Track) {
        viewModel.saveTrack(track)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setupSearch() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.onSearchQueryChanged(it.toString())
        }
    }

    override fun observeTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is SearchUiState.Content -> adapter.submitList(state.tracks)
                    is SearchUiState.Error -> { /* handle error */
                    }

                    SearchUiState.Loading -> { /* show loading */
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
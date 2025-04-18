package kadyshev.dmitry.ui_saved_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.ui_saved_tracks.databinding.FragmentSavedTracksBinding
import kadyshev.dmitry.ui_tracks_core.BaseTracksFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SavedTracksFragment : BaseTracksFragment() {

    private var _binding: FragmentSavedTracksBinding? = null
    private val binding: FragmentSavedTracksBinding
        get() = _binding ?: throw RuntimeException("FragmentSavedTracksBinding == null")

    private val viewModel: SavedTracksViewModel by viewModel()

    override val recyclerView get() = binding.tracksRecyclerView

    override fun onAddClick(track: Track) {
        //потом переделать под удалить/добавить
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

    override fun observeTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is SavedTracksUiState.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                    binding.tracksRecyclerView.visibility = View.GONE
//                    binding.errorText.visibility = View.GONE
                    }

                    is SavedTracksUiState.Content -> {
//                    binding.progressBar.visibility = View.GONE
                        binding.tracksRecyclerView.visibility = View.VISIBLE
//                    binding.errorText.visibility = View.GONE
                        adapter.submitList(state.tracks)
                    }

                    is SavedTracksUiState.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    binding.tracksRecyclerView.visibility = View.GONE
//                    binding.errorText.visibility = View.VISIBLE
//                    binding.errorText.text = state.message
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
package kadyshev.dmitry.ui_saved_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

//    override fun observeTracks() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.savedTracks.collectLatest {
//                adapter.submitList(it)
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
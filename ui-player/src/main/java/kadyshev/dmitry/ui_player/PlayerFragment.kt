package kadyshev.dmitry.ui_player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import kadyshev.dmitry.core_navigtaion.PlayerNavigation
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.ui_player.databinding.FragmentPlayerBinding
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding ?: throw RuntimeException("FragmentPlayerBinding == null")

    private val viewModel: PlayerViewModel by viewModel()

    private val playerNavigation: PlayerNavigation by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playerDataJson = requireArguments().getString("playerData") ?: return
        val playerData = Json.decodeFromString<PlayerData>(playerDataJson)
        viewModel.setPlayerData(playerData)
        viewModel.startPlayer()

        binding.backButton.setOnClickListener {
            playerNavigation.popBackFromPlayer(this)
        }

        setupListeners()
        observeUiState()
    }

    private fun setupListeners() = with(binding) {
        playPauseButton.setOnClickListener {
            viewModel.togglePlayPause()
        }

        nextTrackButton.setOnClickListener {
            viewModel.nextTrack()
        }

        prevTrackButton.setOnClickListener {
            viewModel.previousTrack()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var userIsSeeking = false

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.currentTime.text = formatMillis(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                userIsSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                userIsSeeking = false
                seekBar?.progress?.let { viewModel.seekTo(it) }
            }
        })
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is PlayerUiState.Loading -> {
                            binding.loadingOverlay.visibility = View.VISIBLE
                            binding.errorText.visibility = View.GONE
                        }

                        is PlayerUiState.Error -> {
                            binding.loadingOverlay.visibility = View.GONE
                            binding.errorText.visibility = View.VISIBLE
                        }

                        is PlayerUiState.Content -> {
                            binding.loadingOverlay.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            renderContent(state)
                        }
                    }
                }
            }
        }
    }

    private fun renderContent(state: PlayerUiState.Content) = with(binding) {
        val track = state.playerData.tracks[state.currentIndex]

        trackName.text = track.title
        artist.text = track.artist
        album.text = track.album
        trackImage.load(track.coverUrl)

        playPauseButton.setImageResource(
            if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )

        seekBar.max = state.trackDuration
        seekBar.progress = state.currentProgress
        trackDuration.text = formatMillis(state.trackDuration)
        currentTime.text = formatMillis(state.currentProgress)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbindService()
        _binding = null
    }

    private fun formatMillis(ms: Int): String {
        val seconds = ms / 1000 % 60
        val minutes = ms / 1000 / 60
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}
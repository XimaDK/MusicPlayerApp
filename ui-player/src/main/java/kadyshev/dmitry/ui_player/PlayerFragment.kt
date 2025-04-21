package kadyshev.dmitry.ui_player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.ui_player.databinding.FragmentPlayerBinding
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel()

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

        setupListeners()
        observeViewModel()
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

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentTrack.collect { track ->
                        Log.d("TRACK", track.toString())
                        track?.let {
                            binding.trackName.text = it.title
                            binding.artist.text = it.artist
                            binding.trackImage.load(it.coverUrl)
                            binding.album.text = it.album

                        }
                    }
                }
                launch {
                    viewModel.trackDuration.collect { duration ->
                        Log.d("duration", duration.toString())
                        if (duration > 0) {
                            binding.trackDuration.text = formatMillis(duration)
                            binding.seekBar.max = duration
                        }
                    }
                }
                launch {
                    viewModel.currentProgress.collect { progress ->
                        binding.seekBar.progress = progress
                        binding.currentTime.text = formatMillis(progress)
                    }
                }

                launch {
                    viewModel.isPlaying.collect { isPlaying ->
                        binding.playPauseButton.setImageResource(
                            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                        )
                    }
                }
            }
        }
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
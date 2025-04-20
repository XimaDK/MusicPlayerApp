package kadyshev.dmitry.ui_player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.player_service.PlayerListener
import kadyshev.dmitry.player_service.PlayerService
import kadyshev.dmitry.ui_player.databinding.FragmentPlayerBinding
import kotlinx.serialization.json.Json

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private var service: PlayerService? = null
    private var bound = false

    private var initPlayerData: PlayerData? = null
    private var initIndex: Int = 0
    private var isSeeking = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as PlayerService.PlayerBinder).getService()
            bound = true

            // Регистрируем коллбэки для обновления UI
            service!!.listener = object : PlayerListener {
                override fun onTrackChanged(track: Track, index: Int) {
                    binding.trackName.text = track.title
                    binding.artist.text = track.artist
                    binding.album.text = track.album ?: ""
                    binding.trackImage.load(track.coverUrl) { crossfade(true) }
                }

                override fun onProgressChanged(current: Int, total: Int) {
                    if (!isSeeking) {
                        binding.seekBar.max = total
                        binding.seekBar.progress = current
                        binding.currentTime.text = formatTime(current)
                        binding.trackDuration.text = formatTime(total)
                    }
                }

                override fun onPlayStateChanged(isPlaying: Boolean) {
                    binding.playPauseButton.setImageResource(
                        if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    )
                }
            }
            initPlayerData?.let { data ->
                service!!.start(data, initIndex)
                initPlayerData = null
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            service = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        arguments?.getString(ARGUMENTS_KEY)?.let { json ->
            initPlayerData = Json.decodeFromString<PlayerData>(json)
            initIndex = initPlayerData?.currentIndex ?: 0
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        // Стартуем сервис (foreground) и биндимся к нему
        val svcIntent = Intent(requireContext(), PlayerService::class.java)
        ContextCompat.startForegroundService(requireContext(), svcIntent)
        requireContext().bindService(svcIntent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            service?.listener = null
            requireContext().unbindService(connection)
            bound = false
        }
    }

    private fun setupListeners() {
        binding.playPauseButton.setOnClickListener {
            service?.togglePlayPause()
        }
        binding.nextTrackButton.setOnClickListener {
            service?.moveToNext()
        }
        binding.prevTrackButton.setOnClickListener {
            service?.moveToPrev()
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.currentTime.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                sb?.let {
                    service?.seekTo(it.progress)
                }
                isSeeking = false
            }
        })
    }

    private fun formatTime(ms: Int): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(java.util.Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARGUMENTS_KEY = "playerData"
    }
}
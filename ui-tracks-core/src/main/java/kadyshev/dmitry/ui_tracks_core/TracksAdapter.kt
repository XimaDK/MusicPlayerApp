package kadyshev.dmitry.ui_tracks_core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kadyshev.dmitry.domain.entities.Track
import kadyshev.dmitry.ui_tracks_core.databinding.ItemTrackBinding

class TracksAdapter(
    private val onTrackClick: (Track) -> Unit,
    private val onAddClick: (Track) -> Unit,
    private var currentPlayingId: Long? = null
) : ListAdapter<Track, TracksAdapter.TrackViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding =
            ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun updatePlayingTrackId(newId: Long?) {
        val oldId = currentPlayingId
        currentPlayingId = newId

        val oldPosition = currentList.indexOfFirst { it.id == oldId }
        if (oldPosition != -1) notifyItemChanged(oldPosition)

        val newPosition = currentList.indexOfFirst { it.id == newId }
        if (newPosition != -1) notifyItemChanged(newPosition)
    }

    inner class TrackViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) = with(binding) {
            titleTextView.text = track.title
            artistTextView.text = track.artist
            coverImage.load(track.coverUrl) {
                crossfade(true)
            }

            pauseIcon.isVisible = track.id == currentPlayingId

            addButton.setImageResource(
                if (track.isDownloaded) R.drawable.ic_added else R.drawable.ic_add
            )

            addButton.setOnClickListener {
                onAddClick(track)
            }

        }

    }

    object DiffCallback : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Track, newItem: Track) = oldItem == newItem
    }
}

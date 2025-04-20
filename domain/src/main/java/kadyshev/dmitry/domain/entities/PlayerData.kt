package kadyshev.dmitry.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    val tracks: List<Track>,
    val currentIndex: Int
)

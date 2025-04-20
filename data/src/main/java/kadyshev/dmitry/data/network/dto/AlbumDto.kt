package kadyshev.dmitry.data.network.dto

import com.google.gson.annotations.SerializedName

data class AlbumDto(
    val title: String,
    @SerializedName("cover_big") val cover: String
)
package kadyshev.dmitry.data.network

import kadyshev.dmitry.data.network.dto.ChartResponseDto
import kadyshev.dmitry.data.network.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApi {
    @GET("chart")
    suspend fun getChart(): ChartResponseDto

    @GET("search")
    suspend fun search(@Query("q") query: String): SearchResponseDto
}

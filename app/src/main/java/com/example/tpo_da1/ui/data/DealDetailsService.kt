import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.domain.DealDetailsResponse
import com.google.android.gms.common.api.ApiException
import retrofit2.awaitResponse

class DealDetailsService(private val api: CheapSharkApi) {
    suspend fun getDealDetails(dealID: String): DealDetailsResponse {
        val response = api.getDealDetails("https://www.cheapshark.com/api/1.0/deals?id=$dealID").awaitResponse()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Deal not found")
        } else {
            throw Exception("Failed to fetch deal: ${response.code()} - ${response.message()}")
        }
    }
}
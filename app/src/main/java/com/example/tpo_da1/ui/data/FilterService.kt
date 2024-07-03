import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.domain.Store
import retrofit2.awaitResponse

class FilterService(private val api: CheapSharkApi) {

    suspend fun getStores(): List<Store> {
        val response = api.getStores().awaitResponse()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No stores found")
        } else {
            throw Exception("Failed to fetch stores: ${response.code()} - ${response.message()}")
        }
    }
}
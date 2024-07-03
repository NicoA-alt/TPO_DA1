package com.example.tpo_da1.ui.data.deal

import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.domain.Deal
import retrofit2.awaitResponse

class DealsService(private val api: CheapSharkApi) {
    suspend fun getDeals(page: Int, pageSize: Int): List<Deal> {
        val response = api.getDeals(page, pageSize).awaitResponse()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No deals found")
        } else {
            throw Exception("Failed to fetch deals: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun searchDeals(query: String, page: Int, desc: Int, sortBy: String, lowerPrice: Int, upperPrice: Int, storeID: Int): List<Deal> {
        val response = if (storeID == 0) {
            api.searchDeals(query, page, desc, sortBy, lowerPrice, upperPrice).awaitResponse()
        } else {
            api.searchDealsWithStoreID(query, page, desc, sortBy, lowerPrice, upperPrice, storeID).awaitResponse()
        }

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No deals found")
        } else {
            throw Exception("Failed to search deals: ${response.code()} - ${response.message()}")
        }
    }
}
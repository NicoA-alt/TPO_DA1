package com.example.tpo_da1.ui.data

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

    suspend fun searchDeals(query: String, page: Int): List<Deal> {
        val response = api.searchDeals(query, page).awaitResponse()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No deals found")
        } else {
            throw Exception("Failed to search deals: ${response.code()} - ${response.message()}")
        }
    }
}
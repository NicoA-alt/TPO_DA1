package com.example.tpo_da1.ui.data.deal
import com.example.tpo_da1.ui.domain.Deal

class DealsRepository(private val service: DealsService) {
    suspend fun getDeals(page: Int, pageSize: Int): List<Deal> {
        return service.getDeals(page, pageSize)
    }

    suspend fun searchDeals(query: String, page: Int, desc: Int, sortBy: String, lowerPrice: Int, upperPrice: Int, storeID: Int): List<Deal> {
        return service.searchDeals(query, page, desc, sortBy, lowerPrice, upperPrice, storeID)
    }
}
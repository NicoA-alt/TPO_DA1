package com.example.tpo_da1.ui.data

import DealDetailsService
import com.example.tpo_da1.ui.domain.DealDetailsResponse

class DealDetailsRepository(private val service: DealDetailsService) {
    suspend fun getDealDetails(dealID: String): DealDetailsResponse {
        return service.getDealDetails(dealID)
    }
}
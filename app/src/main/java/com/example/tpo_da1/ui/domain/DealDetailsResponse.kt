package com.example.tpo_da1.ui.domain

data class DealDetailsResponse(
    val gameInfo: DealDetails?,
    val cheaperStores: List<CheaperStore>?,
    val cheapestPrice: CheapestPrice?
)
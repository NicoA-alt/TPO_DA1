package com.example.tpo_da1.ui.domain

data class DealDetails(
    val storeID: String?,
    val name: String?,
    val salePrice: String?,
    val retailPrice: String?,
    val steamRatingText: String?,
    val steamRatingPercent: String?,
    val metacriticScore: String?,
    val thumb: String?,
    val cheapestPrice: CheapestPrice?
)
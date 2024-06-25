package com.example.tpo_da1.ui.domain

data class DealDetails(
    val storeID: String?,
    val gameID: String?,
    val name: String?,
    val steamAppID: String?,
    val salePrice: String?,
    val retailPrice: String?,
    val steamRatingText: String?,
    val steamRatingPercent: String?,
    val steamRatingCount: String?,
    val metacriticScore: String?,
    val metacriticLink: String?,
    val releaseDate: Long?,
    val publisher: String?,
    val steamworks: String?,
    val thumb: String?,
    val cheapestPrice: CheapestPrice?
)
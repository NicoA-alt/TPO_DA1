package com.example.tpo_da1.ui.domain

data class FilterSettings(
    val order: Int,
    val sortBy: String,
    val lowerPrice: Int,
    val upperPrice: Int,
    val storeID: Int
)
package com.example.tpo_da1.ui.domain

data class Store(
    val storeID: String,
    val storeName: String,
    val isActive: Int,
    val images: StoreImages
)
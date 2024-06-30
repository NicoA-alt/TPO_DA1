package com.example.tpo_da1.ui.data.dbLocal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deal_details")
data class DealDetailsEntity(
    @PrimaryKey val dealID: String,
    val name: String?,
    val salePrice: String?,
    val retailPrice: String?,
    val steamRatingText: String?,
    val steamRatingPercent: String?,
    val metacriticScore: String?,
    val thumb: String?
)
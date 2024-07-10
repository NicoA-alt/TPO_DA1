package com.example.tpo_da1.ui.data.dealDetails

import android.content.Context
import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.data.dbLocal.AppDatabase
import com.example.tpo_da1.ui.data.dbLocal.DealDetailsEntity
import com.example.tpo_da1.ui.domain.DealDetails
import com.example.tpo_da1.ui.domain.DealDetailsResponse
import retrofit2.awaitResponse

class DealDetailsService(private val api: CheapSharkApi, context: Context) {
    private val dealDetailsDao = AppDatabase.getDatabase(context).dealDetailsDao()

    suspend fun getDealDetails(dealID: String): DealDetailsResponse {
        return try {
            val dealDetailsResponse = getDealDetailsFromApi(dealID)
            val dealDetailsEntity = DealDetailsEntity(
                dealID = dealID,
                name = dealDetailsResponse.gameInfo?.name,
                salePrice = dealDetailsResponse.gameInfo?.salePrice,
                retailPrice = dealDetailsResponse.gameInfo?.retailPrice,
                steamRatingText = dealDetailsResponse.gameInfo?.steamRatingText,
                steamRatingPercent = dealDetailsResponse.gameInfo?.steamRatingPercent,
                metacriticScore = dealDetailsResponse.gameInfo?.metacriticScore,
                thumb = dealDetailsResponse.gameInfo?.thumb
            )
            dealDetailsDao.insertDealDetails(dealDetailsEntity)
            dealDetailsResponse
        } catch (e: Exception) {
            val cachedDealDetails = dealDetailsDao.getDealDetails(dealID)
            if (cachedDealDetails != null) {
                DealDetailsResponse(
                    gameInfo = DealDetails(
                        storeID = null,
                        name = cachedDealDetails.name,
                        salePrice = cachedDealDetails.salePrice,
                        retailPrice = cachedDealDetails.retailPrice,
                        steamRatingText = cachedDealDetails.steamRatingText,
                        steamRatingPercent = cachedDealDetails.steamRatingPercent,
                        metacriticScore = cachedDealDetails.metacriticScore,
                        thumb = cachedDealDetails.thumb,
                        cheapestPrice = null
                    ),
                    cheaperStores = null,
                    cheapestPrice = null
                )
            } else {
                throw e
            }
        }
    }

    private suspend fun getDealDetailsFromApi(dealID: String): DealDetailsResponse {
        val response = api.getDealDetails("https://www.cheapshark.com/api/1.0/deals?id=$dealID").awaitResponse()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Deal not found")
        } else {
            throw Exception("Failed to fetch deal: ${response.code()} - ${response.message()}")
        }
    }
}
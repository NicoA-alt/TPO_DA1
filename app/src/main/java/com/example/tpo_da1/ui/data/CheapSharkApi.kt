package com.example.tpo_da1.ui.data

import com.example.tpo_da1.ui.domain.Deal
import com.example.tpo_da1.ui.domain.DealDetailsResponse
import com.example.tpo_da1.ui.domain.Store
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CheapSharkApi {
    @GET("deals")
    fun getDeals(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<List<Deal>>

    @GET("deals")
    fun searchDeals(
        @Query("title") query: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("desc") desc: Int,
        @Query("sortBy") sortBy: String,
        @Query("lowerPrice") lowerPrice: Int,
        @Query("upperPrice") upperPrice: Int
    ): Call<List<Deal>>

    @GET("deals")
    fun searchDealsWithStoreID(
        @Query("title") query: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("desc") desc: Int,
        @Query("sortBy") sortBy: String,
        @Query("lowerPrice") lowerPrice: Int,
        @Query("upperPrice") upperPrice: Int,
        @Query("storeID") storeID: Int
    ): Call<List<Deal>>

    @GET
    fun getDealDetails(@Url url: String): Call<DealDetailsResponse>
    @GET("stores")
    fun getStores(): Call<List<Store>>
}
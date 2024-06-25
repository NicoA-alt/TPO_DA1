package com.example.tpo_da1.ui.data

import com.example.tpo_da1.ui.domain.Deal
import com.example.tpo_da1.ui.domain.DealDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CheapSharkApi {
    @GET("deals")
    fun getDeals(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<List<Deal>>

    @GET("deals")
    fun searchDeals(@Query("title") query: String, @Query("pageNumber") pageNumber: Int): Call<List<Deal>>
    @GET
    fun getDealDetails(@Url url: String): Call<DealDetailsResponse>
}
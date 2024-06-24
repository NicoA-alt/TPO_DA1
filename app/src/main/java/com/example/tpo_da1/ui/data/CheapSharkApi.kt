package com.example.tpo_da1.ui.data

import com.example.tpo_da1.ui.domain.Deal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {
    @GET("deals")
    fun getDeals(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<List<Deal>>

    @GET("deals")
    fun searchDeals(@Query("title") query: String, @Query("pageNumber") pageNumber: Int): Call<List<Deal>>
}
package com.example.tpo_da1.ui.data

import com.example.tpo_da1.ui.domain.Deal
import retrofit2.Call
import retrofit2.http.GET

interface CheapSharkApi {
    @GET("deals")
    fun getDeals(): Call<List<Deal>>
}
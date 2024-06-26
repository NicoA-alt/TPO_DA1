package com.example.tpo_da1.ui.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://www.cheapshark.com/api/1.0/"

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getCheapSharkApi(): CheapSharkApi {
        return getRetrofit().create(CheapSharkApi::class.java)
    }
}
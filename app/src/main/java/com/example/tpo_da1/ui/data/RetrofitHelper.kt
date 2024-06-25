package com.example.tpo_da1.ui.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit():Retrofit{
        return  Retrofit.Builder()
        .baseUrl("https://www.cheapshark.com/api/1.0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
}
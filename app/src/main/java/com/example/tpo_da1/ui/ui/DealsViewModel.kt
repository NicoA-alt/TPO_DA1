package com.example.tpo_da1.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.domain.Deal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DealsViewModel : ViewModel() {
    private val _deals = MutableLiveData<List<Deal>>()
    val deals: LiveData<List<Deal>> get() = _deals

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.cheapshark.com/api/1.0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(CheapSharkApi::class.java)

    init {
        fetchDeals()
    }

    private fun fetchDeals() {
        api.getDeals().enqueue(object : Callback<List<Deal>> {
            override fun onResponse(call: Call<List<Deal>>, response: Response<List<Deal>>) {
                if (response.isSuccessful) {
                    _deals.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Deal>>, t: Throwable) {
                // Manejar errores
            }
        })
    }
}
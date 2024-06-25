package com.example.tpo_da1.ui.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.domain.CheapestPrice
import com.example.tpo_da1.ui.domain.DealDetails
import com.example.tpo_da1.ui.domain.DealDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DealDetailsViewModel : ViewModel() {
    private val _dealDetails = MutableLiveData<DealDetails?>()
    val dealDetails: LiveData<DealDetails?> get() = _dealDetails

    private val _cheapestPrice = MutableLiveData<CheapestPrice?>()
    val cheapestPrice: LiveData<CheapestPrice?> get() = _cheapestPrice

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.cheapshark.com/api/1.0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(CheapSharkApi::class.java)

    fun fetchDealDetails(dealID: String) {
        _loading.value = true
        Log.d("DealsViewModel", "Fetching deal details for deal ID: $dealID")
        val call = api.getDealDetails("https://www.cheapshark.com/api/1.0/deals?id=$dealID")
        Log.d("DealsViewModel", "Sent request to URL: ${call.request().url}")
        call.enqueue(object : Callback<DealDetailsResponse> {
            override fun onResponse(call: Call<DealDetailsResponse>, response: Response<DealDetailsResponse>) {
                if (response.isSuccessful) {
                    _dealDetails.value = response.body()?.gameInfo
                    _cheapestPrice.value = response.body()?.cheapestPrice
                } else {
                    _dealDetails.value = null
                    _cheapestPrice.value = null
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<DealDetailsResponse>, t: Throwable) {
                _loading.value = false
                _dealDetails.value = null
                _cheapestPrice.value = null
                Log.e("DealsViewModel", "Failed to fetch deal details", t)
            }
        })
    }
}
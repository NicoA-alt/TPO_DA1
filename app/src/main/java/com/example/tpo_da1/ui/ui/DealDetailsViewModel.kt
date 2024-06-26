package com.example.tpo_da1.ui.ui

import DealDetailsService
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.data.DealDetailsRepository
import com.example.tpo_da1.ui.data.RetrofitHelper
import com.example.tpo_da1.ui.domain.CheapestPrice
import com.example.tpo_da1.ui.domain.DealDetails
import com.example.tpo_da1.ui.domain.DealDetailsResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DealDetailsViewModel : ViewModel() {
    private val _dealDetails = MutableLiveData<DealDetails?>()
    val dealDetails: LiveData<DealDetails?> get() = _dealDetails

    private val _cheapestPrice = MutableLiveData<CheapestPrice?>()
    val cheapestPrice: LiveData<CheapestPrice?> get() = _cheapestPrice

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val repository = DealDetailsRepository(DealDetailsService(RetrofitHelper.getCheapSharkApi()))

    fun fetchDealDetails(dealID: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getDealDetails(dealID)
                _dealDetails.value = response.gameInfo
                _cheapestPrice.value = response.cheapestPrice
            } catch (e: Exception) {
                _dealDetails.value = null
                _cheapestPrice.value = null
            }
            _loading.value = false
        }
    }
}
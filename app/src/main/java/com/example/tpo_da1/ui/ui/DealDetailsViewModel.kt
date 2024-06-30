package com.example.tpo_da1.ui.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tpo_da1.ui.data.DealDetails.DealDetailsRepository
import com.example.tpo_da1.ui.data.DealDetails.DealDetailsService
import com.example.tpo_da1.ui.data.RetrofitHelper
import com.example.tpo_da1.ui.domain.CheapestPrice
import com.example.tpo_da1.ui.domain.DealDetails
import kotlinx.coroutines.launch

class DealDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val _dealDetails = MutableLiveData<DealDetails?>()
    val dealDetails: LiveData<DealDetails?> get() = _dealDetails

    private val _cheapestPrice = MutableLiveData<CheapestPrice?>()
    val cheapestPrice: LiveData<CheapestPrice?> get() = _cheapestPrice

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val repository = DealDetailsRepository(DealDetailsService(RetrofitHelper.getCheapSharkApi(), application))

    fun fetchDealDetails(dealID: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getDealDetails(dealID)
                _dealDetails.value = response.gameInfo
                _cheapestPrice.value = response.cheapestPrice
                Log.d("DealDetailsViewModel", "Deal details fetched: ${response.gameInfo}")
            } catch (e: Exception) {
                _dealDetails.value = null
                _cheapestPrice.value = null
                Log.e("DealDetailsViewModel", "Error fetching deal details: ${e.message}")
            }
            _loading.value = false
        }
    }
}
package com.example.tpo_da1.ui.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.data.DealsRepository
import com.example.tpo_da1.ui.data.DealsService
import com.example.tpo_da1.ui.data.RetrofitHelper
import com.example.tpo_da1.ui.domain.Deal
import com.example.tpo_da1.ui.domain.DealDetails
import com.example.tpo_da1.ui.domain.DealDetailsResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder

class DealsViewModel : ViewModel() {

    private val repository = DealsRepository(DealsService(RetrofitHelper.getRetrofit().create(CheapSharkApi::class.java)))

    private val _deals = MutableLiveData<List<Deal>>()
    val deals: LiveData<List<Deal>> get() = _deals

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var searchQuery: String? = null

    var currentPage = 0
    private var isLastPage = false
    private val allDeals = mutableListOf<Deal>()
    private val pageSize = 60

    init {
        fetchDeals(currentPage)
    }

    fun fetchDeals(page: Int) {
        if (_loading.value == true || isLastPage) return

        _loading.value = true
        viewModelScope.launch {
            try {
                val newDeals = repository.getDeals(page, pageSize)
                if (newDeals.size < pageSize) {
                    isLastPage = true
                }
                allDeals.addAll(newDeals)
                _deals.value = groupDealsByTitle(allDeals)
            } catch (e: Exception) {
                // Manejar errores
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMoreDeals() {
        if (searchQuery.isNullOrEmpty()) {
            fetchDeals(++currentPage)
        } else {
            searchDeals(searchQuery!!, ++currentPage)
        }
    }

    fun searchDeals(query: String, page: Int = 0) {
        if (_loading.value == true) return

        _loading.value = true
        if (page == 0) {
            searchQuery = query
            allDeals.clear()
        }

        viewModelScope.launch {
            try {
                val searchResults = repository.searchDeals(query, page)
                if (searchResults.size < pageSize) {
                    isLastPage = true
                }
                allDeals.addAll(searchResults)
                _deals.value = groupDealsByTitle(allDeals)
            } catch (e: Exception) {
                // Manejar errores
            } finally {
                _loading.value = false
            }
        }
    }

    private fun groupDealsByTitle(deals: List<Deal>): List<Deal> {
        val groupedDeals = deals.groupBy { it.title }
        return groupedDeals.map { entry ->
            val combinedDeal = entry.value.first().copy(
                normalPrice = entry.value.maxOf { it.normalPrice },
                salePrice = entry.value.minOf { it.salePrice }
            )
            combinedDeal
        }
    }
}
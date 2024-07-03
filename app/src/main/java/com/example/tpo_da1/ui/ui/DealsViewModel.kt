package com.example.tpo_da1.ui.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.data.Deal.DealsRepository
import com.example.tpo_da1.ui.data.Deal.DealsService
import com.example.tpo_da1.ui.data.RetrofitHelper
import com.example.tpo_da1.ui.domain.Deal
import kotlinx.coroutines.launch

class DealsViewModel : ViewModel() {

    private val repository = DealsRepository(DealsService(RetrofitHelper.getRetrofit().create(CheapSharkApi::class.java)))

    private val _deals = MutableLiveData<List<Deal>>()
    val deals: LiveData<List<Deal>> get() = _deals

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var searchQuery: String? = null
    private var order: Int = 0
    private var ordenar: String = "price"
    private var lowerPrice: Int = 0
    private var upperPrice: Int = 50
    private var selectedStoreID: Int = 0

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
            searchDeals(searchQuery!!, ++currentPage,order,ordenar,lowerPrice,upperPrice,selectedStoreID)
        }
    }

    fun searchDeals(query: String, page: Int, desc: Int, sortBy: String, lowerPrice: Int, upperPrice: Int, storeID: Int) {
        Log.d("DealsViewModel", "Searching deals with query: $query, page: $page, order: $desc, sort: $sortBy, lowerPrice: $lowerPrice, upperPrice: $upperPrice")
        if (_loading.value == true) return

        _loading.value = true
        if (page == 0) {
            searchQuery = query
            order = desc
            ordenar = sortBy
            this.lowerPrice = lowerPrice
            this.upperPrice = upperPrice
            selectedStoreID = storeID
            allDeals.clear()
        }

        viewModelScope.launch {
            try {
                val searchResults = repository.searchDeals(query, page, desc, sortBy, lowerPrice, upperPrice, storeID)
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
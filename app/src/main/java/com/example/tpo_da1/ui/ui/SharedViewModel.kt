package com.example.tpo_da1.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tpo_da1.ui.domain.FilterSettings

class SharedViewModel : ViewModel() {
    private val _order = MutableLiveData<Int>()
    val order: LiveData<Int> get() = _order

    private val _sortBy = MutableLiveData<String>()
    val sortBy: LiveData<String> get() = _sortBy

    private val _lowerPrice = MutableLiveData<Int>()
    val lowerPrice: LiveData<Int> get() = _lowerPrice

    private val _upperPrice = MutableLiveData<Int>()
    val upperPrice: LiveData<Int> get() = _upperPrice

    private val _storeID = MutableLiveData<Int>()
    val storeID: LiveData<Int> get() = _storeID

    private val _filterSettings = MutableLiveData<FilterSettings>()
    val filterSettings: LiveData<FilterSettings> get() = _filterSettings

    fun setOrder(order: Int) {
        _order.value = order
        updateFilterSettings()
    }

    fun setSortBy(sortBy: String) {
        _sortBy.value = sortBy
        updateFilterSettings()
    }

    fun setLowerPrice(lowerPrice: Int) {
        _lowerPrice.value = lowerPrice
        updateFilterSettings()
    }

    fun setUpperPrice(upperPrice: Int) {
        _upperPrice.value = upperPrice
        updateFilterSettings()
    }

    fun setSelectedStoreID(storeID: Int) {
        _storeID.value = storeID
        updateFilterSettings()
    }

    private fun updateFilterSettings() {
        _filterSettings.value = FilterSettings(
            order = _order.value ?: 0,
            sortBy = _sortBy.value ?: "price",
            lowerPrice = _lowerPrice.value ?: 0,
            upperPrice = _upperPrice.value ?: 50,
            storeID = _storeID.value ?: 0,
        )
    }
}
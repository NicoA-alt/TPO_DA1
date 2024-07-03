package com.example.tpo_da1.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _order = MutableLiveData<Int>()
    val order: LiveData<Int> get() = _order

    private val _sortBy = MutableLiveData<String>()
    val sortBy: LiveData<String> get() = _sortBy

    private val _lowerPrice = MutableLiveData<Int>()
    val lowerPrice: LiveData<Int> get() = _lowerPrice

    private val _upperPrice = MutableLiveData<Int>()
    val upperPrice: LiveData<Int> get() = _upperPrice

    fun setOrder(order: Int) {
        _order.value = order
    }
    fun setSortBy(sortBy: String) {
        _sortBy.value = sortBy
    }
    fun setLowerPrice(lowerPrice: Int) {
        _lowerPrice.value = lowerPrice
    }

    fun setUpperPrice(upperPrice: Int) {
        _upperPrice.value = upperPrice
    }
}
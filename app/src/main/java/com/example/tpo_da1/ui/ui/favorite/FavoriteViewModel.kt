package com.example.tpo_da1.ui.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_da1.ui.data.FavoriteRepository
import com.example.tpo_da1.ui.domain.Deal
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
    private val repository = FavoriteRepository()

    private val _favoriteDeals = MutableLiveData<List<Deal>>()
    val favoriteDeals: LiveData<List<Deal>> get() = _favoriteDeals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        _isLoading.value = true
        viewModelScope.launch {
            val deals = repository.getFavorites()
            _favoriteDeals.value = deals
            _isLoading.value = false
        }
    }
}
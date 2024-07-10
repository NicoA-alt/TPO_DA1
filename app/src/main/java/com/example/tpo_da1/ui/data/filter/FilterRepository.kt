package com.example.tpo_da1.ui.data.filter

import android.content.Context
import com.example.tpo_da1.ui.domain.FilterSettings
import com.example.tpo_da1.ui.domain.Store
import com.example.tpo_da1.ui.domain.StoreImages

class FilterRepository(private val service: FilterService, private val context: Context) {

    suspend fun getStores(): List<Store> {
        return service.getStores().filter { it.isActive == 1 }.toMutableList().apply {
            add(0, Store("0", "Todas las tiendas", 1, StoreImages("", "", "")))
        }
    }

    fun loadFilters(): FilterSettings {
        val sharedPreferences = context.getSharedPreferences("FILTERS", Context.MODE_PRIVATE)
        return FilterSettings(
            order = sharedPreferences.getInt("ORDER", 0),
            sortBy = sharedPreferences.getString("SORT_BY", "price") ?: "price",
            lowerPrice = sharedPreferences.getInt("LOWER_PRICE", 0),
            upperPrice = sharedPreferences.getInt("UPPER_PRICE", 50),
            storeID = sharedPreferences.getInt("STORE_ID", 0)
        )
    }

    fun saveFilters(filterSettings: FilterSettings) {
        val sharedPreferences = context.getSharedPreferences("FILTERS", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("ORDER", filterSettings.order)
            putString("SORT_BY", filterSettings.sortBy)
            putInt("LOWER_PRICE", filterSettings.lowerPrice)
            putInt("UPPER_PRICE", filterSettings.upperPrice)
            putInt("STORE_ID", filterSettings.storeID)
            apply()
        }
    }
}
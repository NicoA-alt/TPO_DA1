package com.example.tpo_da1.ui.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_da1.databinding.FragmentFilterBinding
import com.example.tpo_da1.ui.data.RetrofitHelper
import com.example.tpo_da1.ui.domain.Store
import com.example.tpo_da1.ui.domain.StoreImages
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPriceRangeSlider()
        loadStores()

        binding.applyFiltersButton.setOnClickListener {
            applyFilters()
        }

        binding.resetFiltersButton.setOnClickListener {
            resetFilters()
        }
        binding.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupPriceRangeSlider() {
        binding.priceRangeSlider.setLabelFormatter { value: Float ->
            if (value >= 50) "50+" else value.toInt().toString()
        }

        binding.priceRangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val minValue = values[0].toInt()
            val maxValue = if (values[1] >= 50) "50+" else values[1].toInt().toString()
            binding.priceRangeText.text = "$minValue - $maxValue"
        }
    }

    private fun loadStores() {
        val api = RetrofitHelper.getCheapSharkApi()

        api.getStores().enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                if (response.isSuccessful) {
                    val stores = response.body()?.filter { it.isActive == 1 }?.toMutableList()
                    stores?.add(0, Store("0", "Todas las tiendas", 1, StoreImages("", "", "")))

                    val adapter = StoreSpinnerAdapter(
                        requireContext(),
                        stores ?: emptyList()
                    )
                    binding.spinnerStore.adapter = adapter
                    loadFilters()
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                // Manejar errores
            }
        })
    }

    private fun applyFilters() {
        val order = if (binding.orderSpinner.selectedItemPosition == 0) 0 else 1 // 0: ascendente, 1: descendente
        val sortBy = when (binding.sortSpinner.selectedItemPosition) {
            0 -> "price"
            1 -> "savings"
            else -> "price"
        }
        val lowerPrice = binding.priceRangeSlider.values[0].toInt()
        val upperPrice = binding.priceRangeSlider.values[1].toInt()
        val selectedStoreID = binding.spinnerStore.selectedItemId.toInt()
        Log.d("FilterFragment", "Applying filters with order: $order, sortBy: $sortBy, lowerPrice: $lowerPrice, upperPrice: $upperPrice, storeID: $selectedStoreID")
        sharedViewModel.setOrder(order)
        sharedViewModel.setSortBy(sortBy)
        sharedViewModel.setLowerPrice(lowerPrice)
        sharedViewModel.setUpperPrice(upperPrice)
        sharedViewModel.setSelectedStoreID(selectedStoreID)

        saveFilters(order, sortBy, lowerPrice, upperPrice, selectedStoreID)
        parentFragmentManager.popBackStack()
    }

    private fun saveFilters(order: Int, sortBy: String, lowerPrice: Int, upperPrice: Int, storeID: Int) {
        val sharedPreferences = requireActivity().getSharedPreferences("FILTERS", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("ORDER", order)
            putString("SORT_BY", sortBy)
            putInt("LOWER_PRICE", lowerPrice)
            putInt("UPPER_PRICE", upperPrice)
            putInt("STORE_ID", storeID)
            apply()
        }
    }

    private fun loadFilters() {
        val sharedPreferences = requireActivity().getSharedPreferences("FILTERS", Context.MODE_PRIVATE)
        val order = sharedPreferences.getInt("ORDER", 0)
        val sortBy = sharedPreferences.getString("SORT_BY", "price") ?: "price"
        val lowerPrice = sharedPreferences.getInt("LOWER_PRICE", 0)
        val upperPrice = sharedPreferences.getInt("UPPER_PRICE", 50)
        val storeID = sharedPreferences.getInt("STORE_ID", 0)

        Log.d("FilterFragment", "Loading filters with order: $order, sortBy: $sortBy, lowerPrice: $lowerPrice, upperPrice: $upperPrice")

        binding.orderSpinner.setSelection(order)
        binding.sortSpinner.setSelection(
            when (sortBy) {
                "price" -> 0
                "savings" -> 1
                else -> 0
            }
        )
        binding.priceRangeSlider.values = listOf(lowerPrice.toFloat(), upperPrice.toFloat())
        binding.spinnerStore.setSelection(storeID)
    }

    private fun resetFilters() {
        binding.spinnerStore.setSelection(0)
        binding.priceRangeSlider.values = listOf(0f, 50f)
        binding.sortSpinner.setSelection(0)
        binding.orderSpinner.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
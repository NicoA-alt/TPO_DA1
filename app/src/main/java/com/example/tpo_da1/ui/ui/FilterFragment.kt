package com.example.tpo_da1.ui.ui

import FilterService
import FilterViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_da1.databinding.FragmentFilterBinding
import com.example.tpo_da1.ui.data.FilterRepository
import com.example.tpo_da1.ui.data.RetrofitHelper
import com.example.tpo_da1.ui.domain.FilterSettings
import com.example.tpo_da1.ui.domain.Store
import com.example.tpo_da1.ui.domain.StoreImages
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FilterViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        val service = FilterService(RetrofitHelper.getCheapSharkApi())
        val repository = FilterRepository(service, requireContext())
        viewModel = FilterViewModel(repository)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPriceRangeSlider()

        viewModel.stores.observe(viewLifecycleOwner) { stores ->
            val adapter = StoreSpinnerAdapter(requireContext(), stores)
            binding.spinnerStore.adapter = adapter
            viewModel.loadFilters()
        }

        viewModel.filters.observe(viewLifecycleOwner) { filters ->
            binding.orderSpinner.setSelection(filters.order)
            binding.sortSpinner.setSelection(
                when (filters.sortBy) {
                    "price" -> 0
                    "savings" -> 1
                    else -> 0
                }
            )
            binding.priceRangeSlider.values = listOf(filters.lowerPrice.toFloat(), filters.upperPrice.toFloat())
            binding.spinnerStore.setSelection(filters.storeID)
        }

        viewModel.loadStores()

        binding.applyFiltersButton.setOnClickListener { applyFilters() }
        binding.resetFiltersButton.setOnClickListener { resetFilters() }
        binding.cancelButton.setOnClickListener { parentFragmentManager.popBackStack() }
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

    private fun applyFilters() {
        val order = if (binding.orderSpinner.selectedItemPosition == 0) 0 else 1
        val sortBy = when (binding.sortSpinner.selectedItemPosition) {
            0 -> "price"
            1 -> "savings"
            else -> "price"
        }
        val lowerPrice = binding.priceRangeSlider.values[0].toInt()
        val upperPrice = binding.priceRangeSlider.values[1].toInt()
        val selectedStoreID = binding.spinnerStore.selectedItemId.toInt()

        sharedViewModel.setOrder(order)
        sharedViewModel.setSortBy(sortBy)
        sharedViewModel.setLowerPrice(lowerPrice)
        sharedViewModel.setUpperPrice(upperPrice)
        sharedViewModel.setSelectedStoreID(selectedStoreID)

        Log.d("FilterFragment", "Applying filters with order: $order, sortBy: $sortBy, lowerPrice: $lowerPrice, upperPrice: $upperPrice, storeID: $selectedStoreID")

        viewModel.saveFilters(FilterSettings(order, sortBy, lowerPrice, upperPrice, selectedStoreID))
        parentFragmentManager.popBackStack()
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
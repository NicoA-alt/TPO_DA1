package com.example.tpo_da1.ui.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.example.tpo_da1.R
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
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
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                // Manejar errores
            }
        })
    }

    private fun applyFilters() {
        // Aquí deberías obtener los valores de los filtros y aplicar la lógica para filtrar las ofertas
    }

    private fun resetFilters() {
        binding.spinnerStore.setSelection(0)
        binding.priceRangeSlider.values = listOf(0f, 50f)
        binding.sortSpinner.setSelection(0)
        binding.orderSpinner.setSelection(0)
        binding.checkboxOnSale.isChecked = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
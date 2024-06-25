package com.example.tpo_da1.ui.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var dealDetailsViewModel: DealDetailsViewModel
    private lateinit var dealID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dealDetailsViewModel = ViewModelProvider(this).get(DealDetailsViewModel::class.java)

        arguments?.let {
            dealID = it.getString("dealID").orEmpty()
        }

        dealDetailsViewModel.fetchDealDetails(dealID)
        observeViewModel()
    }

    private fun observeViewModel() {
        dealDetailsViewModel.dealDetails.observe(viewLifecycleOwner) { deal ->
            deal?.let {
                binding.title.text = it.name ?: "N/A"
                binding.steamRatingText.text = "Calificación en Steam: ${it.steamRatingText ?: "N/A"} (${it.steamRatingPercent ?: "N/A"}%)"
                binding.metacriticRatingText.text = "Calificación en Metacritic: ${it.metacriticScore ?: "N/A"}"

                binding.salePriceText.text = "Precio de venta: "
                binding.salePrice.text = "$${it.salePrice ?: "N/A"}"

                binding.normalPriceText.text = "Precio original: "
                binding.retailPrice.text = "$${it.retailPrice ?: "N/A"}"

                binding.lowestPriceText.text = "Precio más bajo: "
                binding.lowestPrice.text = "$${it.cheapestPrice?.price ?: "N/A"}"

                Glide.with(binding.root.context)
                    .load(it.thumb)
                    .into(binding.dealImage)
            } ?: run {
                binding.title.text = "N/A"
                binding.steamRatingText.text = "Calificación en Steam: N/A (N/A%)"
                binding.metacriticRatingText.text = "Calificación en Metacritic: N/A"

                binding.salePriceText.text = "Precio de venta: "
                binding.salePrice.text = "N/A"

                binding.normalPriceText.text = "Precio original: "
                binding.retailPrice.text = "N/A"

                binding.lowestPriceText.text = "Precio más bajo: "
                binding.lowestPrice.text = "N/A"

                Glide.with(binding.root.context)
                    .load(R.drawable.ic_launcher_background) // PlaceHolder
                    .into(binding.dealImage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
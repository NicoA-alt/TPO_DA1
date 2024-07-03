package com.example.tpo_da1.ui.ui.dealDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.FragmentDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var dealDetailsViewModel: DealDetailsViewModel
    private lateinit var dealID: String
    private val db = FirebaseFirestore.getInstance()
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dealDetailsViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[DealDetailsViewModel::class.java]

        arguments?.let {
            dealID = it.getString("dealID").orEmpty()
        }
        dealDetailsViewModel.fetchDealDetails(dealID)
        observeViewModel()
        checkIfFavorite()
        setButtonListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        dealDetailsViewModel.dealDetails.observe(viewLifecycleOwner) { deal ->
            deal?.let {
                Log.d("DetailFragment", "Deal details observed: $deal")
                binding.title.text = it.name ?: "N/A"
                binding.steamRatingLabel.text = "Calificación en Steam: "
                binding.steamRatingText.text = "${it.steamRatingText ?: "N/A"} (${it.steamRatingPercent ?: "N/A"}%)"
                binding.metacriticRatingLabel.text = "Calificación en Metacritic: "
                binding.metacriticRatingText.text = it.metacriticScore ?: "N/A"

                binding.salePriceText.text = "Precio de venta: "
                binding.salePrice.text = "$${it.salePrice ?: "N/A"}"

                binding.normalPriceText.text = "Precio original: "
                binding.retailPrice.text = "$${it.retailPrice ?: "N/A"}"

                Glide.with(binding.root.context)
                    .load(it.thumb)
                    .into(binding.dealImage)
            } ?: run {
                Log.d("DetailFragment", "Deal details are null")
                binding.title.text = "N/A"
                binding.steamRatingLabel.text = "Calificación en Steam: "
                binding.steamRatingText.text = "N/A (N/A%)"
                binding.metacriticRatingLabel.text = "Calificación en Metacritic: "
                binding.metacriticRatingText.text = "N/A"

                binding.salePriceText.text = "Precio de venta: "
                binding.salePrice.text = "N/A"

                binding.normalPriceText.text = "Precio original: "
                binding.retailPrice.text = "N/A"

                Glide.with(binding.root.context)
                    .load(R.drawable.ic_launcher_background) // Placeholder
                    .into(binding.dealImage)
            }
        }

        dealDetailsViewModel.cheapestPrice.observe(viewLifecycleOwner) { cheapestPrice ->
            binding.lowestPriceText.text = "Precio más bajo: "
            binding.lowestPrice.text = "$${cheapestPrice?.price ?: "N/A"}"
        }
    }

    private fun checkIfFavorite() {
        db.collection("favorites").document(dealID)
            .get()
            .addOnSuccessListener { document ->
                isFavorite = document.exists()
                updateFavoriteButton()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setButtonListeners() {
        binding.buyButton.setOnClickListener {
            val buyUrl = "https://www.cheapshark.com/redirect?dealID=$dealID"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(buyUrl)
            }
            startActivity(intent)
        }

        binding.infoButton.setOnClickListener {
            val gameName = binding.title.text.toString().replace(" ", "_")
            val infoUrl = "https://www.pcgamingwiki.com/wiki/$gameName"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(infoUrl)
            }
            startActivity(intent)
        }

        binding.favoriteButton.setOnClickListener {
            toggleFavoriteStatus()
        }
    }

    private fun toggleFavoriteStatus() {
        if (isFavorite) {
            removeFavorite()
        } else {
            saveFavorite()
        }
        isFavorite = !isFavorite
        updateFavoriteButton()
    }

    private fun saveFavorite() {
        val deal = dealDetailsViewModel.dealDetails.value
        deal?.let {
            val dealMap = hashMapOf(
                "title" to it.name,
                "normalPrice" to it.retailPrice,
                "salePrice" to it.salePrice,
                "thumb" to it.thumb,
                "dealID" to dealID
            )

            db.collection("favorites").document(dealID)
                .set(dealMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun removeFavorite() {
        db.collection("favorites").document(dealID)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateFavoriteButton() {
        if (_binding != null) {
            val iconRes = if (isFavorite) R.drawable.ic_bookmark_active else R.drawable.ic_bookmark
            binding.favoriteButton.setImageResource(iconRes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
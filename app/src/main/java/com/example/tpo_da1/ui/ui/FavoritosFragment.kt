package com.example.tpo_da1.ui.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.FragmentFavoritosBinding
import com.example.tpo_da1.ui.domain.Deal
import com.google.firebase.firestore.FirebaseFirestore


class FavoritosFragment : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteDeals = mutableListOf<Deal>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(favoriteDeals) { dealID ->
            val bundle = Bundle().apply {
                putString("dealID", dealID)
            }
            val detailFragment = DetailFragment().apply {
                arguments = bundle
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, detailFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }
    }

    private fun loadFavorites() {
        binding.progressBar.visibility = View.VISIBLE
        db.collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                favoriteDeals.clear()
                for (document in result) {
                    val deal = document.toObject(Deal::class.java)
                    favoriteDeals.add(deal)
                }
                favoriteAdapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE

                if (favoriteDeals.isEmpty()) {
                    binding.noResultsLayout.visibility = View.VISIBLE
                    binding.favoritesRecyclerView.visibility = View.GONE
                } else {
                    binding.noResultsLayout.visibility = View.GONE
                    binding.favoritesRecyclerView.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { _ ->
                // Error
                binding.progressBar.visibility = View.GONE
                binding.noResultsLayout.visibility = View.VISIBLE
                binding.favoritesRecyclerView.visibility = View.GONE
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
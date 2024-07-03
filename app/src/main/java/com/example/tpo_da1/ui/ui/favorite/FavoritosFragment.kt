package com.example.tpo_da1.ui.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.FragmentFavoritosBinding
import com.example.tpo_da1.ui.ui.dealDetails.DetailFragment


class FavoritosFragment : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

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
        observeViewModel()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(emptyList()) { dealID ->
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

    private fun observeViewModel() {
        favoriteViewModel.favoriteDeals.observe(viewLifecycleOwner) { deals ->
            if (deals.isEmpty()) {
                binding.noResultsLayout.visibility = View.VISIBLE
                binding.favoritesRecyclerView.visibility = View.GONE
            } else {
                binding.noResultsLayout.visibility = View.GONE
                binding.favoritesRecyclerView.visibility = View.VISIBLE
                favoriteAdapter.updateData(deals)
            }
        }

        favoriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.tpo_da1.ui.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dealsViewModel: DealsViewModel
    private lateinit var dealsAdapter: DealsAdapter
    private var isLoading = false
    private val loadMoreHandler = Handler(Looper.getMainLooper())
    private val loadMoreRunnable = Runnable { dealsViewModel.loadMoreDeals() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dealsViewModel = ViewModelProvider(this).get(DealsViewModel::class.java)

        setupRecyclerView()
        setupSearch()
        observeViewModel()

        binding.filterButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, FilterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        dealsAdapter = DealsAdapter(mutableListOf()) { deal ->
            val bundle = Bundle().apply {
                putString("dealID", deal.dealID)
            }
            val detailFragment = DetailFragment().apply {
                arguments = bundle
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, detailFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerProduct.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dealsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (!isLoading && totalItemCount <= (lastVisibleItem + 10)) {
                        loadMoreDealsWithDelay()
                    }
                }
            })
        }
    }

    private fun setupSearch() {
        binding.searchButton.setOnClickListener {
            performSearch()
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    private fun performSearch() {
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            dealsViewModel.currentPage = 0
            dealsViewModel.searchDeals(query)
        }
    }

    private fun loadMoreDealsWithDelay() {
        isLoading = true
        dealsAdapter.showLoader(true)
        loadMoreHandler.postDelayed(loadMoreRunnable, 1500) // Delay de 1.5 segundos
    }

    private fun observeViewModel() {
        dealsViewModel.deals.observe(viewLifecycleOwner) { deals ->
            if (deals.isEmpty()) {
                binding.noResultsLayout.visibility = View.VISIBLE
                binding.recyclerProduct.visibility = View.GONE
            } else {
                binding.noResultsLayout.visibility = View.GONE
                binding.recyclerProduct.visibility = View.VISIBLE
                dealsAdapter.setDeals(deals)
                if (dealsViewModel.currentPage == 0) {
                    binding.recyclerProduct.scrollToPosition(0)
                }
            }
            dealsAdapter.showLoader(false)
            isLoading = false
        }
        dealsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading && dealsViewModel.currentPage == 0) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        loadMoreHandler.removeCallbacks(loadMoreRunnable)
    }
}
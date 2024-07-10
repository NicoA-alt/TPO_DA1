package com.example.tpo_da1.ui.ui.deals

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.FragmentHomeBinding
import com.example.tpo_da1.ui.ui.dealDetails.DetailFragment
import com.example.tpo_da1.ui.ui.filter.FilterFragment
import com.example.tpo_da1.ui.ui.SharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dealsViewModel: DealsViewModel
    private lateinit var dealsAdapter: DealsAdapter
    private var isLoading = false
    private val loadMoreHandler = Handler(Looper.getMainLooper())
    private val loadMoreRunnable = Runnable { dealsViewModel.loadMoreDeals() }
    private lateinit var sharedViewModel: SharedViewModel
    private var currentOrder: Int = 0
    private var currentSortBy: String = "price"
    private var currentLowerPrice: Int = 0
    private var currentUpperPrice: Int = 50
    private var currentStoreID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dealsViewModel = ViewModelProvider(this)[DealsViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.order.observe(viewLifecycleOwner) { order ->
            currentOrder = order
        }
        sharedViewModel.sortBy.observe(viewLifecycleOwner) { sortBy ->
            currentSortBy = sortBy
        }

        sharedViewModel.lowerPrice.observe(viewLifecycleOwner) { lowerPrice ->
            currentLowerPrice = lowerPrice
        }
        sharedViewModel.upperPrice.observe(viewLifecycleOwner) { upperPrice ->
            currentUpperPrice = upperPrice
        }
        sharedViewModel.storeID.observe(viewLifecycleOwner) { storeID ->
            currentStoreID = storeID
        }

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
            dealsViewModel.searchDeals(query, 0, currentOrder, currentSortBy, currentLowerPrice, currentUpperPrice,currentStoreID)
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
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
package com.example.tpo_da1.ui.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        observeViewModel()
    }

    private fun setupRecyclerView() {
        dealsAdapter = DealsAdapter(mutableListOf()) { deal ->
            // Click en oferta
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

    private fun loadMoreDealsWithDelay() {
        isLoading = true
        dealsAdapter.showLoader(true)
        loadMoreHandler.postDelayed(loadMoreRunnable, 1500) // Delay de 1.5 segundos
    }

    private fun observeViewModel() {
        dealsViewModel.deals.observe(viewLifecycleOwner) { deals ->
            dealsAdapter.setDeals(deals)
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
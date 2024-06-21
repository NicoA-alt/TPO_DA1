package com.example.tpo_da1.ui.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpo_da1.databinding.FragmentHomeBinding
import com.example.tpo_da1.ui.domain.Deal

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dealsViewModel: DealsViewModel
    private lateinit var dealsAdapter: DealsAdapter
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
        binding.recyclerProduct.layoutManager = LinearLayoutManager(context)
    }

    private fun observeViewModel() {
        dealsViewModel.deals.observe(viewLifecycleOwner) { deals ->
            val groupedDeals = groupDealsByTitle(deals)
            dealsAdapter = DealsAdapter(groupedDeals) { deal ->
                // Click en oferta
            }
            binding.recyclerProduct.adapter = dealsAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun groupDealsByTitle(deals: List<Deal>): List<Deal> {
        val groupedDeals = deals.groupBy { it.title }
        return groupedDeals.map { entry ->
            val combinedDeal = entry.value.reduce { acc, deal ->
                Deal(
                    title = acc.title,
                    normalPrice = acc.normalPrice,
                    salePrice = acc.salePrice,
                    thumb = acc.thumb,
                    storeName = "${acc.storeName}, ${deal.storeName}"
                )
            }
            combinedDeal
        }
    }
}
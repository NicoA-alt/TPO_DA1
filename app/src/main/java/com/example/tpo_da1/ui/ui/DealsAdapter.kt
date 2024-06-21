package com.example.tpo_da1.ui.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tpo_da1.databinding.ItemDealBinding
import com.example.tpo_da1.ui.domain.Deal

class DealsAdapter(private val deals: List<Deal>, private val clickListener: (Deal) -> Unit) :
    RecyclerView.Adapter<DealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val binding = ItemDealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        holder.bind(deals[position], clickListener)
    }

    override fun getItemCount(): Int = deals.size
}
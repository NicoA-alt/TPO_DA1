package com.example.tpo_da1.ui.ui.deals

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tpo_da1.databinding.ItemDealBinding
import com.example.tpo_da1.ui.domain.Deal
import kotlin.math.roundToInt

class DealViewHolder(private val binding: ItemDealBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(deal: Deal, clickListener: (Deal) -> Unit) {
        binding.title.text = deal.title
        binding.normalPrice.text = "$${deal.normalPrice}"
        binding.salePrice.text = "$${deal.salePrice}"

        Glide.with(binding.root.context)
            .load(deal.thumb)
            .into(binding.thumbnail)

        val normalPrice = deal.normalPrice.toFloat()
        val salePrice = deal.salePrice.toFloat()
        val discountPercentage = ((normalPrice - salePrice) / normalPrice * 100).roundToInt()
        binding.discountPercentage.text = "$discountPercentage%"

        binding.root.setOnClickListener { clickListener(deal) }
    }
}
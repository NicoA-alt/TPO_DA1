package com.example.tpo_da1.ui.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.ItemFavoriteBinding
import com.example.tpo_da1.ui.domain.Deal
import kotlin.math.roundToInt

class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(deal: Deal, clickListener: (String) -> Unit) {
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

        binding.root.setOnClickListener { clickListener(deal.dealID) }
    }
}
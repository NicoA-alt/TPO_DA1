package com.example.tpo_da1.ui.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.ItemFavoriteBinding
import com.example.tpo_da1.ui.domain.Deal

class FavoriteAdapter(private val deals: List<Deal>, private val clickListener: (String) -> Unit) : RecyclerView.Adapter<FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(deals[position], clickListener)
    }

    override fun getItemCount() = deals.size
}

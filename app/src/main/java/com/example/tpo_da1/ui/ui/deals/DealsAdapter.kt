package com.example.tpo_da1.ui.ui.deals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.ItemDealBinding
import com.example.tpo_da1.ui.domain.Deal

class DealsAdapter(private val deals: MutableList<Deal>, private val clickListener: (Deal) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DEAL = 0
        private const val VIEW_TYPE_LOADER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (deals[position].title == "Loader") VIEW_TYPE_LOADER else VIEW_TYPE_DEAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoaderViewHolder(view)
        } else {
            val binding = ItemDealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DealViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DealViewHolder) {
            holder.bind(deals[position], clickListener)
        }
    }

    override fun getItemCount(): Int = deals.size

    fun addDeals(newDeals: List<Deal>) {
        val startPosition = deals.size
        deals.addAll(newDeals)
        notifyItemRangeInserted(startPosition, newDeals.size)
    }

    fun setDeals(newDeals: List<Deal>) {
        deals.clear()
        deals.addAll(newDeals)
        notifyDataSetChanged()
    }

    fun showLoader(show: Boolean) {
        if (show) {
            deals.add(Deal(title = "Loader", normalPrice = "", salePrice = "", thumb = ""))
            notifyItemInserted(deals.size - 1)
        } else {
            if (deals.isNotEmpty() && deals.last().title == "Loader") {
                val position = deals.size - 1
                deals.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    inner class LoaderViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
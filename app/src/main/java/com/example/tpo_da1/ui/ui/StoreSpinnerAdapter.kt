package com.example.tpo_da1.ui.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tpo_da1.R
import com.example.tpo_da1.ui.domain.Store

class StoreSpinnerAdapter(context: Context, stores: List<Store>) :
    ArrayAdapter<Store>(context, 0, stores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_item_store)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_item_store)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup, resource: Int): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val store = getItem(position)

        val imageView = view.findViewById<ImageView>(R.id.store_icon)
        val textView = view.findViewById<TextView>(R.id.store_name)

        store?.let {
            textView.text = it.storeName
            Glide.with(context)
                .load("https://www.cheapshark.com${it.images.icon}")
                .into(imageView)
        }

        return view
    }
}
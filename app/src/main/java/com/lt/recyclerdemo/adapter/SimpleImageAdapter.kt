package com.lt.recyclerdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lt.recyclerdemo.R
import kotlinx.android.synthetic.main.item_simple_image.view.*

class SimpleImageAdapter(private val context: Context) :
    RecyclerView.Adapter<SimpleImageAdapter.SimpleImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SimpleImageViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.item_simple_image, parent, false
        )
    )

    override fun getItemCount(): Int = 100

    override fun onBindViewHolder(holder: SimpleImageViewHolder, position: Int) {
        holder.itemView.tvPos.text = "$position"
    }

    inner class SimpleImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

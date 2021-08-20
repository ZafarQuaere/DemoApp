package com.zaf.econnecto.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zaf.econnecto.R

class CardsImageRecyclerAdapter(private val context: Context, private val mValues: MutableList<String>) :
        RecyclerView.Adapter<CardsImageRecyclerAdapter.ItemViewHolder>() {


   inner class ItemViewHolder internal constructor( itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgItem = itemView.findViewById<ImageView>(R.id.imgItem)
       var mItemName : String? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_cards_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.mItemName = mValues[position]
        Picasso.get().load(mValues[position]).placeholder(R.drawable.default_biz_profile_pic)
                .into(holder.imgItem)
    }

    override fun getItemCount(): Int {
       return mValues.size
    }

}
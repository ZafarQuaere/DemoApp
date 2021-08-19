package com.zaf.econnecto.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zaf.econnecto.R

class CardsImageRecyclerAdapter(private val context: Context, private val mValues: MutableList<String>
) : RecyclerView.Adapter<CardsImageRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_cards_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        Picasso.get().load(mValues[position]).placeholder(R.drawable.default_biz_profile_pic)
                .into(holder.imgItem)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView)

    // final TextView textEstd;
    val imgItem: ImageView = mView.findViewById<View>(R.id.imgItem) as ImageView
    var mItem: String? = null
}
package com.zaf.econnecto.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.GeneralAmenities

class AmenitiesAddEditAdapter() : RecyclerView.Adapter<AmenitiesAddEditAdapter.ViewHolder>() {
    lateinit var mSelectedItem: MutableList<GeneralAmenities>
    lateinit var mContext: Context

    companion object {
        // if checkedPosition = -1, there is no default selection
        // if checkedPosition = 0, 1st item is selected by default
        var checkedPosition: Int = 0
    }


    constructor(mContext: Context, mValues: List<GeneralAmenities>) : this() {
        this.mContext = mContext
        this.mSelectedItem = mValues as MutableList<GeneralAmenities>

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bricks_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = mSelectedItem[position].amenity_name

        if (checkedPosition == -1) {
            holder.imgSelect.visibility = View.GONE
        } else {
            if (checkedPosition == holder.adapterPosition) {
                holder.imgSelect.visibility = View.VISIBLE
            } else {
                holder.imgSelect.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener {
            holder.imgSelect.visibility = View.VISIBLE
            if (checkedPosition != holder.adapterPosition) {
                notifyItemChanged(checkedPosition)
                checkedPosition = holder.adapterPosition
            }
        }

    }

    override fun getItemCount(): Int {
        return mSelectedItem.size
    }


    fun getSelected(): GeneralAmenities? {
        return if (checkedPosition != -1) {
            mSelectedItem[checkedPosition]
        } else null
    }

    fun setListItem(amenityList: MutableList<GeneralAmenities>) {
        this.mSelectedItem = ArrayList<GeneralAmenities>()
        this.mSelectedItem = amenityList
        notifyDataSetChanged()
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val imgSelect: ImageView = itemView.findViewById(R.id.imgSelect)
    }
}
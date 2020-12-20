package com.zaf.econnecto.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.AmenitySelectedData
import com.zaf.econnecto.utils.LogUtils

class AmenitiesAddEditStaggeredAdapter(private val context: Context, private val mValues: List<AmenitySelectedData>) : RecyclerView.Adapter<AmenitiesAddEditStaggeredAdapter.ViewHolder>() {
   var mSelectedItem  = mutableListOf<AmenitySelectedData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bricks_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var selectedData: AmenitySelectedData = mValues[position]
        holder.textName.text = mValues[position].amenity_name
        holder.imgSelect.visibility = if (selectedData.isChecked) View.VISIBLE else View.GONE
        holder.textName.setOnClickListener { v: View? ->
            selectedData.isChecked = !(selectedData.isChecked)
            holder.imgSelect.visibility = if (selectedData.isChecked) View.VISIBLE else View.GONE
            LogUtils.showToast(context, holder.textName.text.toString())
            if (selectedData.isChecked) {
                mSelectedItem.add(mValues[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    fun getSelectedItems(): List<AmenitySelectedData> {
        var selectedItems = mutableListOf<AmenitySelectedData>()
        for (i in 0 until mSelectedItem.size)
            selectedItems.add(i,mSelectedItem[i])
        return selectedItems
    }


    class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textName: TextView = mView.findViewById<View>(R.id.textName) as TextView
        val imgSelect: ImageView = mView.findViewById<View>(R.id.imgSelect) as ImageView

        override fun toString(): String {
            return super.toString() + " '" + textName.text + "'"
        }

    }
}
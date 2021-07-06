package com.zaf.econnecto.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.AmenityData
import com.zaf.econnecto.ui.activities.mybiz.ProductNServiceData

class DealsInStaggeredAdapter(private val context: Context, private val mValues: List<ProductNServiceData>) : RecyclerView.Adapter<DealsInStaggeredAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.amenity_items, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var amenityData: ProductNServiceData = mValues[position]
        holder.textAmenityName.text = amenityData.prod_serv_name
    }

    override fun getItemCount(): Int {
        return mValues.size
    }


    class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textAmenityName: TextView = mView.findViewById<View>(R.id.textAmenityName) as TextView

        override fun toString(): String {
            return super.toString() + " '" + textAmenityName.text + "'"
        }

    }
}
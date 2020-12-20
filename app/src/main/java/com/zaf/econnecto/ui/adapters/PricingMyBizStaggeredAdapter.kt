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
import com.zaf.econnecto.ui.activities.mybiz.PaymentMethodData
import com.zaf.econnecto.ui.activities.mybiz.PricingData

class PricingMyBizStaggeredAdapter(private val context: Context, private val mValues: List<PricingData>) : RecyclerView.Adapter<PricingMyBizStaggeredAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pricing_items, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val amenityData: PricingData = mValues[position]
        holder.textPricingLabel.text = amenityData.prod_serv_name
        holder.textPricingValue.text = amenityData.price
    }

    override fun getItemCount(): Int {
        return mValues.size
    }


    class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textPricingLabel: TextView = mView.findViewById<View>(R.id.textPricingLabel) as TextView
        val textPricingValue: TextView = mView.findViewById<View>(R.id.textPricingValue) as TextView
    }
}
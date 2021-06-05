package com.zaf.econnecto.ui.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.PricingData
import com.zaf.econnecto.ui.interfaces.DeleteCategoryListener

class AddPricingListAdapter(activity: Activity, data: List<PricingData>/*, listener: DeleteCategoryListener*/) : BaseAdapter() {
    var priceList : List<PricingData> = data
    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var rowView : View
//    private val mListener: DeleteCategoryListener = listener

    override fun getCount(): Int {
       return priceList.size
    }

    override fun getItem(position: Int): PricingData {
        return priceList.get(position)
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.pricing_items,parent,false)
        }

        // get current item to be displayed

        val textPricingLabel = rowView.findViewById(R.id.textPricingLabel) as TextView
        val textPricingValue = rowView.findViewById(R.id.textPricingValue) as TextView
        val iconDelete = rowView.findViewById(R.id.iconDelete) as ImageButton
        iconDelete.setOnClickListener {
//            mListener.deletePricingClick(priceList.get(position)!!)
        }

        //sets the text for item name and item description from the current item object

        //sets the text for item name and item description from the current item object
        textPricingLabel.text = priceList.get(position).prod_serv_name
        textPricingValue.text = priceList.get(position).price

        return rowView
    }
}
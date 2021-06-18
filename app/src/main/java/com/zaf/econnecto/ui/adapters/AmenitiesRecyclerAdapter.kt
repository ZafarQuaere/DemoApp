package com.zaf.econnecto.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.AmenityData
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessViewModel
import com.zaf.econnecto.utils.storage.PrefUtil

class AmenitiesRecyclerAdapter(private val context: Activity, private val mValues: List<AmenityData>, amenitiesViewModel: MyBusinessViewModel) : RecyclerView.Adapter<AmenitiesRecyclerAdapter.ViewHolder>() {

    private val mAmenitiesViewModel = amenitiesViewModel
    val mActivity = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_biz_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textAmenityName.text = mValues[position].amenity_name
        holder.iconDelete.setOnClickListener {
            callDeleteApi(mValues[position].amenity_id)
        }
    }

    private fun callDeleteApi(amenityId: String) {
        mAmenitiesViewModel.removeAmenity(mActivity, amenityId, null, PrefUtil.getBizId(mActivity))
    }

    override fun getItemCount(): Int {
        return mValues.size
    }


    inner class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textAmenityName: TextView = mView.findViewById<View>(R.id.textCategoryName) as TextView
        val iconDelete: ImageButton = mView.findViewById<View>(R.id.iconDelete) as ImageButton
    }


}
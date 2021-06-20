package com.zaf.econnecto.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.MbCategoryViewModel
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessViewModel
import com.zaf.econnecto.ui.activities.mybiz.UserCategoryData

class MyBizCategoriesAdapter(private val context: Activity, private val mValues: List<UserCategoryData>, payVm: MyBusinessViewModel) : RecyclerView.Adapter<MyBizCategoriesAdapter.ViewHolder>() {

    private val mPayViewModel = payVm
    val mActivity = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_biz_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textPayTypeName.text = mValues[position].category_name
        holder.iconDelete.setOnClickListener {
            callDeleteApi(mValues[position])
        }
    }

    private fun callDeleteApi(userData: UserCategoryData) {
        mPayViewModel.removeCategory(mActivity, userData.category_id)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }


    inner class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textPayTypeName: TextView = mView.findViewById<View>(R.id.textCategoryName) as TextView
        val iconDelete: ImageButton = mView.findViewById<View>(R.id.iconDelete) as ImageButton
    }


}
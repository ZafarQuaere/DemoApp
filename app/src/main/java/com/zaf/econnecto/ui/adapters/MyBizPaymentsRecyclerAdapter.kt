package com.zaf.econnecto.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.PaymentMethodData
import com.zaf.econnecto.ui.activities.mybiz.PaymentsViewModel
import com.zaf.econnecto.utils.storage.PrefUtil

class MyBizPaymentsRecyclerAdapter(private val context: Activity, private val mValues: List<PaymentMethodData>, payVm: PaymentsViewModel) : RecyclerView.Adapter<MyBizPaymentsRecyclerAdapter.ViewHolder>() {

    private val mPayViewModel = payVm
    val mActivity = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_biz_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textPayTypeName.text = mValues[position].p_method_name
        holder.iconDelete.setOnClickListener {
            callDeleteApi(mValues[position].p_method_id)
        }
    }

    private fun callDeleteApi(payMethodId: String) {
        mPayViewModel.removePayType(mActivity, payMethodId)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }


    inner class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textPayTypeName: TextView = mView.findViewById<View>(R.id.textCategoryName) as TextView
        val iconDelete: ImageButton = mView.findViewById<View>(R.id.iconDelete) as ImageButton
    }


}
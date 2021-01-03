package com.zaf.econnecto.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.ProductNServiceData
import com.zaf.econnecto.ui.interfaces.DeleteProductListener

class BizProdNServiceListAdapter(activity: Activity, data: List<ProductNServiceData>, listener : DeleteProductListener) : RecyclerView.Adapter<BizProdNServiceListAdapter.ViewHolder>() {
    var prodList : List<ProductNServiceData> = data
    private val mListener: DeleteProductListener = listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_biz_category_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productData: ProductNServiceData = prodList[position]
        holder.textCategoryName.text = productData.prod_serv_name
        holder.iconDelete.setOnClickListener {
            mListener.deleteProd(productData)
        }
    }

    override fun getItemCount(): Int {
        return prodList.size
    }


    class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textCategoryName: TextView = mView.findViewById<View>(R.id.textCategoryName) as TextView
        val iconDelete: ImageButton = mView.findViewById<View>(R.id.iconDelete) as ImageButton
    }

}
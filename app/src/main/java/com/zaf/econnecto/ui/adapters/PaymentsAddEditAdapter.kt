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
import com.zaf.econnecto.ui.activities.mybiz.GeneralPaymentMethods

class PaymentsAddEditAdapter(private val context: Context, private val mValues: List<GeneralPaymentMethods>) : RecyclerView.Adapter<PaymentsAddEditAdapter.ViewHolder>() {

    var mSelectedItem = mValues

    companion object {
        // if checkedPosition = -1, there is no default selection
        // if checkedPosition = 0, 1st item is selected by default
        var checkedPosition: Int = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bricks_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = mValues[position].p_method_name

        if (checkedPosition == -1) {
            holder.imgSelect.visibility = View.GONE
        } else {
            if (checkedPosition == holder.adapterPosition) {
                holder.imgSelect.visibility = View.VISIBLE
            } else {
                holder.imgSelect.visibility = View.GONE
            }
        }
        holder.itemView.setOnClickListener { v: View? ->
            holder.imgSelect.visibility = View.VISIBLE
            if (checkedPosition != holder.adapterPosition) {
                notifyItemChanged(checkedPosition)
                checkedPosition = holder.adapterPosition
            }
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    fun getSelectedItems(): GeneralPaymentMethods? {
        return if (checkedPosition != -1) {
            mSelectedItem[checkedPosition]
        } else null
    }


    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textName: TextView = mView.findViewById<View>(R.id.textName) as TextView
        val imgSelect: ImageView = mView.findViewById<View>(R.id.imgSelect) as ImageView
    }
}
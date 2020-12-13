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
import com.zaf.econnecto.ui.activities.mybiz.AmenityDummyData
import com.zaf.econnecto.utils.LogUtils

class AmenitiesStaggeredAdapter(private val context: Context, private val mValues: List<AmenityDummyData>) : RecyclerView.Adapter<AmenitiesStaggeredAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bricks_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var dummyData: AmenityDummyData = mValues[position]
        holder.textName.text = mValues[position].amenity_name
        holder.imgSelect.visibility = if (dummyData.isChecked) View.VISIBLE else View.GONE
        holder.textName.setOnClickListener { v: View? ->
            dummyData.isChecked = !(dummyData.isChecked)
            holder.imgSelect.visibility = if (dummyData.isChecked) View.VISIBLE else View.GONE
//            holder.textName.setBackgroundColor(R.color.colorRed);
            LogUtils.showToast(context, holder.textName.text.toString())
        }

        //holder.textEstd.setText(mValues.get(position).getLastName());

//        Picasso.get().load(mValues.get(position).getImageLink()).placeholder(R.drawable.default_biz_profile_pic).into(holder.imgItem);
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    /* public List<String> getSelectedItems() {

    }*/
    class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textName: TextView

        // final TextView textEstd;
        val imgSelect: ImageView

        override fun toString(): String {
            return super.toString() + " '" + textName.text + "'"
        }

        init {
            textName = mView.findViewById<View>(R.id.textName) as TextView
            //   textEstd = (TextView) view.findViewById(R.id.content);
            imgSelect = mView.findViewById<View>(R.id.imgSelect) as ImageView
        }
    }
}
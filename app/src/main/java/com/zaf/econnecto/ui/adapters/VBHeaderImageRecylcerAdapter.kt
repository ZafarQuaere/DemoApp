package com.zaf.econnecto.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.home.CategoryData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.ui.fragments.BizCategoryFragment

class VBHeaderImageRecylcerAdapter(private val context: Context, private val mValues: MutableList<ViewImageData>) : RecyclerView.Adapter<VBHeaderImageRecylcerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.header_image_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
//        holder.textName.text = mValues[position].categoryName
        //holder.textEstd.setText(mValues.get(position).getLastName());
        Picasso.get().load(mValues[position].imageLink).placeholder(R.drawable.default_biz_profile_pic).into(holder.imgItem)

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textName: TextView

        // final TextView textEstd;
        val imgItem: ImageView
        var mItem: ViewImageData? = null
        override fun toString(): String {
            return super.toString() + " '" + textName.text + "'"
        }

        init {
            textName = mView.findViewById<View>(R.id.textName) as TextView
            //   textEstd = (TextView) view.findViewById(R.id.content);
            imgItem = mView.findViewById<View>(R.id.imgItem) as ImageView
        }
    }

}
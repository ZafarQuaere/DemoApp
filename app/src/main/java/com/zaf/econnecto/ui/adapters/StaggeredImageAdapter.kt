package com.zaf.econnecto.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.ui.interfaces.DeleteImageListener


class StaggeredImageAdapter(private val context: Context, private
val mValues: MutableList<ViewImageData>, deleteVisible: Boolean, iEditImage: DeleteImageListener?)
    : RecyclerView.Adapter<StaggeredImageAdapter.ViewHolder>() {

    private val showDeleteIcon = deleteVisible
    private val editImageListener = iEditImage
    private val mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_photo_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.imgDelete.visibility = if (showDeleteIcon) View.VISIBLE else View.GONE
        Picasso.get().load(mValues[position].imageLink).placeholder(R.drawable.default_biz_profile_pic).into(holder.imgItem)
        holder.imgDelete.setOnClickListener {
            editImageListener?.onDeleteClick(holder.mItem, position)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder internal constructor(private val mView: View) : RecyclerView.ViewHolder(mView) {
        val imgItem: ImageView = mView.findViewById<View>(R.id.imgItem) as ImageView
        val imgDelete: ImageButton = mView.findViewById<View>(R.id.imgDelete) as ImageButton
        var mItem: ViewImageData? = null
    }
}

package com.zaf.econnecto.ui.adapters

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.model.CategoryListData
import com.zaf.econnecto.ui.interfaces.OnBizCategoryItemClickListener
import java.util.*

class BizCategoryRecyclerAdapter(private val context: Context, listener: OnBizCategoryItemClickListener?, items: MutableList<CategoryListData>?) : RecyclerView.Adapter<BizCategoryRecyclerAdapter.ViewHolder>() {
    private var mCategoryList: MutableList<CategoryListData>? = null
    private val tempList: ArrayList<CategoryListData>
    private val mListener: OnBizCategoryItemClickListener?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mCategoryList!![position]
        holder.textName.text = mCategoryList!![position].categoryName
        holder.mView.setOnClickListener { v: View? -> mListener?.onBizCategoryItemClick(holder.mItem!!) }
    }

    override fun getItemCount(): Int {
        return mCategoryList!!.size
    }

    fun filter(query: String?) {
        assert(query != null)
        val charText = query!!.toLowerCase(Locale.getDefault())
        mCategoryList!!.clear()
        if (charText.isEmpty()) {
            mCategoryList!!.addAll(tempList)
        } else {
            for (wp in tempList) {
                if (wp.categoryName.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mCategoryList!!.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textName: TextView = mView.findViewById(R.id.text1)
        var mItem: CategoryListData? = null
        override fun toString(): String {
            return super.toString() + " '" + textName.text + "'"
        }

    }

    init {
        mCategoryList = items
        tempList = ArrayList()
        tempList.addAll(mCategoryList!!)
        mListener = listener
    }
}
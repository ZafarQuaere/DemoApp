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
import com.zaf.econnecto.ui.activities.mybiz.CategoryData
import com.zaf.econnecto.ui.interfaces.DeleteCategoryListener

class BizCategoryListAdapter(activity: Activity, data: List<CategoryData>, listener : DeleteCategoryListener) : BaseAdapter() {
    var catList : List<CategoryData> = data
    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var rowView : View
    private val mListener: DeleteCategoryListener = listener
    override fun getCount(): Int {
       return catList.size
    }

    override fun getItem(position: Int): Any {
        return catList[position]
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.my_biz_category_item,parent,false)
        }

        // get current item to be displayed

        val textCategoryName = rowView.findViewById(R.id.textCategoryName) as TextView
        val iconDelete = rowView.findViewById(R.id.iconDelete) as ImageButton
        iconDelete.setOnClickListener {
            mListener.deleteCategory(catList[position])
        }

        //sets the text for item name and item description from the current item object

        //sets the text for item name and item description from the current item object
        textCategoryName.text = catList[position].category_name

        return rowView
    }
}
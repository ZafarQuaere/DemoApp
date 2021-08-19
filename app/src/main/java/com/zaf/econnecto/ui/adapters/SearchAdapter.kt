package com.zaf.econnecto.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.zaf.econnecto.network_call.response_model.biz_list.BizData

class SearchAdapter(activity: Activity, layoutResource: Int, bizList: MutableList<BizData>) :
        ArrayAdapter<BizData>(activity, layoutResource, bizList), Filterable {
    private var mBizList: List<BizData> = bizList
    private var bizList: List<BizData> = bizList
    private var mLayoutResource = layoutResource
    override fun getCount(): Int {
        return mBizList.size
    }

    override fun getItem(p0: Int): BizData {
        return mBizList.get(p0)
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context)
                .inflate(mLayoutResource, parent, false) as TextView
//        view.text = "${mBizList[position].businessName} ${mBizList[position].businessId} (${mBizList[position].mobile1})"
        view.text = "${mBizList[position].businessName}"
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mBizList = filterResults.values as List<BizData>
                notifyDataSetChanged()
            }
            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()
                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) bizList
                else
                    bizList.filter {
                        it.businessName.toLowerCase().contains(queryString) /*||
                               it.businessId.toLowerCase().contains(queryString) ||
                               it.mobile1.toLowerCase().contains(queryString)*/
                    }
                return filterResults
            }
        }
    }
}
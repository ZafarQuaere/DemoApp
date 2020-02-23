package com.zaf.econnecto.ui.fragments.add_business

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.adapters.AgeGroupRecyclerAdapter
import com.zaf.econnecto.ui.adapters.CategoryDialogRecylcerAdapter
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen1Fragment.OnCategoryItemClickListener
import com.zaf.econnecto.ui.interfaces.DialogButtonClick


class AddBizScreen1ViewModel : ViewModel() {

    /*fun openBottomSheetDialog(activity: Activity?, dialogBtn: DialogButtonClick) {
        val view: View = (activity)!!.layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(view)

        view.findViewById<TextView>(R.id.textOk).setOnClickListener{
            dialogBtn.onOkClick()
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.textCancel).setOnClickListener{
            dialogBtn.onCancelClick()
            dialog.dismiss()
        }
        dialog.show()

    }*/

    fun openBottomSheetDialog(activity: Activity?, categoryArray : Array<String>?, dialogBtn: DialogButtonClick) {
        val view: View = (activity)!!.layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(view)
        implementCategoryList(activity,view,categoryArray)
        view.findViewById<TextView>(R.id.textOk).setOnClickListener{
            dialogBtn.onOkClick()
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.textCancel).setOnClickListener{
            dialogBtn.onCancelClick()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun implementCategoryList(mContext: Context, view:View,categoryArray: Array<String>?) {
        val ageScrollView = view.findViewById<RecyclerView>(R.id.recyclerAge) as RecyclerView
        ageScrollView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        //
        val categoryList = categoryArray!!.asList()
        CategoryDialogRecylcerAdapter(mContext, categoryList, AddBizScreen1Fragment.OnCategoryItemClickListener)
        ageGroupRecyclerAdapter.setListener(this)
        ageScrollView.adapter = ageGroupRecyclerAdapter
    }

}

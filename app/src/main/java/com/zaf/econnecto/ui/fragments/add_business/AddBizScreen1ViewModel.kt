package com.zaf.econnecto.ui.fragments.add_business

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.adapters.CategoryDialogRecylcerAdapter
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen1Fragment.OnCategoryItemClickListener
import com.zaf.econnecto.ui.interfaces.DialogButtonClick


class AddBizScreen1ViewModel : ViewModel() {
    lateinit var categoryRecycler: RecyclerView
    lateinit var categoryList: List<String>
    lateinit var dialog: BottomSheetDialog
    lateinit var mActivity: Activity
    lateinit var categoryItemClick: OnCategoryItemClickListener

    fun openBottomSheetDialog(activity: Activity?, categoryArray: Array<String>?, click: OnCategoryItemClickListener) {
        val view: View = (activity)!!.layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        mActivity = activity
        categoryItemClick = click
        dialog = BottomSheetDialog(mActivity)
        dialog.setContentView(view)
        categoryList = categoryArray!!.asList()
        categoryRecycler = view.findViewById<RecyclerView>(R.id.recyclerCategory)
        categoryRecycler.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        updateCategoryList(categoryList);

        view.findViewById<EditText>(R.id.editSearchCategory).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCategory(s.toString(), categoryList)
            }
        })

        dialog.show()
    }

    private fun updateCategoryList(categoryList: List<String>) {
        val categoryDialogRecylcerAdapter = CategoryDialogRecylcerAdapter(mActivity, categoryList, object : OnCategoryItemClickListener {
            override fun onCategoryItemClick(item: String?) {
                categoryItemClick.onCategoryItemClick(item)
                dialog.dismiss()
            }
        })
        categoryDialogRecylcerAdapter.notifyDataSetChanged()
        categoryRecycler.adapter = categoryDialogRecylcerAdapter
    }

    private fun filterCategory(item: String, categoryList: List<String>) {
        val catList = mutableListOf<String>()
        // val catList = listOfNotNull(String)
        for (i in categoryList.indices) {
            if (categoryList[i].toLowerCase().trim().contains(item.toLowerCase())) {
                catList.add(categoryList.get(i))
            }
        }
        updateCategoryList(catList)
    }
}

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

    fun openBottomSheetDialog(activity: Activity?, categoryArray: Array<String>?, categoryItemClick: OnCategoryItemClickListener) {
        val view: View = (activity)!!.layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(view)
        val categoryList = categoryArray!!.asList()
        val categoryRecycler = view.findViewById<RecyclerView>(R.id.recyclerCategory)
        categoryRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


        val categoryDialogRecylcerAdapter = CategoryDialogRecylcerAdapter(activity, categoryList, object : OnCategoryItemClickListener {
            override fun onCategoryItemClick(item: String?) {
                categoryItemClick.onCategoryItemClick(item)
                dialog.dismiss()
            }
        })
        categoryRecycler.adapter = categoryDialogRecylcerAdapter

        view.findViewById<EditText>(R.id.editSearchCategory).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterCategory(s.toString(),categoryList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        /*view.findViewById<TextView>(R.id.textOk).setOnClickListener {
            dialogBtn.onOkClick()
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.textCancel).setOnClickListener {
            dialogBtn.onCancelClick()
            dialog.dismiss()
        }*/
        dialog.show()
    }

    private fun filterCategory(item: String, categoryList: List<String>) {
        val catList = mutableListOf<String>()
        // val catList = listOfNotNull(String)
        for (i in categoryList.indices) {
            if (item.toLowerCase().trim().contains(categoryList[i])) {
                val add = catList.add(categoryList.get(i))
            }
        }
        //categoryRecycler.notifyDataSetChanged()
    }

  /* for (cat String : categoryList){
          if (bizData.getBusinessName().toLowerCase().trim().contains(bizName.toLowerCase())){
              filterData.add(bizData);
          }
  }*/
}

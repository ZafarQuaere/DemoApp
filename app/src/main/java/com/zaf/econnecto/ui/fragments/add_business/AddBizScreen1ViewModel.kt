package com.zaf.econnecto.ui.fragments.add_business

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.model.CategoryData
import com.zaf.econnecto.model.CategoryListData
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.adapters.CategoryDialogRecylcerAdapter
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen1Fragment.OnCategoryItemClickListener
import com.zaf.econnecto.ui.interfaces.CategoryDataListener
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.parser.ParseManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class AddBizScreen1ViewModel : ViewModel() {
    lateinit var categoryRecycler: RecyclerView
    lateinit var mCategoryList: MutableList<CategoryListData>
    lateinit var dialog: BottomSheetDialog
    lateinit var mActivity: Activity
    lateinit var categoryItemClick: OnCategoryItemClickListener

    fun openBottomSheetDialog(activity: Activity?, categoryList: MutableList<CategoryListData>?, click: OnCategoryItemClickListener) {
        val view: View = (activity)!!.layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        mActivity = activity
        dialog = BottomSheetDialog(mActivity)
        categoryItemClick = click
        mCategoryList = categoryList!!
        categoryRecycler = view.findViewById<RecyclerView>(R.id.recyclerCategory)
        categoryRecycler.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        dialog.setContentView(view)

        updateCategoryList(mCategoryList);

        view.findViewById<EditText>(R.id.editSearchCategory).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCategory(s.toString(), categoryList)
            }
        })

        dialog.show()
    }

    private fun updateCategoryList(categoryList: MutableList<CategoryListData>) {
        val categoryDialogRecylcerAdapter = CategoryDialogRecylcerAdapter(mActivity, categoryList, object : OnCategoryItemClickListener {
            override fun onCategoryItemClick(item: CategoryListData?) {
                categoryItemClick.onCategoryItemClick(item)
                dialog.dismiss()
            }
        })
        categoryDialogRecylcerAdapter.notifyDataSetChanged()
        categoryRecycler.adapter = categoryDialogRecylcerAdapter
    }

    private fun filterCategory(item: String, categoryList: List<CategoryListData>) {
        val catList = mutableListOf<CategoryListData>()
        for (i in categoryList.indices) {
            if (categoryList[i].categoryName.toLowerCase().trim().contains(item.toLowerCase())) {
                catList.add(categoryList[i])
            }
        }
        updateCategoryList(catList)
    }

}


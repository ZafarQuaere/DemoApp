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

    fun callCategoryApi(activity: Activity?, catDataListner: CategoryDataListener) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()

        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.getCategoryList()
        requestCall.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity!!.getString(R.string.ok), mActivity!!.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                var status = body.optInt("status")
                loader.dismiss()

                if (status == AppConstant.SUCCESS) {
                    val body = JSONObject(Gson().toJson(response.body()))
                    val categoryData: CategoryData = ParseManager.getInstance().fromJSON(body, CategoryData::class.java)
                    val categoryList: MutableList<CategoryListData>? = categoryData.data
                    catDataListner.onCategoryListLoaded(categoryList)
                    LogUtils.DEBUG("Response : ${categoryList!!.get(3).categoryName}")
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity!!.getString(R.string.ok), message)
                }
            }

        })
    }
}


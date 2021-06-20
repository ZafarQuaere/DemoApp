package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.MyBizCategoriesAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import org.json.JSONObject
import retrofit2.Response


class CategoriesFragment : Fragment() {

    private lateinit var categoriesVm: MyBusinessViewModel
    private lateinit var mbCategoriesAdapter: MyBizCategoriesAdapter
    lateinit var  mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        categoriesVm = ViewModelProviders.of(requireActivity()).get(MyBusinessViewModel::class.java)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_categories, container, false)
        view.textNoCategories.setOnClickListener {
            startActivityForResult(Intent(activity, CategoriesActivity::class.java), MyBusinessActivityLatest.UPDATE_CATEGORY)
        }
        registerListener()
        return view
    }

    private fun callCategoryApi() {
        (activity as MyBusinessActivityLatest).callBizCategoryListApi()
//        activity?.let { PrefUtil.getBizId(it) }?.let { categoriesVm.bizCategoryList(activity as Activity?, it) }
    }

    private fun registerListener() {
        categoriesVm.mbCategoryList.observe(viewLifecycleOwner, Observer { categories : UserCategories ->
            updateCategoryUI(categories)
        })
        categoriesVm.isCategoryDeleted.observe(viewLifecycleOwner, Observer { isCategoryDeleted: Boolean ->
            if (AppConstant.ADD_EDIT_CATEGORY){
                AppConstant.ADD_EDIT_CATEGORY = false
                updateRemoveCategoryUI(isCategoryDeleted)
            }
        })
    }

    private fun updateRemoveCategoryUI(isCategoryDeleted: Boolean) {
        if (isCategoryDeleted)
            callCategoryApi()
       /* val body = JSONObject(Gson().toJson(jsonObj.body()))
        LogUtils.DEBUG("removeCategory Response:->> $body")
        val status = body.optInt("status")
        if (status == AppConstant.SUCCESS) {
            callCategoryApi()
        } else {
            LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
        }*/
    }

    private fun updateCategoryUI(pricing: UserCategories) {
        if (pricing.status == AppConstant.SUCCESS) {
            textNoCategories.visibility = View.GONE
            myBizCategoryList.visibility = View.VISIBLE
            textAddCategory.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            myBizCategoryList.layoutManager = layoutManager
            myBizCategoryList.itemAnimator = DefaultItemAnimator()
            val payListData = pricing.data
            if (payListData.isNotEmpty()) {
                mbCategoriesAdapter = MyBizCategoriesAdapter(mContext as Activity, payListData, categoriesVm)
                myBizCategoryList.adapter = mbCategoriesAdapter
                textAddCategory.setOnClickListener {
                    startActivityForResult(Intent(activity, CategoriesActivity::class.java), MyBusinessActivityLatest.UPDATE_CATEGORY)
                }
            } else {
                textNoCategories.visibility = View.VISIBLE
                myBizCategoryList.visibility = View.GONE
                textAddCategory.visibility = View.GONE
            }
        } else {
            textNoCategories.visibility = View.VISIBLE
            myBizCategoryList.visibility = View.GONE
            textAddCategory.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_CATEGORY && AppConstant.ADD_EDIT_CATEGORY) {
            LogUtils.DEBUG("Coming from Categories Fragment")
            AppConstant.ADD_EDIT_CATEGORY = false
//            LogUtils.showToast(mContext, "Coming from Category Activity")
            callCategoryApi()
        }
    }
}
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
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.MyBizCategoriesAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import retrofit2.Response


class CategoriesFragment : Fragment() {

    private lateinit var categoriesVm: MbCategoryViewModel
    private lateinit var mbCategoriesAdapter: MyBizCategoriesAdapter
    lateinit var  mContext: Context
    companion object {
        var removeCategory = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        categoriesVm = ViewModelProviders.of(this).get(MbCategoryViewModel::class.java)
        callCategoryApi()
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
        activity?.let { PrefUtil.getBizId(it) }?.let { categoriesVm.bizCategoryList(activity as Activity?, it) }
    }

    private fun registerListener() {
        categoriesVm.mbCategoryList.observe(viewLifecycleOwner, Observer { pricing : UserCategories ->
            updateCategoryUI(pricing)
        })
        categoriesVm.removeCategory.observe(viewLifecycleOwner, Observer { jsonObj: Response<JsonObject> ->
            if (removeCategory){
                removeCategory = false
                updateRemoveCategoryUI()
            }
        })
    }

    private fun updateRemoveCategoryUI() {

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
        if (requestCode == MyBusinessActivityLatest.UPDATE_CATEGORY) {
            LogUtils.DEBUG("Coming from Payment Fragment")
//            LogUtils.showToast(mContext, "Coming from Category Activity")
            callCategoryApi()
        }
    }
}
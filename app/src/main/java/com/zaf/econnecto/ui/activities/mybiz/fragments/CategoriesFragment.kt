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
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.MyBizPricingAdapter
import com.zaf.econnecto.ui.adapters.UserCategoryListAdapter
import com.zaf.econnecto.ui.interfaces.DeleteCategoryListener
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment : Fragment() {

    private lateinit var pricingVm: MbCategoryViewModel
    private lateinit var pricingAdapter: UserCategoryListAdapter
    private var pricingData: UserCategories? = null
    private var removePayTypeData: Boolean = false
    private var addPayTypeData: Boolean = false
    lateinit var  mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        pricingVm = ViewModelProviders.of(this).get(MbCategoryViewModel::class.java)
        if (pricingData == null) {
            callCategoryApi()
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registerListener()
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    private fun callCategoryApi() {
        activity?.let { PrefUtil.getBizId(it) }?.let { pricingVm.bizCategoryList(activity as Activity?, it) }
    }

    private fun registerListener() {
        pricingVm.mbCategoryList.observe(viewLifecycleOwner, Observer { pricing : UserCategories ->
            updateCategoryUI(pricing)
            pricingData = pricing
        })
    }

    private fun updateCategoryUI(pricing: UserCategories) {
        if (pricing.status == AppConstant.SUCCESS) {
            myBizCategoryList.visibility = View.VISIBLE
            textAddCategory.visibility = View.VISIBLE
            val payListData = pricing?.data
            pricingAdapter = UserCategoryListAdapter(mContext as Activity, payListData, object : DeleteCategoryListener{
                override fun deleteCategory(categorydata: UserCategoryData) {
                  LogUtils.showToast(mContext,"Delete clicked")
                }
            })
            myBizCategoryList.adapter = pricingAdapter
            textAddCategory.setOnClickListener {
                startActivityForResult(Intent(activity, CategoriesActivity::class.java), MyBusinessActivityLatest.UPDATE_CATEGORY)
            }
        } else {
            myBizCategoryList.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_CATEGORY) {
            LogUtils.DEBUG("Coming from Payment Fragment")
            LogUtils.showToast(mContext, "Coming from Category Activity")
//            callPayListApi()
            callCategoryApi()
        }
    }
}
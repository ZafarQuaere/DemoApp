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
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.MyBizPricingAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_pricing.*
import kotlinx.android.synthetic.main.fragment_pricing.view.*
import org.json.JSONObject


class PricingFragment : Fragment() {

    private lateinit var pricingVm: MyBusinessViewModel
    private lateinit var pricingAdapter: MyBizPricingAdapter
    private lateinit var mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        pricingVm = ViewModelProviders.of(requireActivity()).get(MyBusinessViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pricing, container, false)
        view.textNoPricing.setOnClickListener {
            startActivityForResult(Intent(activity, PricingActivity::class.java), MyBusinessActivityLatest.UPDATE_PRICING)
        }
        registerListener()
        return view
    }

    private fun callPricingApi() {
        (activity as MyBusinessActivityLatest).callPricingListApi()
    }

    private fun registerListener() {
        pricingVm.mbPricingList.observe(viewLifecycleOwner, Observer { pricing: Pricing ->
            updatePricing(pricing)
        })
        pricingVm.isPricingDeleted.observe(viewLifecycleOwner, Observer { isPricingDeleted: Boolean ->
            if (AppConstant.ADD_EDIT_PRICING) {
                AppConstant.ADD_EDIT_PRICING = false
                updateRemovePricingUI(isPricingDeleted)
            }
        })
    }

    private fun updateRemovePricingUI(isPricingDeleted: Boolean) {
        if (isPricingDeleted) {
            callPricingApi()
        }
    }

    private fun updatePricing(pricing: Pricing) {
        if (pricing.status == AppConstant.SUCCESS) {
            textNoPricing.visibility = View.GONE
            recyclerPricing.visibility = View.VISIBLE
            editPricing.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerPricing.layoutManager = layoutManager
            recyclerPricing.itemAnimator = DefaultItemAnimator()
            val payListData = pricing.data
            if (payListData.isNotEmpty()) {
                pricingAdapter = activity?.let { MyBizPricingAdapter(it, payListData, pricingVm) }!!
                recyclerPricing.adapter = pricingAdapter
                editPricing.setOnClickListener {
                    startActivityForResult(Intent(activity, PricingActivity::class.java), MyBusinessActivityLatest.UPDATE_PRICING)
                }
            } else {
                textNoPricing.visibility = View.VISIBLE
                recyclerPricing.visibility = View.GONE
                editPricing.visibility = View.GONE
            }
        } else {
            textNoPricing.visibility = View.VISIBLE
            recyclerPricing.visibility = View.GONE
            editPricing.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_PRICING && AppConstant.ADD_EDIT_PRICING) {
            LogUtils.DEBUG("Coming from Pricing Fragment")
            AppConstant.ADD_EDIT_PRICING = false
            callPricingApi()
        }
    }
}
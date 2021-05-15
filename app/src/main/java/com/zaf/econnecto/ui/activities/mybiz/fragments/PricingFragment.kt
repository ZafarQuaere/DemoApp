package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.app.Activity
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
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.activities.mybiz.Pricing
import com.zaf.econnecto.ui.activities.mybiz.PricingActivity
import com.zaf.econnecto.ui.activities.mybiz.PricingViewModel
import com.zaf.econnecto.ui.adapters.MyBizPricingAdapter
import com.zaf.econnecto.ui.adapters.PricingMyBizStaggeredAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_pricing.*


class PricingFragment : Fragment() {

    private lateinit var pricingVm: PricingViewModel
    private lateinit var pricingAdapter: MyBizPricingAdapter
    private var pricingData: Pricing? = null
    private var removePayTypeData: Boolean = false
    private var addPayTypeData: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pricingVm = ViewModelProviders.of(this).get(PricingViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       registerListener()
        if (pricingData == null) {
            callPricingApi()
        }
        return inflater.inflate(R.layout.fragment_pricing, container, false)
    }

    private fun callPricingApi() {
        activity?.let { PrefUtil.getBizId(it) }?.let { pricingVm.bizPricingList(activity as Activity?, it) }
    }

    private fun registerListener() {
       pricingVm.mbPricingList.observe(viewLifecycleOwner, Observer { pricing : Pricing ->
           updatePricing(pricing)
           pricingData = pricing
       })
    }

    private fun updatePricing(pricing: Pricing) {
        if (pricing.status == AppConstant.SUCCESS) {
            textNoPricing.visibility = View.GONE
            recyclerPricing.visibility = View.VISIBLE
            editPricing.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerPricing.layoutManager = layoutManager
            recyclerPricing.itemAnimator = DefaultItemAnimator()
            val payListData = pricing?.data
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
    }
}
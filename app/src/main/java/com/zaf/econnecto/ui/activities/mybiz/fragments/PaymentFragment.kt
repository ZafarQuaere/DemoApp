package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.activities.mybiz.PaymentMethods
import com.zaf.econnecto.ui.activities.mybiz.PaymentsOptions
import com.zaf.econnecto.ui.activities.mybiz.PaymentsViewModel
import com.zaf.econnecto.ui.adapters.MyBizPaymentsRecyclerAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_payment.*
import org.json.JSONObject
import retrofit2.Response


class PaymentFragment : Fragment() {

    private lateinit var payVm: PaymentsViewModel
    private lateinit var amenityAdapter: MyBizPaymentsRecyclerAdapter
    private var paymentsData: PaymentMethods? = null
    private var removePayTypeData: Boolean = false
    private var addPayTypeData: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        payVm = ViewModelProviders.of(this).get(PaymentsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        registerListeners()
        if (paymentsData == null) {
            callPayListApi()
        }
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    private fun registerListeners() {
        payVm.mbPayOptionList.observe(viewLifecycleOwner, Observer { data: PaymentMethods ->
            updatePaymentList(data)
            paymentsData = data
        })
        payVm.removePayOption.observe(viewLifecycleOwner, Observer { jsonObj: Response<JsonObject> ->
            removePayTypeData = true
            updateRemovePayTypeUI(jsonObj)
        })
        payVm.addPayOption.observe(viewLifecycleOwner, Observer { jsonObj: Response<JsonObject> ->
            addPayTypeData = true
            updateAddPayType(jsonObj)
        })
    }

    private fun updateAddPayType(jsonObj: Response<JsonObject>) {
        if (addPayTypeData) {
            addPayTypeData = false
            val body = JSONObject(Gson().toJson(jsonObj.body()))
            LogUtils.DEBUG("addPaymentTypeApi Response:->> $body")
            val status = body.optInt("status")
            if (status == AppConstant.SUCCESS) {
                activity?.let { PrefUtil.getBizId(it) }?.let { payVm.bizPaymentMethodList(activity as Activity?, it) }
            } else {
                LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
            }
        }
    }

    private fun updateRemovePayTypeUI(jsonObj: Response<JsonObject>) {
        if (removePayTypeData) {
            removePayTypeData = false
            val body = JSONObject(Gson().toJson(jsonObj.body()))
            LogUtils.DEBUG("removePaymentTypeApi Response:->> $body")
            val status = body.optInt("status")
            if (status == AppConstant.SUCCESS) {
                callPayListApi()

            } else {
                LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
            }
        }
    }

    private fun callPayListApi() {
        activity?.let { PrefUtil.getBizId(it) }?.let { payVm.bizPaymentMethodList(activity as Activity?, it) }
    }

    private fun updatePaymentList(data: PaymentMethods) {
        if (data.status == AppConstant.SUCCESS) {
            textNoPaymentOptions.visibility = View.GONE
            recyclerPayments.visibility = View.VISIBLE
            editPaymentOptions.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerPayments.layoutManager = layoutManager
            recyclerPayments.itemAnimator = DefaultItemAnimator()
            val payListData = data.data
            amenityAdapter = activity?.let { MyBizPaymentsRecyclerAdapter(it, payListData, payVm) }!!
            recyclerPayments.adapter = amenityAdapter
            editPaymentOptions.setOnClickListener {
                startActivityForResult(Intent(activity, PaymentsOptions::class.java), MyBusinessActivityLatest.UPDATE_PAYMENTS)
            }
        } else {
            textNoPaymentOptions.visibility = View.VISIBLE
            recyclerPayments.visibility = View.GONE
            editPaymentOptions.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_PAYMENTS) {
            LogUtils.DEBUG("Coming from Payment Fragment")
            callPayListApi()
        }
    }
}
package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.app.Activity
import android.content.Context
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
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.MyBizPaymentsRecyclerAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.fragment_payment.view.*
import org.json.JSONObject


class PaymentFragment : Fragment() {

    private lateinit var payVm: MyBusinessViewModel
    private lateinit var paymentAdapter: MyBizPaymentsRecyclerAdapter
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        payVm = ViewModelProviders.of(requireActivity()).get(MyBusinessViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)
        view.textNoPaymentOptions.setOnClickListener {
            startActivityForResult(Intent(activity, PaymentsOptions::class.java), MyBusinessActivityLatest.UPDATE_PAYMENTS)
        }
        registerListeners()
        return view
    }

    private fun registerListeners() {
        payVm.mbPayOptionList.observe(viewLifecycleOwner, Observer { data: PaymentMethods ->
            updatePaymentList(data)
        })

        payVm.isPayOptionDeleted.observe(viewLifecycleOwner, Observer { isPayOptionDeleted: Boolean ->
            if (AppConstant.ADD_EDIT_PAYMENTS) {
                AppConstant.ADD_EDIT_PAYMENTS = false
                updateRemovePayTypeUI(isPayOptionDeleted)
            }
        })
    }

    private fun updateRemovePayTypeUI(isPayOptionDeleted: Boolean) {
        if (isPayOptionDeleted) {
            callPayListApi()
        }
    }

    private fun callPayListApi() {
        (activity as MyBusinessActivityLatest).callPayOptionListApi()
//        activity?.let { PrefUtil.getBizId(it) }?.let { payVm.bizPaymentMethodList(activity as Activity?, it) }
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
            if (payListData.isNotEmpty()) {
                paymentAdapter = activity?.let { MyBizPaymentsRecyclerAdapter(it, payListData, payVm) }!!
                recyclerPayments.adapter = paymentAdapter
                editPaymentOptions.setOnClickListener {
                    startActivityForResult(Intent(activity, PaymentsOptions::class.java), MyBusinessActivityLatest.UPDATE_PAYMENTS)
                }
            } else {
                textNoPaymentOptions.visibility = View.VISIBLE
                recyclerPayments.visibility = View.GONE
                editPaymentOptions.visibility = View.GONE
            }
        } else {
            textNoPaymentOptions.visibility = View.VISIBLE
            recyclerPayments.visibility = View.GONE
            editPaymentOptions.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_PAYMENTS && AppConstant.ADD_EDIT_PAYMENTS) {
            LogUtils.DEBUG("Coming from Payment Fragment")
            AppConstant.ADD_EDIT_PAYMENTS = false
            callPayListApi()
        }
    }
}
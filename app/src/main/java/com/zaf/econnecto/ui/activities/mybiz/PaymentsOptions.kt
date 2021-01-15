package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.adapters.PaymentsAddEditAdapter
import com.zaf.econnecto.ui.interfaces.IPaymentOptionList
import com.zaf.econnecto.ui.interfaces.PaymentMethodAddListener
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.layout_payment_methods.*


class PaymentsOptions : AppCompatActivity(), IPaymentOptionList, PaymentMethodAddListener {
    private lateinit var adapterAddEdit: PaymentsAddEditAdapter
    lateinit var recyclerPaymentMethods: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    lateinit var emptyTextView: TextView
    var mContext: Activity = this
    private lateinit var myBizViewModel: MyBusinessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_payment_methods)
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        myBizViewModel.bizPaymentsOptionList(this, this)
        initUI()
    }

    private fun initUI() {
        recyclerPaymentMethods = findViewById<RecyclerView>(R.id.recyclerPaymentMethods)
        recyclerPaymentMethods.setHasFixedSize(true)
        emptyTextView = findViewById(R.id.emptyTextView)

        clickEvents()
    }


    private fun clickEvents() {
        textBack.setOnClickListener {
            onBackPressed()
        }

        textSubmitPM.setOnClickListener {
            val selectedItem = adapterAddEdit.getSelectedItems()
            if (selectedItem != null) {
                myBizViewModel.addPaymentMethodsApi(mContext, this, selectedItem)
            }
            /* val selectedItem : ArrayList<PaymentSelectedData> = adapterAddEdit.getSelectedItems() as ArrayList<PaymentSelectedData>
             val paymentMethodList :MutableList<PaymentMethodData> = ArrayList<PaymentMethodData>();
             for (i in selectedItem.indices){
                 val pmd = PaymentMethodData(selectedItem[i].p_method_id,selectedItem[i].p_method_name)
                 paymentMethodList.add(pmd)
             }*/

        }
    }

    override fun updatePaymentListUI(data: List<GeneralPaymentMethods>?) {
        if (data != null) {
            val layoutManager = LinearLayoutManager(this)
            recyclerPaymentMethods.layoutManager = layoutManager
            recyclerPaymentMethods.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
            adapterAddEdit = PaymentsAddEditAdapter(this, data)
            recyclerPaymentMethods.adapter = adapterAddEdit
        } else {
            LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), getString(R.string.something_wrong_from_server_plz_try_again)) { onBackPressed() }
        }
    }

    override fun updatePaymentMethod() {
        LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), getString(R.string.payment_method_added_successfully)) {
            val returnIntent = Intent()
            returnIntent.putExtra("AddPaymentMethod", getString(R.string.payment_method_added_successfully))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}
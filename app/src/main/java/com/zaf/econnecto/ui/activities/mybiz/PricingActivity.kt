package com.zaf.econnecto.ui.activities.mybiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.adapters.AddPricingListAdapter
import com.zaf.econnecto.ui.interfaces.PricingAddedListener
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.activity_pricing.*

class PricingActivity : AppCompatActivity(), PricingAddedListener/*, DeletePricingListener*/ {
    private lateinit var pricingVm: PricingViewModel
    private lateinit var pricingData: Pricing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pricing)
        pricingVm = ViewModelProviders.of(this).get(PricingViewModel::class.java)
        initUI()
    }

    private fun initUI() {
        val listViewPricing = findViewById<ListView>(R.id.listViewPricing)
        listViewPricing.visibility = View.GONE
        textBack.setOnClickListener {
            onBackPressed()
        }
        textAddMorePricing.setOnClickListener {
            validateInput(editPriceDesc.text.toString(),editPrice.text.toString(),editPriceUnit.text.toString())
        }
        pricingVm.mbPricingList.observe(this, Observer { pricing: Pricing ->
            updatePricingUI(pricing)
        })
        textUpdatePricing.setOnClickListener {
//            finish()
            validateInput(editPriceDesc.text.toString(),editPrice.text.toString(),editPriceUnit.text.toString())
        }
    }

    private fun updatePricingUI(pricing: Pricing) {
        listViewPricing.visibility = View.VISIBLE
        val adapter = AddPricingListAdapter(this, pricing.data/*, null*/)
        listViewPricing.adapter = adapter
        editPriceDesc.setText("")
        editPrice.setText("")
        editPriceUnit.setText("")
        editPriceDesc.requestFocus()
    }

    private fun validateInput(desc: String, price: String, unit: String) {
        when {
            desc.isEmpty() -> {
                LogUtils.showToast(this,"Enter description")
            }
            price.isEmpty() -> {
                LogUtils.showToast(this,"Enter price")
            }
            unit.isEmpty() -> {
                LogUtils.showToast(this,"Enter Unit")
            }
            else -> {
                pricingVm.callAddPricingApi(this,this,desc,price,unit)
            }
        }
    }

    override fun updatePricing() {
        // we had to update the pricing list here in this activity but as in success response of add api we are not
        // getting prod_id as it was required for delete functionality here, so we can't update list for now.
        finish()
//        pricingVm.bizPricingList(this, PrefUtil.getBizId(this) )

    }

//    override fun deletePricingClick(pricingData: PricingData) {
//        LogUtils.showToast(this,"Delete Clicked")
//        //
////        finish()
//    }

}
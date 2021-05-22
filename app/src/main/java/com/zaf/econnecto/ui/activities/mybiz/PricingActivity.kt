package com.zaf.econnecto.ui.activities.mybiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.interfaces.PricingAddedListener
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.activity_pricing.*

class PricingActivity : AppCompatActivity(), PricingAddedListener{
    private lateinit var pricingVm: PricingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pricing)
        pricingVm = ViewModelProviders.of(this).get(PricingViewModel::class.java)
        initUI()
    }

    private fun initUI() {
        textBack.setOnClickListener {
            onBackPressed()
        }
        textAddMorePricing.setOnClickListener {
            validateInput(editPriceDesc.text.toString(),editPrice.text.toString(),editPriceUnit.text.toString())
        }
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
        finish()
    }
}
package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.presenters.operations.IProductNService
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.layout_operating_hour.textSubmit
import kotlinx.android.synthetic.main.layout_product_n_services.*


class ProductAndServices : AppCompatActivity(),IProductNService {

    private var serviceLayoutCount = 0
    private var productLayoutCount = 0
    private lateinit var layoutServices: LinearLayout
    private lateinit var layoutProducts: LinearLayout
    private lateinit var viewModel: MyBusinessViewModel
    private lateinit var mContext: ProductAndServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_product_n_services)
        mContext = this
        viewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        initUI()
    }

    private fun initUI() {
        layoutServices = findViewById<LinearLayout>(R.id.layoutServices)
        layoutProducts = findViewById<LinearLayout>(R.id.layoutProducts)

        textSubmit.setOnClickListener {
            validateInputCallApi()
        }

        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateInputCallApi() {
        var inputValue = ""
        if (editProduct.text.toString().isNotEmpty()){
            inputValue = editProduct.text.toString()
        } else if (editService.text.toString().isNotEmpty()){
            inputValue = editService.text.toString()
        }
        if (inputValue.isEmpty()) {
            LogUtils.showToast(this,this.getString(R.string.please_enter_product_or_service_name))
        } else
        viewModel.addProductServicesApi(this, false, this,inputValue)
    }



    override fun updateProductServices() {
        LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), getString(R.string.details_successfully_added)) {
            val returnIntent = Intent()
            returnIntent.putExtra("ProductNServices", getString(R.string.details_successfully_added))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}

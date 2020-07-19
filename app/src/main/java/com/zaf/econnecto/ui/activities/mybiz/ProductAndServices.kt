package com.zaf.econnecto.ui.activities.mybiz

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.layout_operating_hour.textSubmit
import kotlinx.android.synthetic.main.layout_product_n_services.*


class ProductAndServices : AppCompatActivity() {

    private var serviceLayoutCount = 0
    private var productLayoutCount = 0
    private lateinit var layoutServices: LinearLayout
    private lateinit var layoutProducts: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_product_n_services)
        initUI()
    }

    private fun initUI() {
        layoutServices = findViewById<LinearLayout>(R.id.layoutServices)
        layoutProducts = findViewById<LinearLayout>(R.id.layoutProducts)
        textSubmit.setOnClickListener {
            checkServiceLayout()
//            val returnIntent = Intent()
//            returnIntent.putExtra("result", "data from secondActivity")
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
        }

        clickEvents()
        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun checkServiceLayout() {
        val childCount = layoutServices.childCount
        LogUtils.DEBUG("Child count $childCount")

        for (i in 0 until childCount) {
            val value: TextInputEditText? = layoutServices.getChildAt(i) as? TextInputEditText
            LogUtils.DEBUG("child : $i >> $value")
            LogUtils.DEBUG("child : $i >> ${value!!.text}")
        }
    }

    private fun clickEvents() {
        textAddServices.setOnClickListener {
            if (serviceLayoutCount in 0..3) {
                val layoutCount = layoutServices.childCount
                do {
                    if (layoutCount > 0)
                        addServices()
                } while (layoutCount < 4)

                if (layoutCount > 0) {
                    val count = 0
                    while (layoutCount < 4) {
                        val value = layoutServices.getChildAt(serviceLayoutCount - 1) as TextInputEditText
                        if (value.text.isNullOrEmpty()) {
                            LogUtils.showToast(this, "Enter Service Name")
                            return@setOnClickListener
                        }
                    }
                    addServices()
                } else
                    addServices()
            } else {
                LogUtils.showToast(this, "you can't add more than 4 Services at a time")
            }
        }

        textAddProduct.setOnClickListener {
            if (productLayoutCount < 4)
                addProducts()
            else {
                LogUtils.showToast(this, "you can't add more than 4 Products at a time")
            }
        }
    }

    private fun addServices() {
        val tiEdit = TextInputEditText(this)
        tiEdit.hint = "Enter Service name"
        tiEdit.id = serviceLayoutCount
        layoutServices.addView(tiEdit)
        serviceLayoutCount++
    }

    private fun addProducts() {
        val tiEdit = TextInputEditText(this)
        tiEdit.hint = "Enter Product name"
        tiEdit.id = productLayoutCount
        layoutProducts.addView(tiEdit)
        productLayoutCount++
    }
}

package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.layout_operating_hour.textSubmit
import kotlinx.android.synthetic.main.layout_product_n_services.*
import java.util.*


class ProductAndServices : AppCompatActivity() {

    var serviceLayoutCount = 0
    lateinit var layout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_product_n_services)
        initUI()
    }

    private fun initUI() {
         layout = findViewById<LinearLayout>(R.id.layoutServices)
        textSubmit.setOnClickListener {
            checkServiceLayout()
//            val returnIntent = Intent()
//            returnIntent.putExtra("result", "data from secondActivity")
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
        }

        clickEvents()
        textBack.setOnClickListener{
            onBackPressed()
        }
    }

    private fun checkServiceLayout() {
        val childCount = layout.childCount
        LogUtils.DEBUG("Child count $childCount")
        for(i in 0..childCount){
            val til = layout.getChildAt(i)
            LogUtils.DEBUG("child : $i >> ${layout.getChildAt(i)}")
        }
    }

    private fun clickEvents() {
        textAddServices.setOnClickListener {
            if (serviceLayoutCount < 4)
                addNewLayout()
        }
    }

    private fun addNewLayout() {
//        val til = TextInputLayout(this)
        val tiEdit = TextInputEditText(this)
        tiEdit.hint = "Enter Service name"
//        til.addView(tiEdit)
        tiEdit.id = serviceLayoutCount
        tiEdit.setText( "editname $serviceLayoutCount")
        layout.addView(tiEdit)
        serviceLayoutCount++
    }
}
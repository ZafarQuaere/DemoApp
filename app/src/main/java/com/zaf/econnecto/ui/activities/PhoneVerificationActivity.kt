package com.zaf.econnecto.ui.activities

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils

class PhoneVerificationActivity : AppCompatActivity() {
    lateinit var viewModel: PhoneVerificationViewModel
    lateinit var mobileNo: String
    val mContext: Activity = this
    lateinit var editOTP : TextInputEditText
    lateinit var editPhone : TextInputEditText
    lateinit var txtVerifyPhone : TextView
    lateinit var txtResendOTP : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_verification_fragment)
        mobileNo = intent.getStringExtra("mobile")
        initUI()
        viewModel = ViewModelProviders.of(this).get(PhoneVerificationViewModel::class.java)
        LogUtils.showToast(mContext, mobileNo)
        viewModel.callRequestOTPApi(mContext, mobileNo)

    }

    private fun initUI() {
        editOTP = findViewById(R.id.editOTP)
        editPhone = findViewById(R.id.editPhone)
        txtVerifyPhone = findViewById(R.id.txtVerifyPhone)
        txtResendOTP = findViewById(R.id.txtResendOTP)

        txtVerifyPhone.setOnClickListener {
            viewModel.callVerifyPhoneApi(mContext,editPhone.text.toString().trim(),editOTP.text.toString().trim())
        }
        txtResendOTP.setOnClickListener {
            viewModel.callResendApi(mContext,mobileNo)
            Utils.applyDisableTime(txtResendOTP)
        }
    }

    private fun addActionBar() {
        //setSupportActionBar(findViewById(R.id.toolbar))
    }

}

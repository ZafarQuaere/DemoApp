package com.zaf.econnecto.ui.activities

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.phone_verification_fragment.*

class PhoneVerificationActivity : AppCompatActivity() {
    lateinit var viewModel: PhoneVerificationViewModel
    lateinit var mobileNo: String
    val mContext: Activity = this
    lateinit var editOTP: TextInputEditText
    lateinit var editPhone: TextInputEditText
    lateinit var txtVerifyPhone: TextView
    lateinit var txtResendOTP: TextView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_verification_fragment)
        mobileNo = intent.getStringExtra("mobile").toString()
        initUI()
        viewModel = ViewModelProviders.of(this).get(PhoneVerificationViewModel::class.java)
        // LogUtils.showToast(mContext, mobileNo)
        viewModel.callRequestOTPApi(mContext, mobileNo)
        viewModel.updateTimerUI(mContext!!, txtResendOTP, chronoResendTime, AppConstant.OTP_TIME)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initUI() {
        editOTP = findViewById(R.id.editOTP)
        editPhone = findViewById(R.id.editPhone)
        editPhone.setText(mobileNo)
        txtVerifyPhone = findViewById(R.id.txtVerifyPhone)
        txtResendOTP = findViewById(R.id.txtResendOTP)

        txtVerifyPhone.setOnClickListener {
            viewModel.callVerifyPhoneApi(mContext, editPhone.text.toString().trim(), editOTP.text.toString().trim())
        }
        txtResendOTP.setOnClickListener {
            viewModel.callResendApi(mContext, mobileNo)
            viewModel.updateTimerUI(mContext!!, txtResendOTP, chronoResendTime, AppConstant.RESEND_OTP_TIME)
            Utils.applyDisableTime(txtResendOTP)
        }
    }
}

package com.zaf.econnecto.ui.fragments.user_register

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs

import com.zaf.econnecto.R
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.phone_verification_fragment.*

class PhoneVerificationFragment : Fragment() {

    val args: PhoneVerificationFragmentArgs by navArgs()
    val mContext = activity

    companion object {
        fun newInstance() = PhoneVerificationFragment()
    }

    private lateinit var viewModel: PhoneVerificationFragmentViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.let { viewModel.callRequestOTPApi(it, args.mobileNo) }
        KotUtil.updateActionBar(activity, PhoneVerificationFragment.javaClass.simpleName, activity!!.getString(R.string.phone_verification), null, null)
        val view = inflater.inflate(R.layout.phone_verification_fragment, container, false)
        viewModel.updateTimerUI(activity!!, view, AppConstant.OTP_TIME)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(PhoneVerificationFragmentViewModel::class.java)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        editPhone.setText(args.mobileNo)

        txtVerifyPhone.setOnClickListener {
            if (!editOTP.text.toString().isEmpty()) {
                activity?.let { it -> viewModel.callVerifyPhoneApi(it, editPhone.text.toString(), editOTP.text.toString()) }
            } else {
                LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_otp))
            }
        }

        txtResendOTP.setOnClickListener {
            activity?.let { it1 -> viewModel.callResendApi(it1, editPhone.text.toString()) }
            Utils.applyDisableTime(txtResendOTP)
            txtResendOTP.setTextColor(activity!!.resources.getColor(R.color.colorGrey))
            viewModel.updateTimerUI(activity!!, view, AppConstant.RESEND_OTP_TIME)
        }
    }
}

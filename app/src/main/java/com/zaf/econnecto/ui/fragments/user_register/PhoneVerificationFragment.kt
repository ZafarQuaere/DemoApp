package com.zaf.econnecto.ui.fragments.user_register

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.zaf.econnecto.R
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen2FragmentArgs
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.phone_verification_fragment.*

class PhoneVerificationFragment : Fragment() {

    val args: PhoneVerificationFragmentArgs by navArgs()
    val mContext = activity

    companion object {
        fun newInstance() = PhoneVerificationFragment()
    }

    private lateinit var viewModel: PhoneVerificationFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.let { viewModel.callRequestOTPApi(it, args.mobileNo) }
        KotUtil.updateActionBar(activity, PhoneVerificationFragment.javaClass.simpleName, activity!!.getString(R.string.phone_verification), null, null)
        return inflater.inflate(R.layout.phone_verification_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(PhoneVerificationFragmentViewModel::class.java)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProviders.of(this).get(PhoneVerificationViewModel::class.java)
        editPhone.setText(args.mobileNo)

        //LogUtils.showToast(activity, "Mobile no is : "+args.stringKeyMobileNo)
        txtVerifyPhone.setOnClickListener {
            if (!editOTP.text.toString().isEmpty()) {
                activity?.let { it -> viewModel.callVerifyPhoneApi(it, editPhone.text.toString(), editOTP.text.toString()) }
            } else {
                LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_otp))
            }
        }

        txtResendOTP.setOnClickListener {
            activity?.let { it1 -> viewModel.callResendApi(it1, editPhone.text.toString()) }
        }
    }

}

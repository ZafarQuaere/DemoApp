package com.zaf.econnecto.ui.fragments.add_business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.add_biz_screen3_fragment.*

class AddBizScreen3Fragment : Fragment() {

    private lateinit var navController: NavController
    private val addressInfo: AddBizScreen3FragmentArgs by navArgs()
    lateinit var screen12Data: AddressInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screen12Data = addressInfo.addressInfo
        //LogUtils.showErrorDialog(activity, "Ok", "${addressInfo.addressInfo.category1}, ${addressInfo.addressInfo.estdYear},${addressInfo.addressInfo.address1} ,${addressInfo.addressInfo.pincode}")
    }

    companion object {
        fun newInstance() = AddBizScreen3Fragment()
    }

    private lateinit var viewModel: AddBizScreen3ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_biz_screen3_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KotUtil.updateActionBar(activity, AddBizScreen1Fragment.javaClass.simpleName, activity!!.getString(R.string.add_business), null, null)
        btnSubmitAddBiz.setOnClickListener {
            validateInput()
        }
        navController = Navigation.findNavController(view)
        btnPrevious.setOnClickListener {
            activity!!.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun validateInput() {
        val mobileNo = editMobile.text.toString().trim()
        val emailId = editEmail.text.toString().trim()
        val alternateMobile = editAlternateMobile.text.toString().trim()
        val telephone = editTelephone.text.toString().trim()
        val website = editBizWebsite.text.toString().trim()
        when {
            mobileNo.isEmpty() -> {
                LogUtils.showErrorDialog(activity, activity?.getString(R.string.ok), activity?.getString(R.string.enter_mobile_no))
            }
            emailId.isEmpty() -> {
                LogUtils.showErrorDialog(activity, activity?.getString(R.string.ok), activity?.getString(R.string.enter_email))
            }
            else -> {
                activity!!.finish()
                //Call Api
                viewModel.callAddBizApi(activity, screen12Data, mobileNo, emailId, alternateMobile, telephone, website)
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen3ViewModel::class.java)

    }

}





package com.zaf.econnecto.ui.fragments.add_business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.add_biz.PinCodeResponse
import com.zaf.econnecto.ui.interfaces.PinCodeDataListener
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.*
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.editAddress1
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.editCity
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.editCountry
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.editPinCode
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.editState
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.lytLocalitySpin
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.spinnerLocality
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.textLocalityLabel
import kotlinx.android.synthetic.main.layout_edit_details.*

class AddBizScreen2Fragment : Fragment() {

    lateinit var navController: NavController
    private var address1: String = ""
    private var address2: String = ""
    private var landmark: String = ""
    lateinit var pincode: String
    var locality: String? = null
    lateinit var city: String
    lateinit var state: String
    private var country: String = "India"

    private lateinit var viewModel: AddBizScreen2ViewModel

    private val bizInfo: AddBizScreen2FragmentArgs by navArgs()

    companion object {
        fun newInstance() = AddBizScreen2Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // LogUtils.showErrorDialog(activity, "Ok", bizInfo.bizDetail.category1 + " " + bizInfo.bizDetail.category2+" "+bizInfo.bizDetail.category3)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_biz_screen2_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        KotUtil.updateActionBar(activity, AddBizScreen2Fragment.javaClass.simpleName, requireActivity().getString(R.string.add_business), null, null)
        btnNext.setOnClickListener {
            pincode = editPinCode.text.toString().trim()
            city = editCity.text.toString().trim()
            state = editState.text.toString().trim()
            address1 = editAddress1.text.toString().trim()

            var addressInfoData = AddressInfo(bizInfo.bizDetail.bizName,bizInfo.bizDetail.shortDesc, bizInfo.bizDetail.category1, bizInfo.bizDetail.estdYear, bizInfo.bizDetail.category2, bizInfo.bizDetail.category3,
                    address1, address2, landmark, pincode, locality, city, state, country)
            validateInputsAndNavigate(addressInfoData)

        }

        btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun validateInputsAndNavigate(data: AddressInfo) = when {
        data.address1.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, requireActivity().getString(R.string.ok), requireActivity().getString(R.string.please_enter_address))
        }
        data.pincode.isNullOrEmpty() || data.pincode.length < 6 -> {
            LogUtils.showErrorDialog(activity, requireActivity().getString(R.string.ok), requireActivity().getString(R.string.please_enter_valid_pincode))
        }
        data.locality.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, requireActivity().getString(R.string.ok), requireActivity().getString(R.string.please_enter_locality))
        }
        data.city.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, requireActivity().getString(R.string.ok), requireActivity().getString(R.string.please_enter_city_name))
        }
        data.state.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, requireActivity().getString(R.string.ok), requireActivity().getString(R.string.please_enter_state))
        }
        data.country.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, requireActivity().getString(R.string.ok), requireActivity().getString(R.string.please_enter_country))
        }
        else -> {
            var bundle = bundleOf("addressInfo" to data)
            navController.navigate(R.id.action_screen2_to_screen3, bundle)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen2ViewModel::class.java)
        viewModel.updatePinCodeData(activity, editPinCode, object : PinCodeDataListener {
            override fun onDataFetched(pincodeData: PinCodeResponse?) {
                if (pincodeData != null) {
                    val data = pincodeData.getPostOffice()
                    if (data != null) {
                        spinnerLocality.isEnabled = true
                        textLocalityLabel.visibility = View.VISIBLE
                        lytLocalitySpin.visibility = View.VISIBLE
                        pincode = data[0]?.getPincode().toString()
                        city = data[0]?.getBlock().toString()
                        state = data[0]?.getState().toString()
                        editCity.setText("")
                        editState.setText("")
                        editCountry.setText("")
                        editCity.setText(data[0]?.getBlock())
                        editState.setText(data[0]?.getState())
                        editCountry.setText(getString(R.string.india))
                        editCity.isEnabled = false
                        editState.isEnabled = false
                        editCountry.isEnabled = false
                        val localityArray = arrayOfNulls<String>(data.size)
                        for (i in data.indices) {
                            localityArray[i] = data[i]?.getName().toString()
                        }
                        val localityAdapter = ArrayAdapter<String>(
                            activity,
                            android.R.layout.simple_spinner_item,
                            localityArray
                        )
                        localityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerLocality.adapter = localityAdapter
                    }
                } else {
//                    LogUtils.showToast(activity, getString(R.string.please_enter_valid_pincode))
                    editCity.setText("")
                    editState.setText("")
                    editCountry.setText("")
                    spinnerLocality.isEnabled = false
                }

                spinnerLocality.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            locality = parent?.getItemAtPosition(position).toString()
                            //  LogUtils.showToast(activity, locality )
                        }
                    }
            }
        })
    }
}


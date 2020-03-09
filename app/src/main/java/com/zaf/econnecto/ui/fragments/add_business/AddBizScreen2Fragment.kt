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
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.*

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
       // LogUtils.showErrorDialog(activity, "Ok", bizInfo.bizDetail.bizName + " " + bizInfo.bizDetail.estdYear)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_biz_screen2_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        btnNext.setOnClickListener {
            pincode = editPinCode.text.toString().trim()
            city = editCity.text.toString().trim()
            state = editState.text.toString().trim()
            address1 = editAddress1.text.toString().trim()

            var addressInfoData = AddressInfo(bizInfo.bizDetail.bizName, bizInfo.bizDetail.category1, bizInfo.bizDetail.estdYear, bizInfo.bizDetail.category2, bizInfo.bizDetail.category3,
                    address1, address2, landmark, pincode, locality, city, state, country)
            validateInputsAndNavigate(addressInfoData)

        }

        btnPrevious.setOnClickListener {
            activity!!.onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun validateInputsAndNavigate(data: AddressInfo) = when {
        data.address1.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_address))
        }
        data.pincode.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_valid_pincode))
        }
        data.locality.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_locality))
        }
        data.city.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_city_name))
        }
        data.state.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_state))
        }
        data.country.isNullOrEmpty() -> {
            LogUtils.showErrorDialog(activity, activity!!.getString(R.string.ok), activity!!.getString(R.string.please_enter_country))
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
            override fun onDataFetched(pincodeData: PinCodeResponse) {
                val data = pincodeData.getData()
                if (data != null) {
                    textLocalityLabel.visibility = View.VISIBLE
                    lytLocalitySpin.visibility = View.VISIBLE
                    pincode = pincodeData.getData()!![0]!!.getPincode().toString()
                    city = pincodeData.getData()!![0]!!.getDistrict().toString()
                    state = pincodeData.getData()!![0]!!.getStateName().toString()
                    editCity.setText(pincodeData.getData()!![0]!!.getDistrict())
                    editState.setText(pincodeData.getData()!![0]!!.getStateName())
                    editCountry.setText(getString(R.string.india))
                    val localityArray = arrayOfNulls<String>(data.size)
                    for (i in data.indices) {
                        localityArray[i] = data[i]!!.getOfficeName().toString()
                    }

                    val localityAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, localityArray)
                    localityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerLocality.adapter = localityAdapter
                }

                spinnerLocality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        locality = parent!!.getItemAtPosition(position).toString()
                        //  LogUtils.showToast(activity, locality )
                    }
                }
            }
        })
    }
}


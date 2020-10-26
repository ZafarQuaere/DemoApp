package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.add_biz.PinCodeResponse
import com.zaf.econnecto.ui.activities.BaseActivity
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen2ViewModel
import com.zaf.econnecto.ui.interfaces.PinCodeDataListener
import com.zaf.econnecto.ui.presenters.EditDetailsPresenter
import com.zaf.econnecto.ui.presenters.operations.IEditDetails
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.layout_edit_details.*

class EditDetails : BaseActivity<EditDetailsPresenter?>(), IEditDetails {
    val mContext = this
    lateinit var locality: String
    lateinit var pincode: String
    lateinit var city: String
    lateinit var state: String

    private lateinit var viewModel: AddBizScreen2ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_edit_details)

        initUI();
        getLocalityData()
    }

    private fun getLocalityData() {
        viewModel = ViewModelProviders.of(this).get(AddBizScreen2ViewModel::class.java)
        viewModel.updatePinCodeData(this, editPinCode, object : PinCodeDataListener {
            override fun onDataFetched(pincodeData: PinCodeResponse) {
                val data = pincodeData.getData()
                if (data != null) {
                    textLocalityLabel.visibility = View.VISIBLE
                    lytLocalitySpin.visibility = View.VISIBLE
                    pincode = pincodeData.getData()!![0]!!.getPincode().toString()
                    city = pincodeData.getData()!![0]!!.getDistrict().toString()
                    state = pincodeData.getData()!![0]!!.getStateName().toString()
                    editCity.setText("")
                    editState.setText("")
                    editCountry.setText("")
                    locality = pincodeData.getData()!![0]!!.getTaluk().toString()
                    editCity.setText(pincodeData.getData()!![0]!!.getDistrict())
                    editState.setText(pincodeData.getData()!![0]!!.getStateName())
                    editCountry.setText(getString(R.string.india))
                    editCity.isEnabled = false
                    editState.isEnabled = false
                    editCountry.isEnabled = false
                    val localityArray = arrayOfNulls<String>(data.size)
                    for (i in data.indices) {
                        localityArray[i] = data[i]!!.getOfficeName().toString()
                    }

                    val localityAdapter = ArrayAdapter<String>(this@EditDetails, android.R.layout.simple_spinner_item, localityArray)
                    localityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerLocality.adapter = localityAdapter
                }

                spinnerLocality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        locality = parent!!.getItemAtPosition(position).toString()
//                        LogUtils.showToast(this@EditDetails, locality )
                    }
                }
            }
        })
    }

    private fun initUI() {
        val data = PrefUtil.getBasicDetailsData(this)
        if (data != null) {
            editLocality.visibility = View.VISIBLE
            lytLocalitySpin.visibility = View.GONE
            locality = data.areaLocality
            editBizName.setText(data.businessName)
            editShortDesc.setText(data.shortDescription)
            editEstdYear.setText(data.yearEstablished)
            editAddress1.setText(data.address1)
            editAddress2.setText(data.address2)
            editLandMark.setText(data.landmark)
            editPinCode.setText(data.pinCode)
            editCity.setText(data.cityTown)
            editState.setText(data.state)
            editCountry.setText(data.country)
            editMobile.setText(data.mobile1)
            editAlternateMobile.setText(data.mobile2)
            editPhone.setText(data.telephone)
            editEmail.setText(data.email)
            editWebsite.setText(data.website)
            editLocality.setText(locality)

            editLocality.isEnabled = false
            editCity.isEnabled = false
            editState.isEnabled = false
            editCountry.isEnabled = false
        }

        onClickEvents()
        editPinCode.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    editLocality.visibility = View.GONE
                    lytLocalitySpin.visibility = View.VISIBLE
                } else if (s.toString().length == 6) {
                    getLocalityData()
                }
            }
        })
    }

    private fun onClickEvents() {
        textUpdateBizDetails.setOnClickListener {
            presenter!!.validateBasicInputs(editBizName.text.toString(), editShortDesc.text.toString(), editEstdYear.text.toString())
        }
        textUpdateAddressDetails.setOnClickListener {
            presenter!!.validateAddressInputs(editAddress1.text.toString(), editAddress2.text.toString(), editLandMark.text.toString(), editPinCode.text.toString(), locality, editCity.text.toString(), editState.text.toString(), editCountry.text.toString())
        }

        textUpdateContactDetails.setOnClickListener {
            presenter!!.validateContactInputs(editMobile.text.toString(), editAlternateMobile.text.toString(), editPhone.text.toString(), editEmail.text.toString(), editWebsite.text.toString())
        }
        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initPresenter(): EditDetailsPresenter? {
        return EditDetailsPresenter(this, this)
    }


    override fun updateAddressDetails(msg: String?) {
        LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), msg) {
            updateInfoNReturn("AddressDetail")
        }
    }

    override fun updateContactDetails(msg: String?) {
        LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), msg) {
            updateInfoNReturn("ContactDetail")
        }
    }

    override fun updateBusinessDetails(msg: String?) {
        LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), msg) {
            updateInfoNReturn("BusinessDetail")
        }
    }

    private fun updateInfoNReturn(s: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("update", s)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onValidationError(msg: String?) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg)
    }

}
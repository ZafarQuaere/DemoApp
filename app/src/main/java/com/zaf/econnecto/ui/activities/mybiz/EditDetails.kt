package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.BaseActivity
import com.zaf.econnecto.ui.presenters.EditDetailsPresenter
import com.zaf.econnecto.ui.presenters.operations.IEditDetails
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.activity_edit_details.*

class EditDetails : BaseActivity<EditDetailsPresenter?>(), IEditDetails {
    val mContext = this
    lateinit var locality : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        initUI();
    }

    private fun initUI() {
        textUpdateBizDetails.setOnClickListener {
           presenter!!.validateBasicInputs(editBizName.text.toString(),editShortDesc.text.toString(),editEstdYear.text.toString())
        }
        textUpdateAddressDetails.setOnClickListener {
            presenter!!.validateAddressInputs(editAddress1.text.toString(),editPinCode.text.toString(),locality,editCity.text.toString(),editState.text.toString(),editCountry.text.toString())
        }

        textUpdateContactDetails.setOnClickListener {
            presenter!!.validateContactInputs(editMobile.text.toString(),editAlternateMobile.text.toString(),editPhone.text.toString(),editEmail.text.toString(),editWebsite.text.toString())
        }
        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initPresenter(): EditDetailsPresenter? {
       return EditDetailsPresenter(this,this)
    }


    override fun updateAddressDetails(msg: String?) {

    }

    override fun updateContactDetails(msg: String?) {

    }

    override fun updateBusinessDetails(msg: String?) {

    }

    private fun updateInfoNReturn(){
        val returnIntent = Intent()
        returnIntent.putExtra("result", "data from seconActivity")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onValidationError(msg: String?) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg)
    }

}
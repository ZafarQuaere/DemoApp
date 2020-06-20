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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        initUI();
    }

    private fun initUI() {
        textUpdateBizDetails.setOnClickListener {
           presenter!!.validateBasicInputs(editBizName.text.toString(),editShortDesc.text.toString(),editEstdYear.text.toString())
        }
        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initPresenter(): EditDetailsPresenter? {
       return EditDetailsPresenter(this,this)
    }


    override fun updateAddressDetails(msg: String?) {
        TODO("Not yet implemented")
    }

    override fun updateContactDetails(msg: String?) {
        TODO("Not yet implemented")
    }

    override fun updateBusinessDetails(msg: String?) {
        TODO("Not yet implemented")
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
package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.OtpPresenter;
import com.zaf.econnecto.ui.presenters.operations.IOtp;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class EnterOTPActivity extends BaseActivity<OtpPresenter> implements IOtp {

    private Context mContext;

    @Override
    protected OtpPresenter initPresenter() {
        return new OtpPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mContext = this;
        Utils.updateActionBar(this,new EnterOTPActivity().getClass().getSimpleName(),getString(R.string.enter_otp),
                null,null);
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationField();
            }
        });
    }


    private void validationField() {
        EditText editOTP = (EditText) findViewById(R.id.editOTP);
        getPresenter().validateOtp(editOTP.getText().toString().trim());
    }

    @Override
    public void submitOtp() {
        startActivity(new Intent(EnterOTPActivity.this, EnterNewPswdActivity.class));
        finishAffinity();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }


    public void resendOTP(View view) {
    }

    public void editNumber(View view) {
    }
}

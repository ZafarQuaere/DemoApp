package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.OtpPresenter;
import com.zaf.econnecto.ui.presenters.operations.IOtp;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class ResetPswdActivity extends BaseActivity<OtpPresenter> implements IOtp {

    private Context mContext;
    private String email;

    @Override
    protected OtpPresenter initPresenter() {
        return new OtpPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pswd);
        mContext = this;
        email = getIntent().getStringExtra("email");
        Utils.updateActionBar(this, new ResetPswdActivity().getClass().getSimpleName(), getString(R.string.enter_otp),
                null, null);
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationField();
            }
        });
    }


    private void validationField() {
        TextInputEditText editOTP = (TextInputEditText) findViewById(R.id.editOTP);
        TextInputEditText editPassword = (TextInputEditText) findViewById(R.id.editPassword);
        TextInputEditText editConfirmPassword = (TextInputEditText) findViewById(R.id.editConfirmPassword);
        getPresenter().validateInputs(editOTP.getText().toString().trim(), editPassword.getText().toString().trim(), editConfirmPassword.getText().toString().trim(), email);
    }

    @Override
    public void moveToLogin() {
        startActivity(new Intent(ResetPswdActivity.this, LoginActivity.class));
        finishAffinity();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

}

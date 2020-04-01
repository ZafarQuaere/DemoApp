package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.view.View;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.OtpPresenter;
import com.zaf.econnecto.ui.presenters.operations.IOtp;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class ChangePswdActivity extends BaseActivity<OtpPresenter> implements IOtp {

    private Context mContext;
    private String email;

    @Override
    protected OtpPresenter initPresenter() {
        return new OtpPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pswd);
        mContext = this;
        email = Utils.getUserEmail(mContext);
        Utils.updateActionBar(this, new ChangePswdActivity().getClass().getSimpleName(), getString(R.string.change_pswd),
                null, null);
        findViewById(R.id.btnSubmit).setOnClickListener(v -> validationField());
    }


    private void validationField() {
        TextInputEditText editMobile = (TextInputEditText) findViewById(R.id.editMobile);
        TextInputEditText editOldPassword = (TextInputEditText) findViewById(R.id.editOldPassword);
        TextInputEditText editNewPassword = (TextInputEditText) findViewById(R.id.editNewPassword);
        getPresenter().validateCPInputs(editMobile.getText().toString().trim(), editOldPassword.getText().toString().trim(), editNewPassword.getText().toString().trim(), email);
    }

    @Override
    public void moveToLogin() {
        startActivity(new Intent(ChangePswdActivity.this, LoginActivity.class));
        finishAffinity();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

}

package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.ForgotPswdPresenter;
import com.zaf.econnecto.ui.presenters.operations.IFrgtPswd;
import com.zaf.econnecto.utils.LogUtils;


public class ForgetPswdActivity extends BaseActivity<ForgotPswdPresenter> implements IFrgtPswd {

    private Context mContext;


    @Override
    protected ForgotPswdPresenter initPresenter() {
        return  new ForgotPswdPresenter(this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pswd);
        mContext = this;

        //getPresenter().updateActionBar(mContext);
        findViewById(R.id.btnPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationField();
            }
        });

    }

    private void validationField() {
        EditText editEmail = (EditText)findViewById(R.id.editEmail);
        getPresenter().validMobileNo(editEmail.getText().toString().trim());
    }


    @Override
    public void submitMobile() {
        startActivity(new Intent(ForgetPswdActivity.this,EnterOTPActivity.class));
        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

    @Override
    public void callSubmitMobileApi(String mobile) {
        getPresenter().callSubmitMobileApi(mobile);
    }
}

package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.ForgotPswdPresenter;
import com.zaf.econnecto.ui.presenters.operations.IFrgtPswd;
import com.zaf.econnecto.utils.LogUtils;


public class ForgetPswdActivity extends BaseActivity<ForgotPswdPresenter> implements IFrgtPswd {

    private Context mContext;
    private String email;
    private TextInputLayout tilEmail,tilPhone;
    private Button btnPassword;
    private TextView textForgotEmail;
    private EditText editEmail;

    @Override
    protected ForgotPswdPresenter initPresenter() {
        return  new ForgotPswdPresenter(this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pswd);
        mContext = this;
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        btnPassword = findViewById(R.id.btnPassword);
        textForgotEmail = (TextView) findViewById(R.id.textForgotEmail);

        btnPassword.setOnClickListener(v -> {
            if (btnPassword.getText().equals(getString(R.string.send_otp))){
                validationField(true);
            } else {
                validationField(false);
            }
        });

    }

    private void validationField(boolean isEmail) {
        editEmail = (EditText)findViewById(R.id.editEmail);
        EditText editPhone = (EditText)findViewById(R.id.editPhone);
        email = editEmail.getText().toString().trim();
        String phone  = editPhone.getText().toString().trim();
        getPresenter().validateInput(isEmail,email,phone);
    }


    @Override
    public void startOTPActivity() {
        startActivity(new Intent(ForgetPswdActivity.this, ResetPswdActivity.class).putExtra("email",email));
        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

    @Override
    public void callOtpApi(String mobile) {
        getPresenter().callOtpApi(mobile);
    }

    @Override
    public void updateEmail(String email) {
        btnPassword.setText(getString(R.string.send_otp));
        tilPhone.setVisibility(View.GONE);
        tilEmail.setVisibility(View.VISIBLE);
        editEmail.setText(email);
    }

    public void forgotEmail(View view) {
        tilEmail.setVisibility(tilEmail.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        tilPhone.setVisibility(tilPhone.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        btnPassword.setText(btnPassword.getText().equals(getString(R.string.send_otp))? getString(R.string.find_my_email):getString(R.string.send_otp));
        textForgotEmail.setText(textForgotEmail.getText().equals(getString(R.string.forget_your_email))? getString(R.string.forget_your_phone_no):getString(R.string.forget_your_email));
    }
}

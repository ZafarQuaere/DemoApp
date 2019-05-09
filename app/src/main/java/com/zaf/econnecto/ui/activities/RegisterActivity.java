package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.request_model.Register;
import com.zaf.econnecto.ui.presenters.RegisterPresenter;
import com.zaf.econnecto.ui.presenters.operations.IRegister;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegister {

    private Context mContext;
    private String mobile;
    private EditText editMobile;

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter(this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        Utils.updateActionBar(this,new RegisterActivity().getClass().getSimpleName(),getString(R.string.register),
                null,null);
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(RegisterActivity.this, EnterOTPActivity.class));
                validateFields();
            }
        });

         mobile = getIntent().getStringExtra("mobile");
         editMobile = (EditText) findViewById(R.id.editMobile);
         editMobile.setText(mobile != null ? mobile : "");
    }

    private void validateFields() {
        EditText editName = (EditText) findViewById(R.id.editName);

        EditText editEmailId = (EditText) findViewById(R.id.editEmailId);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        EditText editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);

        getPresenter().validateFields(editName.getText().toString().trim(),
                editMobile.getText().toString().trim(),
                editEmailId.getText().toString().trim(),
                editPassword.getText().toString().trim(),
                editConfirmPassword.getText().toString().trim());
    }

    @Override
    public void callApi(Register register) {
        //openProgressDialog();
        getPresenter().callRegisterApi(register);

    }

    @Override
    public void doRegister() {
       // hideProgressDialog();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext,getString(R.string.ok),msg);
    }
}

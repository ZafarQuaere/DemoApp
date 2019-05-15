package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.request_model.Register;
import com.zaf.econnecto.ui.presenters.LoginPresenter;
import com.zaf.econnecto.ui.presenters.operations.ILogin;
import com.zaf.econnecto.utils.KeyboardUtils;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class LoginActivity extends BaseActivity<LoginPresenter> implements ILogin, View.OnClickListener {

    private EditText editUserName;
    private EditText editPassword;
    private Context mContext;


    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        if (Utils.isLoggedIn(mContext)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            initUI();
        }

    }

    private void initUI() {
      //  Utils.updateActionBar(this,new LoginActivity().getClass().getSimpleName(),getString(R.string.login_label), null,null);

        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        TextView textForgetPswd = (TextView) findViewById(R.id.textForgetPswd);
        TextView txtRegister = (TextView) findViewById(R.id.txtRegister);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        textForgetPswd.setOnClickListener(this);
        txtRegister.setOnClickListener(this);

    }

    private void validationField() {
        String mobile = editUserName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        getPresenter().validateUsernamePassword(mobile, password);
    }

    @Override
    public void doLogin() {

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

    @Override
    public void callLoginApi(String userId, String password) {
        getPresenter().callApi(userId, password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textForgetPswd:
                getPresenter().startActivity(mContext);
                break;

            case R.id.btnLogin:
                validationField();
                break;

            case R.id.txtRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

        }
    }
}

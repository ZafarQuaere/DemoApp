package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.NewPasswordPresenter;
import com.zaf.econnecto.ui.presenters.operations.INewPswd;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class EnterNewPswdActivity extends BaseActivity<NewPasswordPresenter> implements INewPswd {

    private EditText editPassword;
    private EditText editConfirmPassword;
    private LinearLayout lytTop;
    private Context mContext;
    private TextView textForgetPswd;


    @Override
    protected NewPasswordPresenter initPresenter() {
        return new NewPasswordPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_pswd);
        mContext = this;
        initUI();

    }

    private void initUI() {
        Utils.updateActionBar(this,new EnterNewPswdActivity().getClass().getSimpleName(),getString(R.string.change_pswd),
                null,null);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        Button btnChangePswd = (Button) findViewById(R.id.btnChangePswd);
        btnChangePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        String password = editPassword.getText().toString().trim();
        String confrmPswd = editConfirmPassword.getText().toString().trim();
        getPresenter().validatePswd(password,confrmPswd );
    }

    @Override
    public void changePswd() {
        startActivity(new Intent(EnterNewPswdActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

    @Override
    public void callApi(String password) {
        getPresenter().changePswdApi(password);
    }


}

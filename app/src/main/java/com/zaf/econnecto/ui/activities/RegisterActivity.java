package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.adapters.AgeGroupRecyclerAdapter;
import com.zaf.econnecto.ui.interfaces.AgeSelectedListener;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.RegisterPresenter;
import com.zaf.econnecto.ui.presenters.operations.IRegister;
import com.zaf.econnecto.utils.LogUtils;

import java.util.ArrayList;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegister, AgeSelectedListener {

    private Context mContext;
    private ArrayList<String> ages;
    private AgeGroupRecyclerAdapter ageGroupRecyclerAdapter;
    private String mSelectedAge;
    private String mSelectedGender;

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        scrollViewImplementation();
        findViewById(R.id.txtRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(RegisterActivity.this, ChangePswdActivity.class));
                validateFields();
            }
        });
        findViewById(R.id.txtLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initUI();

    }

    private void initUI() {
        RadioGroup rGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRb = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = selectedRb.isChecked();
                if (isChecked) {
                    mSelectedGender = selectedRb.getText().toString().trim();
                }
            }
        });
    }

    private void scrollViewImplementation() {
        RecyclerView ageScrollView = (RecyclerView) findViewById(R.id.recyclerAge);
        ageScrollView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Adding items to RecyclerView.
        addAgeGroupItem();
        ageGroupRecyclerAdapter = new AgeGroupRecyclerAdapter(mContext, ages);
        ageGroupRecyclerAdapter.setListener(this);

        ageScrollView.setAdapter(ageGroupRecyclerAdapter);


    }

    private void addAgeGroupItem() {
        ages = new ArrayList<>();
        ages.add("< 15");
        ages.add("15-18");
        ages.add("19-25");
        ages.add("26-30");
        ages.add("31-40");
        ages.add("> 40");

    }

    private void validateFields() {
        EditText editFirstName = (EditText) findViewById(R.id.editFirstName);
        EditText editLastName = (EditText) findViewById(R.id.editLastName);
        EditText editUserName = (EditText) findViewById(R.id.editUserName);
        // EditText editMobile = (EditText) findViewById(R.id.editMobile);
        EditText editEmailId = (EditText) findViewById(R.id.editEmailId);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        EditText editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);

        //String ageGroup = getPresenter().getAgeGroup(mSelectedAge);
        getPresenter().validateFields(editFirstName.getText().toString().trim(),
                editLastName.getText().toString().trim(),
                editUserName.getText().toString().trim(),
                editEmailId.getText().toString().trim(),
                editPassword.getText().toString().trim(),
                editConfirmPassword.getText().toString().trim(), mSelectedAge, mSelectedGender);
    }

    @Override
    public void doRegister() {
        LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), getString(R.string.congratulations_your_registration_is_successfull), new DialogButtonClick() {
            @Override
            public void onOkClick() {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
            @Override
            public void onCancelClick() { }
        });

    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

    @Override
    public void onAgeSelected(String selectedAge) {
        this.mSelectedAge = selectedAge;
    }
}

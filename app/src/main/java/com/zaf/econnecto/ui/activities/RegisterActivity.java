package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.request_model.Register;
import com.zaf.econnecto.ui.adapters.RecyclerViewAdapter;
import com.zaf.econnecto.ui.presenters.RegisterPresenter;
import com.zaf.econnecto.ui.presenters.operations.IRegister;
import com.zaf.econnecto.utils.LogUtils;

import java.util.ArrayList;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegister {

    private Context mContext;
    private ArrayList<String> number;
    private RecyclerViewAdapter recyclerViewHorizontalAdapter;
    private View childView;
    private int recyclerViewItemPosition;

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
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(RegisterActivity.this, EnterOTPActivity.class));
                validateFields();
            }
        });

    }

    private void scrollViewImplementation() {
        RecyclerView ageScrollView = (RecyclerView) findViewById(R.id.recyclerAge);
        ageScrollView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        // Adding items to RecyclerView.
        AddItemsToRecyclerViewArrayList();
        recyclerViewHorizontalAdapter = new RecyclerViewAdapter(number);


        ageScrollView.setAdapter(recyclerViewHorizontalAdapter);

        // Adding on item click listener to RecyclerView.
        ageScrollView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rcView, MotionEvent motionEvent) {
                childView = rcView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (childView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    //Getting clicked value.
                    recyclerViewItemPosition = rcView.getChildAdapterPosition(childView);
                    // Showing clicked item value on screen using toast message.
                    // Toast.makeText(mContext, number.get(recyclerViewItemPosition), Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

    }

    private void AddItemsToRecyclerViewArrayList() {
        number = new ArrayList<>();
        number.add("ONE");
        number.add("TWO");
        number.add("THREE");
        number.add("FOUR");
        number.add("FIVE");
        number.add("SIX");
        number.add("SEVEN");
        number.add("EIGHT");
        number.add("NINE");
        number.add("TEN");
    }

    private void validateFields() {
        EditText editFirstName = (EditText) findViewById(R.id.editFirstName);
        EditText editLastName = (EditText) findViewById(R.id.editLastName);
        EditText editUserName = (EditText) findViewById(R.id.editUserName);
        EditText editMobile = (EditText) findViewById(R.id.editMobile);
        EditText editEmailId = (EditText) findViewById(R.id.editEmailId);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        EditText editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);

        getPresenter().validateFields(editUserName.getText().toString().trim(),
                editMobile.getText().toString().trim(),
                editEmailId.getText().toString().trim(),
                editPassword.getText().toString().trim(),
                editConfirmPassword.getText().toString().trim());
    }

    @Override
    public void callApi(Register register) {
        getPresenter().callRegisterApi(register);

    }

    @Override
    public void doRegister() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }
}

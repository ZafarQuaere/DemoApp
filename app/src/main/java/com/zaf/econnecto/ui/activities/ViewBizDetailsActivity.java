package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;


import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.request_model.AddressData;
import com.zaf.econnecto.ui.presenters.AddressPresenter;
import com.zaf.econnecto.ui.presenters.operations.ISellerAddress;
import com.zaf.econnecto.utils.LogUtils;


public class ViewBizDetailsActivity extends AppCompatActivity/*BaseActivity<AddressPresenter>*/ implements ISellerAddress {

    private Context mContext;

   /* @Override
    protected AddressPresenter initPresenter() {
        return new AddressPresenter(this, this);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_biz_details);
        mContext = this;


        initUI();
       // Utils.updateActionBar(this,new ViewBizDetailsActivity().getClass().getSimpleName(),getString(R.string.biz_details), null,null);


    }

    private void initUI() {
        final Toolbar toolbar = findViewById(R.id.toolbarBd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.business_details));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    private void validateFields() {
      /*  EditText editAddress = (EditText) findViewById(R.id.editAddress);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editPincode = (EditText) findViewById(R.id.editPincode);

        getPresenter().validateFields(editAddress.getText().toString().trim(),
                editCity.getText().toString().trim(),
                editState.getText().toString().trim(),
                editPincode.getText().toString().trim());*/
    }

    @Override
    public void callApi(AddressData addressData) {
        //openProgressDialog();
       // getPresenter().callAddressApi(addressData);

    }

    @Override
    public void saveAddress() {
        // hideProgressDialog();
        startActivity(new Intent(ViewBizDetailsActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }
}

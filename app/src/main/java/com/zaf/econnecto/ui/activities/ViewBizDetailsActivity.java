package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.request_model.AddressData;
import com.zaf.econnecto.ui.presenters.AddressPresenter;
import com.zaf.econnecto.ui.presenters.operations.ISellerAddress;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class ViewBizDetailsActivity extends BaseActivity<AddressPresenter> implements ISellerAddress {

    private Context mContext;

    @Override
    protected AddressPresenter initPresenter() {
        return new AddressPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_biz_details);
        mContext = this;
        Utils.updateActionBar(this,new ViewBizDetailsActivity().getClass().getSimpleName(),getString(R.string.biz_details),
                null,null);


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
        getPresenter().callAddressApi(addressData);

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

package com.zaf.econnecto.ui.presenters;

import android.content.Context;
import android.location.Address;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.request_model.AddressData;
import com.zaf.econnecto.ui.presenters.operations.IBizDetail;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;


import org.json.JSONObject;

public class BizDetailPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IBizDetail mSellerAddress;

    public BizDetailPresenter(Context context, IBizDetail iBizDetail) {
        super(context);
        mSellerAddress = iBizDetail;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validateFields(String address, String city, String state, String pincode) {
        if (address.equals("") || address.isEmpty()) {
            mSellerAddress.onValidationError(mContext.getString(R.string.please_enter_address));
        } else if (city.equals("") || city.isEmpty()) {
            mSellerAddress.onValidationError(mContext.getString(R.string.please_enter_city_name));
        } else if (state.equals("") || state.isEmpty()) {
            mSellerAddress.onValidationError(mContext.getString(R.string.please_enter_state));
        } else if (pincode.equals("") || pincode.isEmpty() || pincode.length() < 6 || pincode.length() > 6) {
            mSellerAddress.onValidationError(mContext.getString(R.string.please_enter_valid_pincode));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                getLatLongNCallApi(address, city, state, pincode);

            } else {
                mSellerAddress.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }

    private void getLatLongNCallApi(String address, String city, String state, String pincode) {
        String fullAddress = address + ", " + city + ", " + state + ", " + pincode;
        Address location = Utils.getlocationfromaddress(mContext, fullAddress);
        AddressData adress = new AddressData(address, state, city, pincode, location.getLatitude() + "", "" + location.getLongitude());
        //mSellerAddress.callApi(adress);
    }

    public void callBizDetailApi(final String biz_id) {
        loader.show();

        String url = AppConstant.URL_BASE + AppConstant.URL_BIZ_DETAIL+biz_id;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" );
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("BizDetail Response ::" + response.toString());
                loader.dismiss();
                mSellerAddress.updateUI();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("BizDetail Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "BizDetail");
    }
}

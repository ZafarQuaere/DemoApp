package com.zaf.econnecto.ui.presenters;

import android.content.Context;
import android.location.Address;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.request_model.AddressData;
import com.zaf.econnecto.ui.presenters.operations.ISellerAddress;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

public class AddressPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private ISellerAddress mSellerAddress;

    public AddressPresenter(Context context, ISellerAddress iSellerAddress) {
        super(context);
        mSellerAddress = iSellerAddress;
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
        mSellerAddress.callApi(adress);
    }

    public void callAddressApi(final AddressData addressData) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userId", Utils.getUserId(mContext));
            requestObject.put("addressOne", addressData.getAddress());
            requestObject.put("city", addressData.getCity());
            requestObject.put("state", addressData.getState());
            requestObject.put("pincode", addressData.getPincode());
            requestObject.put("latitude", addressData.getLatitude());
            requestObject.put("longitude", addressData.getLongitude());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_DEALER_ADDRESS;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("AddressData Response ::" + response.toString());
                loader.dismiss();
                mSellerAddress.saveAddress();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("AddressData Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "AddressData");
    }
}

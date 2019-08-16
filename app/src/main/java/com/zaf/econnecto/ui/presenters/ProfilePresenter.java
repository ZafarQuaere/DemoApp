package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.request_model.AddressData;
import com.zaf.econnecto.network_call.response_model.login.LoginData;
import com.zaf.econnecto.ui.presenters.operations.IFragProfile;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;


import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePresenter extends BaseFragmentPresenter {
    private Context mContext;
    private IFragProfile iFragProfile;

    public ProfilePresenter(Context context, IFragProfile iFragProfile) {
        super(context);
        this.iFragProfile = iFragProfile;
        mContext = context;
    }

    public void validateFields(String address, String city, String state, String pincode) {
        if (address.equals("") || address.isEmpty()) {
            iFragProfile.onValidationError(mContext.getString(R.string.please_enter_address));
        } else if (city.equals("") || city.isEmpty()) {
            iFragProfile.onValidationError(mContext.getString(R.string.please_enter_city_name));
        } else if (state.equals("") || state.isEmpty()) {
            iFragProfile.onValidationError(mContext.getString(R.string.please_enter_state));
        } else if (pincode.equals("") || pincode.isEmpty() || pincode.length() < 6 || pincode.length() > 6) {
            iFragProfile.onValidationError(mContext.getString(R.string.please_enter_valid_pincode));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                AddressData adress = new AddressData(address, state, city, pincode, "latitude", "longitude");
               // iFragProfile.callApi(adress);
            } else {
                iFragProfile.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }

    public void callAddressApi(final AddressData addressData) {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("name", "");
            requestObject.put("email", "");
            requestObject.put("mobile", "");
            requestObject.put("state", "");

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_DEALER_ADDRESS;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("AddressData Response ::" + response.toString());

                // iFragProfile.updateUI();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                LogUtils.DEBUG("AddressData Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "AddressData");
    }


    public void updateUI() {
        LoginData loginData = ParseManager.getInstance().fromJSON(Utils.getLoginData(mContext), LoginData.class);
        iFragProfile.updateUI(loginData.getData());
    }
}

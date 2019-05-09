package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.presenters.operations.IAddSeller;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;


import org.json.JSONException;
import org.json.JSONObject;

public class AddSellerPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IAddSeller mSeller;

    public AddSellerPresenter(Context context, IAddSeller iAddSeller) {
        super(context);
        mSeller = iAddSeller;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validateFields(String name, String mobileNo) {
        if (name.equals("") || name.isEmpty()) {
            mSeller.onValidationError(mContext.getString(R.string.please_enter_seller_name));
        } else if (mobileNo.equals("") || mobileNo.isEmpty() || mobileNo.length() < 10) {
            mSeller.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                mSeller.callApi(name, mobileNo);
            } else {
                mSeller.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }

        }
    }

    public void callAddSellerApi(String dName, final String mobile) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("dName", dName);
            requestObject.put("mobile", mobile);
            requestObject.put("brandIds", "");
            requestObject.put("rating", "");
            requestObject.put("addressId", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_INSERT_DELAER;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("AddSeller Response ::" + response.toString());
                loader.dismiss();
                mSeller.addSeller(mobile);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("AddSeller Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "AddSeller");
    }
}

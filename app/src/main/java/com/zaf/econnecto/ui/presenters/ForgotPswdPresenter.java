package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.presenters.operations.IFrgtPswd;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPswdPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IFrgtPswd iFrgtPswd;

    public ForgotPswdPresenter(Context context, IFrgtPswd iFrgtPswd) {
        super(context);
        this.iFrgtPswd = iFrgtPswd;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validateInput(String email) {
        if (email.equals("") || email.isEmpty()) {
            iFrgtPswd.onValidationError(mContext.getString(R.string.please_enter_valid_email));
        } else if (!Utils.isValidEmail(email)) {
            iFrgtPswd.onValidationError(mContext.getString(R.string.please_enter_valid_email));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                iFrgtPswd.callOtpApi(email);
            } else {
                iFrgtPswd.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }

    public void callOtpApi(final String email) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("action", mContext.getString(R.string.request_otp));
            requestObject.put("email", email);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_FORGOT_PSWD;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Request OTP Response ::" + response.toString());
                if (response != null && !response.equals("")){
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS){
                        iFrgtPswd.startOTPActivity();
                        //LogUtils.showToast(mContext,editOTP.getText().toString());
                    }else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }

                }
                loader.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.ERROR("Request OTP Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Request OTP");

    }

    private void callOTPServiceApi(String mobile) {
        String url = AppConstant.URL_BASE + AppConstant.URL_OTP_SERVICE + mobile;
        //LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest otpServiceRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("OtpService Response ::" + response.toString());
                Utils.saveOTPData(mContext, response.toString());
                iFrgtPswd.startOTPActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.DEBUG("OtpService Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(otpServiceRequest, "OtpService");
    }
}

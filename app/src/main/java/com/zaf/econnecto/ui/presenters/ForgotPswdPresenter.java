package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
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

    public void validMobileNo(String mobileNo) {
        if (mobileNo.equals("") || mobileNo.isEmpty()) {
            iFrgtPswd.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
        } else if (mobileNo.length() < 10) {
            iFrgtPswd.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                iFrgtPswd.callSubmitMobileApi(mobileNo);
            } else {
                iFrgtPswd.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }

    public void callSubmitMobileApi(final String mobile) {
        loader.show();
        String url = AppConstant.URL_BASE + AppConstant.URL_VERIFY_MOBILE + mobile;

        //  LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("ForgotPassword Response ::" + response.toString());
                loader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String resMob = jsonObject.optString("mobile");
                    if (resMob.equalsIgnoreCase(mobile)) {
                        Utils.setMobileNo(mContext, mobile);
                        callOTPServiceApi(mobile);
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok),
                                mContext.getString(R.string.please_enter_valid_registered_number));
                    }
                } catch (JSONException e) {
                    LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok),
                            mContext.getString(R.string.please_enter_valid_registered_number));
                    e.printStackTrace();
                    LogUtils.ERROR(e.getMessage());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("ForgotPassword Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "ForgotPassword");
    }

    private void callOTPServiceApi(String mobile) {
        String url = AppConstant.URL_BASE + AppConstant.URL_OTP_SERVICE + mobile;
        //LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest otpServiceRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("OtpService Response ::" + response.toString());
                Utils.saveOTPData(mContext, response.toString());
                iFrgtPswd.submitMobile();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.DEBUG("OtpService Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(otpServiceRequest, "OtpService");
    }

    public void updateActionBar(Context mContext) {
        // int intExtra = ((Activity) mContext).getIntent().getIntExtra(AppConstant.COMINGFROM, AppConstant.LOGIN);
        // LogUtils.showToast(mContext,intExtra== 1 ? "Coming form Login" : "Coming from home");
        // Utils.updateActionBar(mContext, new ForgetPswdActivity().getClass().getSimpleName(), intExtra == 1 ? mContext.getString(R.string.forgot_pswd) : mContext.getString(R.string.change_pswd), null, null);
    }
}

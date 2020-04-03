package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
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

    public void validateInput(boolean isEmail, String email, String phone) {
        if (isEmail){
            if (email.equals("") || email.isEmpty()) {
                iFrgtPswd.onValidationError(mContext.getString(R.string.please_enter_valid_email));
            } else if (!Utils.isValidEmail(email)) {
                iFrgtPswd.onValidationError(mContext.getString(R.string.please_enter_valid_email));
            }if (NetworkUtils.isNetworkEnabled(mContext)) {
                iFrgtPswd.callOtpApi(email);
            } else {
                iFrgtPswd.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
        else {
            if (phone.equals("") || phone.isEmpty()) {
                iFrgtPswd.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
            } else if (NetworkUtils.isNetworkEnabled(mContext)) {
                callFindEmailApi(phone);
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
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_FORGOT_PSWD;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Request OTP Response ::" + response.toString());
                if (response != null && !response.equals("")){
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS_501){
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.an_otp_has_been_sent_to_your_email_plz_check_spam_folder_as_well), new DialogButtonClick() {
                            @Override
                            public void onOkClick() {
                                iFrgtPswd.startOTPActivity();
                            }
                            @Override
                            public void onCancelClick() { }
                        });

                    }else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0));
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

    private void callFindEmailApi(String mobile) {
        loader.show();
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_FIND_MY_EMAIL;
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("phone", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
            loader.dismiss();
        }
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest findEmailRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, response -> {
            LogUtils.DEBUG("FIND_MY_EMAIL Response ::" + response.toString());
            int status = response.optInt("status");
            if (status == AppConstant.SUCCESS){
                JSONObject data = response.optJSONObject("data");
                String email = data.optString("email");
                iFrgtPswd.updateEmail(email);
            }else {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0));
            }
            loader.dismiss();
        }, error -> {
            LogUtils.DEBUG("OtpService Error ::" + error.getMessage());
            loader.dismiss();
        } );

        AppController.getInstance().addToRequestQueue(findEmailRequest, "FIND_MY_EMAIL");
    }
}

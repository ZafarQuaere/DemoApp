package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.operations.IOtp;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpPresenter extends BasePresenter {
    private Context mContext;
    private IOtp iOtp;
    private AppLoaderFragment loader;

    public OtpPresenter(Context context, IOtp iOtp) {
        super(context);
        this.iOtp = iOtp;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validateInputs(String otp, String pswd, String confrmPswd, String email) {
        if (otp.equals("") || otp.isEmpty())
            iOtp.onValidationError(mContext.getString(R.string.please_enter_otp));
        else if (otp.length() < 4)
            iOtp.onValidationError(mContext.getString(R.string.enter_valid_otp));
        else if (pswd == null || confrmPswd == null)
            iOtp.onValidationError(mContext.getString(R.string.please_enter_password));
        else if (pswd.length() < 8 || confrmPswd.length() < 8)
            iOtp.onValidationError(mContext.getString(R.string.password_must_have_atleast_8_character));
        else if (!pswd.equals(confrmPswd))
            iOtp.onValidationError(mContext.getString(R.string.please_enter_same_pswd));
        else if (pswd.equals(confrmPswd)) {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                callResetPassword(otp, pswd, email);
            } else {
                iOtp.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }


    public void callResetPassword(String otp, String pswd, String email) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("action", mContext.getString(R.string.reset_pwd));
            requestObject.put("email", email);
            requestObject.put("otp", otp);
            requestObject.put("password", pswd);
//            requestObject.put("confirm_password", pswd);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_FORGOT_PSWD;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Reset Pswd Response ::" + response.toString());

                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS_401) {
                        iOtp.moveToLogin();
                        //LogUtils.showToast(mContext, mContext.getString(R.string.your_password_is_changed_successfully));
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0));
                      //  LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }
                }
                loader.dismiss();
            }
        }, error -> {
            loader.dismiss();
            LogUtils.ERROR("Reset Pswd Error ::" + error.getMessage());
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Reset Pswd");
    }

    public void validateCPInputs(String mobile, String oldPswd, String newPswd, String email) {
        if (mobile.equals("") || mobile.isEmpty())
            iOtp.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
        else if (mobile.length() < 10)
            iOtp.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
        else if (oldPswd.isEmpty())
            iOtp.onValidationError(mContext.getString(R.string.please_enter_password));
        else if (newPswd.isEmpty())
            iOtp.onValidationError(mContext.getString(R.string.please_enter_new_password));
        else if (oldPswd.length() < 8 || newPswd.length() < 8)
            iOtp.onValidationError(mContext.getString(R.string.password_must_have_atleast_8_character));
       else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                callChangePswdApi(mobile, oldPswd,newPswd, email);
            } else {
                iOtp.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }


    public void callChangePswdApi(String mobile, String pswd,String newPswd, String email) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("phone",mobile);
            requestObject.put("old_password", pswd);
            requestObject.put("new_password", newPswd);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_CHANGE_PSWD;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Change Pswd Response ::" + response.toString());
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.your_password_changed_successfully), new DialogButtonClick() {
                            @Override
                            public void onOkClick() {
                                ((Activity)mContext).finish();
                              //  iOtp.moveToLogin();
                            }
                            @Override
                            public void onCancelClick() { }
                        });

                        //LogUtils.showToast(mContext, mContext.getString(R.string.your_password_is_changed_successfully));
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0));
                        //  LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }
                }
                loader.dismiss();
            }
        }, error -> {
            loader.dismiss();
            LogUtils.ERROR("Change Pswd Error ::" + error.getMessage());
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Change Pswd");
    }

}

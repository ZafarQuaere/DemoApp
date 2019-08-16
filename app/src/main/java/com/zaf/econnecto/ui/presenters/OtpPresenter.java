package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
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
        else if (pswd.length() < 6 || confrmPswd.length() < 6)
            iOtp.onValidationError(mContext.getString(R.string.password_must_have_atleast_6_character));
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
            requestObject.put("confirm_password", pswd);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_FORGOT_PSWD;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Change Pswd Response ::" + response.toString());

                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        iOtp.changePassword();
                        //LogUtils.showToast(mContext, mContext.getString(R.string.your_password_is_changed_successfully));
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }

                }
                loader.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.ERROR("Change Pswd Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Change Pswd");
    }

}

package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.presenters.operations.INewPswd;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

public class NewPasswordPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private INewPswd mNewPswd;

    public NewPasswordPresenter(Context context, INewPswd iNewPswd) {
        super(context);
        mNewPswd = iNewPswd;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validatePswd(String password, String confirmPswd) {
        if (password.equals("") || password.isEmpty()) {
            mNewPswd.onValidationError(mContext.getString(R.string.please_enter_password));
        }  else if (password.length() < 8) {
            mNewPswd.onValidationError(mContext.getString(R.string.password_must_have_atleast_8_character));
        } else if (!password.equalsIgnoreCase(confirmPswd)) {
            mNewPswd.onValidationError(mContext.getString(R.string.please_enter_same_pswd));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                mNewPswd.callApi(password);
            } else {
                mNewPswd.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }

    public void changePswdApi(String password) {
     /*   String otpData = Utils.getOTPData(mContext);

        LogUtils.DEBUG("OTP DATA : " + otpData);*/
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("mobile", "");
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_CHANGE_PASSWORD + Utils.getMobileNo(mContext)+ AppConstant.URL_CP_PASSWORD+password;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("SubmitOtp Response ::" + response.toString());
                mNewPswd.changePswd();
                loader.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                  loader.dismiss();
                LogUtils.DEBUG("SubmitOtp Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "SubmitOtp");
    }
}

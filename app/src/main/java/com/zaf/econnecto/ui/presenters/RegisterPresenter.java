package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.presenters.operations.IRegister;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IRegister mRegister;

    public RegisterPresenter(Context context, IRegister iRegister) {
        super(context);
        mRegister = iRegister;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validateFields(String firstName, String lastName, String userName, String email, String pswd, String confmPswd, String ageGroup, String gender) {
        if (userName.equals("") || userName.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_username));
        } else if (!Utils.isValidEmail(email)) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_valid_email));
        } else if (pswd.equals("") || pswd.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_password));
        } else if (pswd.length() < 6) {
            mRegister.onValidationError(mContext.getString(R.string.password_must_have_atleast_6_character));
        } else if (!confmPswd.equalsIgnoreCase(pswd)) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_same_pswd));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                callRegisterApi(firstName, lastName, userName, pswd, email, pswd, confmPswd, ageGroup, gender);
            } else {
                mRegister.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }

        }
    }

    public void callRegisterApi(String firstName, String lastName, String userName, String pswd, String email, String pswd1, String confmPswd, String ageGroup, String gender) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("first_name", firstName);
            requestObject.put("last_name", lastName);
            requestObject.put("username", userName);
            requestObject.put("email", email);
            requestObject.put("password", pswd);
            requestObject.put("confirm_password", confmPswd);
            // requestObject.put("mobile", register.getMobile());
            requestObject.put("age_group", ageGroup);
            requestObject.put("sex", gender);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_REGISTER;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Register Response ::" + response.toString());
                loader.dismiss();
                mRegister.doRegister();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("Register Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Register");
    }

    public String getAgeGroup(String mSelectedAge) {
        switch (mSelectedAge) {
            case "< 15":
                return "G1";
            case "15-18":
                return "G2";
            case "19-25":
                return "G3";
            case "26-30":
                return "G4";
            case "31-40":
                return "G5";
            case "> 40":
                return "G6";
        }
        return "G2";
    }
}

package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
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
        if (firstName.equals("") || firstName.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_firstname));
        }
        else if (lastName.equals("") || lastName.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_lastname));
        }
        else if (userName.equals("") || userName.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_username));
        } else if (!Utils.isValidEmail(email)) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_valid_email));
        } else if (pswd.equals("") || pswd.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_password));
        } else if (pswd.length() < 8) {
            mRegister.onValidationError(mContext.getString(R.string.password_must_have_atleast_8_character));
        } else if (!confmPswd.equalsIgnoreCase(pswd)) {
            mRegister.onValidationError(mContext.getString(R.string.please_enter_same_pswd));
        }else if (gender == null || gender.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_select_gender));
        } else if (ageGroup == null || ageGroup.isEmpty()) {
            mRegister.onValidationError(mContext.getString(R.string.please_select_age));
        }
        else {
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
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_REGISTER;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Register Response ::" + response.toString());
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.register_successful_plz_login), new DialogButtonClick() {
                            @Override
                            public void onOkClick() {
                                mRegister.doRegister();
                            }
                            @Override
                            public void onCancelClick() { }
                        });
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
                LogUtils.ERROR("Register Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Register");
    }

    public String getAgeGroup(String mSelectedAge) {
        if (mSelectedAge.contains("< 15"))
            return "G1";
        else if (mSelectedAge.contains("15-18")){
            return "G2";
        }
        else if (mSelectedAge.contains("19-25")){
            return "G3";
        }
        else if (mSelectedAge.contains("26-30")){
            return "G4";
        }
        else if (mSelectedAge.contains("31-40")){
            return "G5";
        }
        else if (mSelectedAge.contains("> 40")){
            return "G6";
        }
        return "G2";
    }
}

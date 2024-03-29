package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.UiThread;

import com.android.volley.Request;
import com.android.volley.Response;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.login.LoginData;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
import com.zaf.econnecto.ui.activities.PhoneVerificationActivity;
import com.zaf.econnecto.ui.presenters.operations.ILogin;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.BitmapUtils;
import com.zaf.econnecto.utils.DateUtils;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter extends BasePresenter {
    private Context mContext;
    private ILogin mLogin;
    private AppLoaderFragment loader ;

    public LoginPresenter(Context context, ILogin iLogin) {
        super(context);
        mLogin = iLogin;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validateUsernamePassword(String userId, String password) {
        if (userId.equals("") || userId.isEmpty()) {
            mLogin.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
        } else if (password.equals("") || password.isEmpty()) {
            mLogin.onValidationError(mContext.getString(R.string.please_enter_password));
        } else if (password.length() < 8) {
            mLogin.onValidationError(mContext.getString(R.string.password_must_have_atleast_8_character));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
               mLogin.callLoginApi(userId, password);
                //mLogin.doLogin();
            }else {
                mLogin.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }

   /* public void callApi(final String phoneNo, String password) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            //requestObject.put("email", userId);
            requestObject.put("phone", phoneNo);
            requestObject.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_LOGIN_MVP;
        LogUtils.DEBUG("Login URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Login Response ::" + response.toString());
                if (response != null && !response.equals("")){
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS){
                        Utils.setLoggedIn(mContext, true);
                        LoginData loginData = ParseManager.getInstance().fromJSON(response.toString(),LoginData.class);
                        Utils.saveLoginData(mContext,response.toString());
                        storeOtherValue(loginData);
                        mLogin.doLogin();
                    } else if (status == AppConstant.PHONE_NOT_VERIFIED) {
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok),
                                mContext.getString(R.string.your_phone_number_is_not_verified_plz_verify_it),
                                () -> mContext.startActivity(new Intent(mContext, PhoneVerificationActivity.class).putExtra("mobile", phoneNo)));
                    }
                    else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0));
                    }
                }
                loader.dismiss();
            }
        }, error -> {
            loader.dismiss();
            LogUtils.ERROR("Login Error ::" + error.getMessage());
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Login");
    }*/

    private void storeOtherValue(LoginData loginData) {
        Utils.storeProfileImage((Activity) mContext,loginData);
        Utils.setBusinessStatus(mContext,loginData.getData().getBusinessStatus());
        Utils.setEmailVerified(mContext, loginData.getData().getIsEmailVerified().equals("1"));
        DateUtils.setLoginDate(mContext);
    }



    public void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, ForgetPswdActivity.class);
        intent.putExtra(AppConstant.COMINGFROM,AppConstant.LOGIN);
        mContext.startActivity(intent);
    }
}

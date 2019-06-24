package com.zaf.econnecto.ui.presenters;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.login.LoginPojo;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
import com.zaf.econnecto.ui.presenters.operations.ILogin;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
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
            mLogin.onValidationError(mContext.getString(R.string.please_enter_username));
        } else if (password.equals("") || password.isEmpty()) {
            mLogin.onValidationError(mContext.getString(R.string.please_enter_password));
        } else if (password.length() < 6) {
            mLogin.onValidationError(mContext.getString(R.string.password_must_have_atleast_6_character));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
               mLogin.callLoginApi(userId, password);
                //mLogin.doLogin();
            }else {
                mLogin.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }

    public void callApi(final String userId, String password) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("email", userId);
            requestObject.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_LOGIN;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Login Response ::" + response.toString());

                if (response != null && !response.equals("")){
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS){
                        Utils.setLoggedIn(mContext, true);
                        //Utils.saveLoginData(mContext,response.toString());
                        Utils.saveUserEmail(mContext,userId);
                        mLogin.doLogin();
                    }else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }

                }
                loader.dismiss();
              //  LoginPojo loginData = ParseManager.getInstance().fromJSON(response.toString(),LoginPojo.class);


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("Login Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Login");
    }

    public void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, ForgetPswdActivity.class);
        intent.putExtra(AppConstant.COMINGFROM,AppConstant.LOGIN);
        mContext.startActivity(intent);
    }
}

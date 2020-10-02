package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.service.EConnectoServices;
import com.zaf.econnecto.service.ServiceBuilder;
import com.zaf.econnecto.ui.interfaces.DialogSingleButtonListener;
import com.zaf.econnecto.ui.presenters.operations.IOtp;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

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

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestObject.toString());
        EConnectoServices eConnectoServices = ServiceBuilder.INSTANCE.buildConnectoService(EConnectoServices.class);
        Call<JsonObject> requestCall = eConnectoServices.resetPassword(requestBody);

        requestCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, retrofit2.Response<JsonObject> jObject) {
                try {
                    JSONObject response = new JSONObject(new Gson().toJson(jObject.body()));
                    LogUtils.DEBUG("Reset Pswd Response ::" + response.toString());
                    loader.dismiss();
                    if (response != null && !response.equals("")) {
                        int status = response.optInt("status");
                        if (status == AppConstant.SUCCESS_401) {
                            iOtp.moveToLogin();
                        } else {
                            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, Throwable t) {
                loader.dismiss();
                LogUtils.ERROR("Reset Pswd Error ::" + t.getLocalizedMessage());
            }
        });

       /* String url = AppConstant.URL_BASE_MVP + AppConstant.URL_FORGOT_PSWD;
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
        AppController.getInstance().addToRequestQueue(objectRequest, "Reset Pswd");*/
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
//                callChangePswdApi(mobile, oldPswd,newPswd, email);
                callChangePswd(mobile, oldPswd, newPswd, email);
            } else {
                iOtp.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }
        }
    }


    public void callChangePswd(String mobile, String pswd, String newPswd, String email) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("phone", mobile);
            requestObject.put("old_password", pswd);
            requestObject.put("new_password", newPswd);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestObject.toString());
        EConnectoServices eConnectoServices = ServiceBuilder.INSTANCE.buildConnectoService(EConnectoServices.class);
        Call<JsonObject> requestCall = eConnectoServices.changePassword(requestBody);

        requestCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                try {
                    JSONObject responseObj = new JSONObject(new Gson().toJson(response.body()));
                    LogUtils.DEBUG("Change Pswd Response ::" + responseObj.toString());
                    int status = responseObj.optInt("status");
                    loader.dismiss();
                    if (status == AppConstant.SUCCESS) {
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.your_password_changed_successfully), new DialogSingleButtonListener() {
                            @Override
                            public void okClick() {
                                ((Activity) mContext).finish();
                            }
                        });
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), responseObj.optJSONArray("message").optString(0));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.dismiss();
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.getLocalizedMessage());
            }
        });

    }


    public void callChangePswdApi(String mobile, String pswd, String newPswd, String email) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("phone", mobile);
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
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.your_password_changed_successfully), new DialogSingleButtonListener() {
                            @Override
                            public void okClick() {
                                ((Activity) mContext).finish();
                                //  iOtp.moveToLogin();
                            }
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

package com.zaf.econnecto.ui.presenters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.VolleyMultipartRequest;
import com.zaf.econnecto.ui.activities.ChangePswdActivity;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;
import com.zaf.econnecto.ui.fragments.BizListFragment;
import com.zaf.econnecto.ui.fragments.FragmentProfile;
import com.zaf.econnecto.ui.fragments.HelpNAboutFragment;
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen1Fragment;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.operations.IMain;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.BitmapUtils;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.version2.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainPresenter extends BasePresenter {
    Context mContext;
    IMain iMain;
    private AppLoaderFragment loader;

    public MainPresenter(Context context, IMain iLogin) {
        super(context);
        iMain = iLogin;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }


    public void moveToFragment(String fragName) {
        switch (fragName) {
            case "BizCategoryFragment":
                Utils.moveToFragment(mContext, new BizCategoryFragment(), BizCategoryFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, BizCategoryFragment.class.getSimpleName(), mContext.getString(R.string.hot_deals), null, null);
                break;
            case "AddBizScreen1Fragemnt":
                Utils.moveToFragment(mContext, new AddBizScreen1Fragment(), AddBizScreen1Fragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, AddBizScreen1Fragment.class.getSimpleName(), mContext.getString(R.string.add_business), null, null);
                break;
            case "BizListFragment":
                Utils.moveToFragment(mContext, new BizListFragment(), BizListFragment.class.getSimpleName(), null);
                break;
            case "FragmentProfile":
                Utils.moveToFragment(mContext, new FragmentProfile(), FragmentProfile.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, FragmentProfile.class.getSimpleName(), mContext.getString(R.string.my_business), null, null);
                break;
            case "HelpNAboutFragment":
                Utils.moveToFragment(mContext, new HelpNAboutFragment(), HelpNAboutFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, HelpNAboutFragment.class.getSimpleName(), mContext.getString(R.string.help_faq), null, null);
                break;
            case "HomeFragment":
                Utils.moveToFragment(mContext, new HomeFragment(), HomeFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, HomeFragment.class.getSimpleName(), mContext.getString(R.string.home), null, null);
                break;
        }
        //LogUtils.showToast(mContext, fragName);
    }

    public void logoutUser() {
        LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                mContext.getString(R.string.do_you_really_want_to_logout), new DialogButtonClick() {
                    @Override
                    public void onOkClick() {
                       // callLogoutApi();
                        clearUserDataNLogout();
                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
    }


    public void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, ChangePswdActivity.class);
        // intent.putExtra(AppConstant.COMINGFROM, AppConstant.HOME);
        mContext.startActivity(intent);
    }

    public void updateActionBarTitleOnBackPress(Context mContext, Fragment baseFragment) {
        if (baseFragment.getClass().getSimpleName().contains("Category")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.hot_deals), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("List")) {
            iMain.showAddBizFab(true);
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.business_list), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Profile")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.my_business), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Add")) {
            iMain.showAddBizFab(false);
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.add_business), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("AddressData")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.save_address), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Help")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.help_faq), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Home")) {
            iMain.showAddBizFab(false);
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.home), null, null);
        }
    }

    private void clearUserDataNLogout(){
        Utils.clearLoginDatas(mContext);
        LogUtils.showToast(mContext, mContext.getString(R.string.you_are_sucessfully_logout));
        iMain.onLogoutCall();
    }

    private void callLogoutApi() {
        loader.show();
        String url = AppConstant.URL_BASE + AppConstant.URL_LOGOUT;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" /*+ requestObject.toString()*/);
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Logout Response ::" + response.toString());
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        Utils.clearLoginDatas(mContext);
                        LogUtils.showToast(mContext, mContext.getString(R.string.you_are_sucessfully_logout));
                        iMain.onLogoutCall();
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }
                }
                loader.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.DEBUG("Logout Error ::" + error.getMessage());
                loader.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Logout");
    }

    public void showVerifyDialog(final Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle(mContext.getString(R.string.verify_your_email));
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_verify_email);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final EditText editOTP = (EditText) dialog.findViewById(R.id.editVerifyOtp);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String OTP = editOTP.getText().toString().trim();
                if (OTP != null && !OTP.isEmpty()) {
                    dialog.dismiss();
                    callVerfiyEmailApi(OTP);
                } else {
                    LogUtils.showToast(mContext, mContext.getString(R.string.please_enter_valid_OTP));
                }
            }
        });
        dialog.show();
    }


    public void requestOtpApi() {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("action", mContext.getString(R.string.request_otp));
            requestObject.put("email", Utils.getUserEmail(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_EMAIL_VERIFY;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Request OTP Response ::" + response.toString());
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS_501) {
                        showVerifyDialog(mContext);
                    } else {
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

    public void callVerfiyEmailApi(String otp) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("action", mContext.getString(R.string.verify_email));
            requestObject.put("email", Utils.getUserEmail(mContext));
            requestObject.put("otp", otp);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_EMAIL_VERIFY;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Verify Email Response ::" + response.toString());

                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS_401) {
                        Utils.setEmailVerified(mContext, true);
                        LogUtils.showToast(mContext, mContext.getString(R.string.congrats_your_account_is_verified_now));
                        iMain.updateVerifyEmailUI();
                    } else {
//                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0));
                    }
                }
                loader.dismiss();
            }

        }, error -> {
            loader.dismiss();
            LogUtils.ERROR("Verify Email Error ::" + error.getMessage());
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Verify Email");
    }

    public void uploadBitmap(final Bitmap bitmapUpload/*, CircleImageView imgUserProfile*/) {
        loader.show();
        LogUtils.DEBUG("Upload URL : " + AppConstant.URL_UPLOAD_USER_PROFILE_PIC);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConstant.URL_UPLOAD_USER_PROFILE_PIC,
                response -> {
                    String uploadResponse = new String(response.data);
                    LogUtils.DEBUG("Upload Profile Pic Response : " + uploadResponse);
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        int status = obj.optInt("status");
                        if (status == AppConstant.SUCCESS) {
                            BitmapUtils.saveProfileImage(mContext, bitmapUpload);
                            iMain.updateProfilePic(bitmapUpload);
                        }
                        Toast.makeText(mContext.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loader.dismiss();
                },
                error -> {
                    Toast.makeText(mContext.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    LogUtils.ERROR("Upload profile pic Error " + error);
                    loader.dismiss();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", Utils.getUserEmail(mContext));
                LogUtils.DEBUG("user_email " + Utils.getUserEmail(mContext));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("user_image", new DataPart(imagename + ".png", BitmapUtils.getByteArrayFromBitmap(bitmapUpload)));
                return params;
            }

        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(mContext).add(volleyMultipartRequest);
    }
}

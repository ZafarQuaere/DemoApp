package com.zaf.econnecto.ui.presenters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
import com.zaf.econnecto.ui.fragments.AddBusinessFragment;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;
import com.zaf.econnecto.ui.fragments.BizListFragment;
import com.zaf.econnecto.ui.fragments.FragmentProfile;
import com.zaf.econnecto.ui.fragments.HelpNFaqFragment;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.operations.IMain;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.storage.AppSharedPrefs;

import org.json.JSONObject;


public class MainPresenter extends BasePresenter {
    Context mContext;
    IMain iMain;

    public MainPresenter(Context context, IMain iLogin) {
        super(context);
        iMain = iLogin;
        mContext = context;
    }


    public void moveToFragment(String fragName) {
        switch (fragName) {
            case "BizCategoryFragment":
                Utils.moveToFragment(mContext, new BizCategoryFragment(), BizCategoryFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, BizCategoryFragment.class.getSimpleName(), mContext.getString(R.string.business_category), null, null);
                break;
            case "AddBusinessFragment":
                Utils.moveToFragment(mContext, new AddBusinessFragment(), AddBusinessFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, AddBusinessFragment.class.getSimpleName(), mContext.getString(R.string.add_business), null, null);
                break;
            case "BizListFragment":
                Utils.moveToFragment(mContext, new BizListFragment(), BizListFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, BizListFragment.class.getSimpleName(), mContext.getString(R.string.business_list), null, null);
                break;
            case "FragmentProfile":
                Utils.moveToFragment(mContext, new FragmentProfile(), FragmentProfile.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, FragmentProfile.class.getSimpleName(), mContext.getString(R.string.my_profile), null, null);
                break;
            case "HelpNFaqFragment":
                Utils.moveToFragment(mContext, new HelpNFaqFragment(), HelpNFaqFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, HelpNFaqFragment.class.getSimpleName(), mContext.getString(R.string.help_faq), null, null);
                break;
        }
        //LogUtils.showToast(mContext, fragName);
    }

    public void logoutUser() {
        LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                mContext.getString(R.string.do_you_really_want_to_logout), new DialogButtonClick() {
                    @Override
                    public void onOkClick() {
                        callLogoutApi();

                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
    }


    public void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, ForgetPswdActivity.class);
        intent.putExtra(AppConstant.COMINGFROM, AppConstant.HOME);
        mContext.startActivity(intent);
    }

    public void updateActionBarTitleOnBackPress(Context mContext, Fragment baseFragment) {
        if (baseFragment.getClass().getSimpleName().contains("Category")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.business_category), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("List")) {
            iMain.showAddBizFab(true);
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.business_list), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Profile")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.my_profile), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Add")) {
            iMain.showAddBizFab(false);
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.add_business), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("AddressData")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.save_address), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Help")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.help_faq), null, null);
        }
    }


    private void callLogoutApi() {
        String url = AppConstant.URL_BASE + AppConstant.URL_LOGOUT;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" /*+ requestObject.toString()*/);
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Logout Response ::" + response.toString());
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
                        prefs.clear(mContext.getString(R.string.key_logged_in));
                        Utils.setLoggedIn(mContext, false);
                        // mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        LogUtils.showToast(mContext, mContext.getString(R.string.you_are_sucessfully_logout));
                        iMain.onLogoutCall();
                        // ((Activity) mContext).finish();
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.DEBUG("Logout Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Logout");
    }

}

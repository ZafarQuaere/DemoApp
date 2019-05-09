package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
import com.zaf.econnecto.ui.activities.LoginActivity;
import com.zaf.econnecto.ui.fragments.FragmentProfile;
import com.zaf.econnecto.ui.fragments.HelpNFaqFragment;
import com.zaf.econnecto.ui.fragments.HomeFragment;
import com.zaf.econnecto.ui.fragments.ListingsFragment;
import com.zaf.econnecto.ui.fragments.OrdersFragment;
import com.zaf.econnecto.ui.fragments.PaymentsFragment;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.operations.IMain;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.storage.AppSharedPrefs;


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
            case "HomeFragment":
                Utils.moveToFragment(mContext, new HomeFragment(), HomeFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext,HomeFragment.class.getSimpleName(),mContext.getString(R.string.home),null,null);
                break;
            case "OrdersFragment":
                Utils.moveToFragment(mContext, new OrdersFragment(), OrdersFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext,OrdersFragment.class.getSimpleName(),mContext.getString(R.string.orders),null,null);
                break;
            case "ListingsFragment":
                Utils.moveToFragment(mContext, new ListingsFragment(), ListingsFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext,ListingsFragment.class.getSimpleName(),mContext.getString(R.string.item_list),null,null);
                break;
            case "PaymentsFragment":
                Utils.moveToFragment(mContext, new PaymentsFragment(), PaymentsFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext,PaymentsFragment.class.getSimpleName(),mContext.getString(R.string.payments),null,null);
                break;
            case "FragmentProfile":
                Utils.moveToFragment(mContext, new FragmentProfile(), FragmentProfile.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, FragmentProfile.class.getSimpleName(),mContext.getString(R.string.my_profile),null,null);
                break;
            case "HelpNFaqFragment":
                Utils.moveToFragment(mContext, new HelpNFaqFragment(), HelpNFaqFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, HelpNFaqFragment.class.getSimpleName(),mContext.getString(R.string.help_faq),null,null);
                break;
        }
        //LogUtils.showToast(mContext, fragName);
    }

    public void logoutUser() {
        LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                mContext.getString(R.string.do_you_really_want_to_logout), new DialogButtonClick() {
                    @Override
                    public void onOkClick() {
                        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
                        prefs.clear(mContext.getString(R.string.key_logged_in));
                        Utils.setLoggedIn(mContext, false);
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        LogUtils.showToast(mContext, mContext.getString(R.string.you_are_sucessfully_logout));
                        ((Activity) mContext).finish();
                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
    }

    public void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, ForgetPswdActivity.class);
        intent.putExtra(AppConstant.COMINGFROM,AppConstant.HOME);
        mContext.startActivity(intent);
    }

    public void updateActionBarTitleOnBackPress(Context mContext, Fragment baseFragment) {
        if (baseFragment.getClass().getSimpleName().contains("Home")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.home), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Listing")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.listings), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Profile")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.my_profile), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Orders")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.orders), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("Payments")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.payments), null, null);
        } else if (baseFragment.getClass().getSimpleName().contains("AddressData")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.save_address), null, null);
        }else if (baseFragment.getClass().getSimpleName().contains("Help")) {
            Utils.updateActionBar(mContext, baseFragment.getClass().getSimpleName(), mContext.getString(R.string.help_faq), null, null);
        }
    }
}

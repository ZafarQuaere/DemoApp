package com.zaf.econnecto.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zaf.econnecto.BuildConfig;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.login.LoginPojo;
import com.zaf.econnecto.ui.activities.ViewBizDetailsActivity;
import com.zaf.econnecto.ui.activities.EnterNewPswdActivity;
import com.zaf.econnecto.ui.activities.EnterOTPActivity;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
import com.zaf.econnecto.ui.activities.LoginActivity;
import com.zaf.econnecto.ui.fragments.AddBusinessFragment;
import com.zaf.econnecto.ui.fragments.BListFragment;
import com.zaf.econnecto.ui.fragments.FragmentProfile;
import com.zaf.econnecto.ui.fragments.HomeFragment;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.utils.parser.ParseManager;
import com.zaf.econnecto.utils.storage.AppSharedPrefs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;


public class Utils {


    public static boolean isValidMobileNumber(String mobileNo) {
        return Patterns.PHONE.matcher(mobileNo)
                .matches();
    }

    public static boolean isValidEmail(String email) {
        /*String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();*/
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(AppConstant.TIMESTAMP_FORMAT, Locale.US).format(new Date());
    }

    public static void setLoggedIn(Context mContext, boolean b) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_logged_in), b);
    }

    public static boolean isLoggedIn(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        boolean isLogIn = false;
        if (prefs.get(context.getString(R.string.key_logged_in)) != null) {
            isLogIn = (boolean) prefs.get(context.getString(R.string.key_logged_in));
        }
        return isLogIn;
    }

    public static void moveToFragment(Context context, Fragment fragment, String fragName, Object data) {
        LogUtils.DEBUG("moveToFragment() called : " + fragment.getClass().getSimpleName());
        if (context == null || fragment == null)
            return;

        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
        // ((Activity)context).getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.lytMain, fragment, fragment.getClass().getSimpleName());

        //if data is also transferring
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle.putSerializable(context.getString(R.string.key_serializable), (Serializable) data);
        }
        fragment.setArguments(bundle);

        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();

    }

    public static byte[] decompress(byte[] str) throws IOException, UnsupportedEncodingException {
        if (str == null || str.length == 0) {
            return str;
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder outStr = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            outStr.append(line);
        }
        return new String(outStr).getBytes();
    }

    public static void updateActionBar(final Context activity, final String className,
                                       String dynamicTitle, Object customHeaderData, final DialogButtonClick actionBarItemClickListener) {

        if (activity == null)
            return;

        LogUtils.DEBUG(AppConstant.TAG + " Utils >> updateActionBar() called : " + className + "/" + dynamicTitle);

        RelativeLayout toolbarLayout = (RelativeLayout) ((Activity) activity).findViewById(R.id.lytToolbar);
        TextView textTitle = (TextView) toolbarLayout.findViewById(R.id.textTitle);
        TextView textBack = (TextView) toolbarLayout.findViewById(R.id.textBack);
        ImageView imgActionBarDrawerIcon = (ImageView) toolbarLayout.findViewById(R.id.imgActionBarDrawerIcon);

        textBack.setVisibility(View.GONE);
        textTitle.setText(dynamicTitle);
        if (className.equals(new LoginActivity().getClass().getSimpleName())) {

        } else if (className.equals(new EnterOTPActivity().getClass().getSimpleName())) {
            textBack.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new EnterNewPswdActivity().getClass().getSimpleName())) {
            textBack.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new ViewBizDetailsActivity().getClass().getSimpleName())) {
            textBack.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new HomeFragment().getClass().getSimpleName())) {
            textBack.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
        } else if (className.equals(new AddBusinessFragment().getClass().getSimpleName())) {
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new BListFragment().getClass().getSimpleName())) {
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new FragmentProfile().getClass().getSimpleName())) {
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        }  else if (className.equals(new ForgetPswdActivity().getClass().getSimpleName())) {
            textBack.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        }
    }

    public static void clearBackStackTillHomeFragment(Activity activity) {

        LogUtils.DEBUG("Utils >> clearBackStackTillHomeFragment() >> activity : " + activity);
        if (activity == null) {
            return;
        }
        FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();

        for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
            String fragmentName = (fm.getBackStackEntryAt(i)).getName();
            if (!fragmentName.equals(new HomeFragment().getClass().getName())) {
                fm.popBackStack();
                LogUtils.DEBUG("Utils >> clearBackStackTillHomeFragment() >> removed fragment : " + fragmentName);
            } else {
                break;
            }
        }
        Utils.updateActionBar(activity, HomeFragment.class.getSimpleName(), activity.getString(R.string.home), null, null);
        // updateBottomBar(activity, new HomeFragment().getClass().getSimpleName());
    }

    public static void saveOTPData(Context mContext, String data) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_otp_data), data);
    }

    public static String getOTPData(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_otp_data));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
        return data;

    }

    public static void setMobileNo(Context mContext, String mobile) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_mobile_no), mobile);

    }

    public static String getMobileNo(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_mobile_no));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
        return data;
    }

    public static void shareApp(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "AquaHey Seller");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.select_app_to_share)));
        } catch (Exception e) {
            e.printStackTrace();
            //e.toString();
        }
    }

    public static Address getlocationfromaddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address = null;
        //  GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address == null)
            return null;

        Address location = address.get(0);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();


        // return "Latitude : " +latitude + "  Longitude : " + longitude;
        return location;
    }

    public static String getUserId(Context mContext) {
        //return "1";
        String loginStringData = getLoginData(mContext);
        String dealerId = "";
        LoginPojo loginData = ParseManager.getInstance().fromJSON(loginStringData,LoginPojo.class);
        if (loginData != null ){
            return loginData.getData().getId();
        }
        return dealerId;
    }

    public static String getDealerId(Context mContext) {
        //return "111";
       String loginStringData = getLoginData(mContext);
        String dealerId = "";
        LoginPojo loginData = ParseManager.getInstance().fromJSON(loginStringData,LoginPojo.class);
        if (loginData != null ){
            return loginData.getData().getDealerId();
        }
        return dealerId;
    }

    public static void setFirstTimeLaunch(Context mContext,boolean isFirstTime) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_first_time_launch), isFirstTime);
    }

    public static boolean isFirstTimeLaunch(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        boolean firstTimeLaunch = true;
        try {
            firstTimeLaunch = (boolean)prefs.get(context.getString(R.string.key_first_time_launch));
        } catch (Exception e) {
           return firstTimeLaunch;
        }
        return firstTimeLaunch;
    }

    public static void saveLoginData(Context mContext, String data) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_login_data), data);
    }

    public static String getLoginData(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_login_data));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
        return data;
    }

    public static void saveNewOrderData(Context mContext, String newOrderData) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_new_order_data), newOrderData);
    }

    public static String getNewOrderData(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_new_order_data));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
        return data;
    }

    public static void savePendingOrderData(Context mContext, String pendingOrderData) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_pending_order_data), pendingOrderData);
    }

    public static String getPendingOrderData(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_pending_order_data));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
        return data;
    }

    public static void saveCompleteOrderData(Context mContext, String completeOrderData) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_complete_order_data), completeOrderData);
    }

    public static String getCompleteOrderData(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_complete_order_data));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
        return data;
    }
}

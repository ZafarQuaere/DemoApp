package com.zaf.econnecto.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zaf.econnecto.BuildConfig;
import com.zaf.econnecto.R;
import com.zaf.econnecto.model.CategoryData;
import com.zaf.econnecto.model.CategoryListData;
import com.zaf.econnecto.network_call.response_model.login.LoginData;
import com.zaf.econnecto.ui.activities.BizDetailsActivity;
import com.zaf.econnecto.ui.activities.ChangePswdActivity;
import com.zaf.econnecto.ui.activities.EnterNewPswdActivity;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
import com.zaf.econnecto.ui.activities.LoginActivity;
import com.zaf.econnecto.ui.activities.MyBusinessActivity;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;
import com.zaf.econnecto.ui.fragments.BizListFragment;
import com.zaf.econnecto.ui.fragments.FragmentProfile;
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen1Fragment;
import com.zaf.econnecto.ui.interfaces.ActionBarItemClick;
import com.zaf.econnecto.utils.parser.ParseManager;
import com.zaf.econnecto.utils.storage.AppSharedPrefs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class Utils {

    public static final int USER_PROFILE_IMG = 100;

    public static boolean isValidEmail(String email) {
        /*String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();*/
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidMobileNumber(String mobileNo) {
        return Patterns.PHONE.matcher(mobileNo)
                .matches();
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

    public static void setEmailVerified(Context mContext, boolean b) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_email_verified), b);
    }

    public static boolean isEmailVerified(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        boolean isLogIn = false;
        if (prefs.get(context.getString(R.string.key_email_verified)) != null) {
            isLogIn = (boolean) prefs.get(context.getString(R.string.key_email_verified));
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
                                       String dynamicTitle, Object customHeaderData, final ActionBarItemClick actionBarListener) {

        if (activity == null)
            return;

        LogUtils.DEBUG(AppConstant.TAG + " Utils >> updateActionBar() called : " + className + "/" + dynamicTitle);

        RelativeLayout toolbarLayout = (RelativeLayout) ((Activity) activity).findViewById(R.id.lytToolbar);
        final RelativeLayout rlytSearch = (RelativeLayout) toolbarLayout.findViewById(R.id.rlytSearch);
        final TextView textTitle = (TextView) toolbarLayout.findViewById(R.id.textTitle);
        final TextView textBack = (TextView) toolbarLayout.findViewById(R.id.textBack);
        final TextView txtSearch = (TextView) toolbarLayout.findViewById(R.id.txtSearch);
        final TextView txtSearchBack = (TextView) toolbarLayout.findViewById(R.id.txtSearchBack);
        final TextView txtSearchClear = (TextView) toolbarLayout.findViewById(R.id.txtSearchClear);
        final EditText editSearch = (EditText) toolbarLayout.findViewById(R.id.editSearch);
        final ImageView imgActionBarDrawerIcon = (ImageView) toolbarLayout.findViewById(R.id.imgActionBarDrawerIcon);

        textBack.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);
        rlytSearch.setVisibility(View.GONE);
      //  txtSearchBack.setVisibility(View.GONE);

        textTitle.setText(dynamicTitle);
        if (className.equals(new LoginActivity().getClass().getSimpleName())) {
            textBack.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new ChangePswdActivity().getClass().getSimpleName())) {
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
        } else if (className.equals(new BizDetailsActivity().getClass().getSimpleName())) {
            textBack.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new MyBusinessActivity().getClass().getSimpleName())) {
            textBack.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
        } else if (className.equals(new BizCategoryFragment().getClass().getSimpleName())) {
            textBack.setVisibility(View.GONE);
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
        }  else if (className.equals(new AddBizScreen1Fragment().getClass().getSimpleName())) {
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(v -> ((Activity) activity).onBackPressed());


        } else if (className.equals(new BizListFragment().getClass().getSimpleName())) {
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
            txtSearch.setVisibility(View.VISIBLE);
            txtSearchBack.bringToFront();
            txtSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlytSearch.setVisibility(View.VISIBLE);
                    imgActionBarDrawerIcon.setVisibility(View.GONE);
                    textBack.setVisibility(View.GONE);
                    textTitle.setVisibility(View.GONE);
                    txtSearch.setVisibility(View.GONE);
                    txtSearchClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editSearch.setText("");
                            actionBarListener.clearSearch();
                        }
                    });
                }
            });
            txtSearchBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlytSearch.setVisibility(View.GONE);
                    imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
                    textTitle.setVisibility(View.VISIBLE);
                    txtSearch.setVisibility(View.VISIBLE);
                }
            });
            editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null && !s.toString().isEmpty() && actionBarListener!= null)
                        actionBarListener.afterTextChanged(s);
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

    public static void clearBackStackTillHomeFragment(Context activity) {

        LogUtils.DEBUG("Utils >> clearBackStackTillHomeFragment() >> activity : " + activity);
        if (activity == null) {
            return;
        }
        FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();

        for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
            String fragmentName = (fm.getBackStackEntryAt(i)).getName();
            if (!fragmentName.equals(new BizCategoryFragment().getClass().getName())) {
                fm.popBackStack();
                LogUtils.DEBUG("Utils >> clearBackStackTillHomeFragment() >> removed fragment : " + fragmentName);
            } else {
                break;
            }
        }
        Utils.updateActionBar(activity, BizCategoryFragment.class.getSimpleName(), activity.getString(R.string.business_list), null, null);
        //updateBottomBar(activity, new BizCategoryFragment().getClass().getSimpleName());
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
            LogUtils.ERROR(e.getMessage());
            return data;
        }
        return data;
    }

    public static void shareApp(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "eConnecto");
            String shareMessage = activity.getString(R.string.share_app_content);
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.select_app_to_share)));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
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
            LogUtils.ERROR(e.getMessage());
        }
        if (address == null)
            return null;

        Address location = address.get(0);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        // return "Latitude : " +latitude + "  Longitude : " + longitude;
        return location;
    }

    public static String getUserEmail(Context mContext) {
        String loginStringData = getLoginData(mContext);
        String userEmail = "";
        LoginData loginData = ParseManager.getInstance().fromJSON(loginStringData, LoginData.class);
        if (loginData != null ){
            userEmail = loginData.getData().getEmail();
        }
        return userEmail;
    }


    public static String getUserID(Context mContext) {
        String loginStringData = getLoginData(mContext);
        String userID = "";
        LoginData loginData = ParseManager.getInstance().fromJSON(loginStringData, LoginData.class);
        if (loginData != null) {
           userID = loginData.getData().getId();
        }
        return  userID;
    }

    public static String getUserName(Context mContext) {
        //return "111";
        String loginStringData = getLoginData(mContext);
        String username = "";
        LoginData loginData = ParseManager.getInstance().fromJSON(loginStringData, LoginData.class);
        if (loginData != null) {
            return loginData.getData().getEmail();
        }
        return username;
    }

    public static String getProfilePic(Context mContext) {
        //return "111";
        String loginStringData = getLoginData(mContext);
        String profilePic = "";
        LoginData loginData = ParseManager.getInstance().fromJSON(loginStringData, LoginData.class);
        if (loginData != null) {
            return loginData.getData().getProfilePic();
        }
        return profilePic;
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

    public static String getNewOrderData(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_new_order_data));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
            return data;
        }
        return data;
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
            LogUtils.ERROR(e.getMessage());
            return data;
        }
        return data;
    }


    public static String getAccessToken(Context context) {
        String loginStringData = getLoginData(context);
        String accessToken = "";
        LoginData loginData = ParseManager.getInstance().fromJSON(loginStringData, LoginData.class);
        if (loginData != null) {
            accessToken = loginData.getData().getJWTToken();
        }

        return accessToken;
    }

    public static void setBusinessStatus(Context mContext, String bizStatus) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_business_status), bizStatus);
    }

    public static String getBusinessStatus(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_business_status));
            if (data.isEmpty())
                data ="0";
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
            return "0";
        }
        return data;
    }

    public static void callPhone(Context mContext, String phone1) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone1, null));
            mContext.startActivity(intent);
        } catch (Exception e) {
            LogUtils.ERROR(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void openWhatsApp(Context mContext, String phone) {
        String url = "https://api.whatsapp.com/send?phone="+"+91"+phone;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        mContext.startActivity(i);
    }

    public static void openMsgInbox(Context mContext, String phone ){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phone);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("sms_body", "write your text");
        mContext.startActivity(smsIntent);
    }

    public static void saveProfileImage(Context mContext,String profilePic) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_user_profile_pic), profilePic);
    }

    public static String getUserProfilePic(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String data = "";
        try {
            data = (String) prefs.get(context.getString(R.string.key_user_profile_pic));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
            return data;
        }
        return data;
    }

    public static void openInPlayStore(Activity activity) {
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            Uri uri = Uri.parse("market://details?id=" + appPackageName);
            activity.startActivity(new Intent(Intent.ACTION_VIEW,uri));
        } catch (android.content.ActivityNotFoundException anfe) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    public static List<CategoryListData> readDataFromFile(@Nullable Activity activity) {
        List<CategoryListData> listData = null;
        try {
            JSONObject category = new JSONObject(FileUtil.loadJSONFromAsset(activity, "category"));
            CategoryData data = ParseManager.getInstance().fromJSON(category, CategoryData.class);
            listData = data.getData();
            //LogUtils.DEBUG(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> item1 : "+data.getData().get(0).getCategoryName()+" item2 "+data.getData().get(1).getCategoryName());
        } catch (JSONException e) {
            e.printStackTrace();
            return listData;
        } catch (IOException e) {
            e.printStackTrace();
            return listData;
        }
        return listData;
    }

    public static void clearLoginDatas(Context mContext) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.clear(mContext.getString(R.string.key_logged_in));
        prefs.clear(mContext.getString(R.string.key_login_data));
        prefs.clear(mContext.getString(R.string.key_login_date));
        Utils.setLoggedIn(mContext, false);
        Utils.setBusinessStatus(mContext,"0");
        Utils.setEmailVerified(mContext, false);
    }

    public static void applyDisableTime(View btnTestforTime) {
        btnTestforTime.setEnabled(false);
        btnTestforTime.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnTestforTime.setEnabled(true);
            }
        }, AppConstant.RESEND_OTP_TIME);
    }

    public static void storeProfileImage(Activity mContext,LoginData loginData) {
        Utils.saveProfileImage(mContext,loginData.getData().getProfilePic());
        try {
            Bitmap bitmap = BitmapUtils.getBitmap(mContext,  Uri.parse(loginData.getData().getProfilePic()));
            BitmapUtils.saveProfileImage(mContext,bitmap);
        } catch(Exception e) {
            LogUtils.ERROR(e.getMessage());
        }
    }

    public static boolean isValidPassword(String password) {
        int size = password.length();
        int pos_lastLetter = size - 1;
        if (password.length() > 5 && password.length() < 20) {
            if (Character.isDigit(password.charAt(0)) || Character.isLetter(password.charAt(0))) {
                if (Character.isDigit(password.charAt(pos_lastLetter)) || Character.isLetter(password.charAt(pos_lastLetter))) {
                    for (int i = 0; i < password.length(); i++) {
                        if (Character.isLetter(password.charAt(i)) || Character.isDigit(password.charAt(i)) || password.charAt(i) == '@' || password.charAt(i) == '.' || password.charAt(i) == '_' || password.charAt(i) == '-') {

                        } else {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void openMail(@Nullable Context mContext, @NotNull String mail) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto: "+mail));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        if (mContext != null) {
            mContext.startActivity(intent);
        }
    }
}

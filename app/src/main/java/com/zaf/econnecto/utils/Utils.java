package com.zaf.econnecto.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zaf.econnecto.BuildConfig;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.login.LoginData;
import com.zaf.econnecto.ui.activities.BizDetailsActivity;
import com.zaf.econnecto.ui.activities.ChangePswdActivity;
import com.zaf.econnecto.ui.activities.EnterNewPswdActivity;
import com.zaf.econnecto.ui.activities.ForgetPswdActivity;
import com.zaf.econnecto.ui.activities.LoginActivity;
import com.zaf.econnecto.ui.activities.MyBusinessActivity;
import com.zaf.econnecto.ui.fragments.AddBusinessFragment;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;
import com.zaf.econnecto.ui.fragments.BizListFragment;
import com.zaf.econnecto.ui.fragments.FragmentProfile;
import com.zaf.econnecto.ui.interfaces.ActionBarItemClick;
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
        } else if (className.equals(new AddBusinessFragment().getClass().getSimpleName())) {
            imgActionBarDrawerIcon.setVisibility(View.VISIBLE);
            textBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) activity).onBackPressed();
                }
            });
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
                    if (s != null && !s.toString().isEmpty())
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
        // updateBottomBar(activity, new BizCategoryFragment().getClass().getSimpleName());
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
            LogUtils.ERROR(e.getMessage());
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
            LogUtils.ERROR(e.getMessage());
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
            LogUtils.ERROR(e.getMessage());
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
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        String data = "";
        try {
            data = (String) prefs.get(mContext.getString(R.string.key_user_email));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
            return data;
        }
        return data;
    }
    public static void saveUserEmail(Context mContext, String newOrderData) {
        if (mContext == null)
            return;
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_user_email), newOrderData);
    }

    public static String getUserName(Context mContext) {
        //return "111";
        String loginStringData = getLoginData(mContext);
        String username = "";
        LoginData loginData = ParseManager.getInstance().fromJSON(loginStringData, LoginData.class);
        if (loginData != null) {
            return loginData.getData().getUsername();
        }
        return username;
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
            LogUtils.ERROR(e.getMessage());
            return data;
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

    public static Bitmap getBitmap(Context mContext,Intent data,Uri selectedImageUri) {
        String[] FILE = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(selectedImageUri,FILE, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(FILE[0]);
        String decodedImage = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bitmap = BitmapFactory.decodeFile(decodedImage);
        return bitmap;
    }

}

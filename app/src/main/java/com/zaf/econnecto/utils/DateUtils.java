package com.zaf.econnecto.utils;

import android.content.Context;

import com.zaf.econnecto.R;
import com.zaf.econnecto.utils.storage.AppSharedPrefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static SimpleDateFormat myFormat;

    static {
        myFormat = new SimpleDateFormat("dd MM yyyy");
    }

    public static void differenceInDate() {
        String loginDate = "31 01 2020";
        //String currentDate = "02 02 2014";
        Date date = new Date();
        String currentDate = myFormat.format(date);
        System.out.println("Current date : " + currentDate + "  loginDate: " + loginDate);
        try {
            Date pLoginDate = myFormat.parse(loginDate);
            long difference = date.getTime() - pLoginDate.getTime();
            float daysBetween = (difference / (1000 * 60 * 60 * 24));
            int days = (int) daysBetween;
            System.out.println("Difference in days : " + days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(AppConstant.TIMESTAMP_FORMAT, Locale.US).format(new Date());
    }

    public static void setLoginDate(Context mContext) {
        if (mContext == null)
            return;
        Date date = new Date();
        String loginDate = myFormat.format(date);
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(mContext);
        prefs.put(mContext.getString(R.string.key_login_date), loginDate);
    }

    public static String getLoginDate(Context context) {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(context);
        String date = "";
        try {
            date = (String) prefs.get(context.getString(R.string.key_login_date));
            if (date == null) {
                Date currentDate = new Date();
                date = myFormat.format(currentDate);
            }
        } catch (Exception e) {
            return date;
        }
        return date;
    }

    public static boolean isLoginExpired(Context mContext) {
        String loginDate = getLoginDate(mContext);
        //String loginDate = "31 01 2020";
        Date currentDate = new Date();
        String cDate = myFormat.format(currentDate);
        try {
            Date lDate = myFormat.parse(loginDate);
            long difference = currentDate.getTime() - lDate.getTime();
            int day = (int) (difference / (1000 * 60 * 60 * 24));
            LogUtils.DEBUG("loginDate :" + loginDate + "  currentDate :" + cDate + " difference in days: " + day);
            if (day < 30)
                return false;
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package com.zaf.econnecto.utils;

import com.zaf.econnecto.BuildConfig;

public class AppConstant {

    public static final Integer SUCCESS = 1;
    public static final Integer FAILURE = 0;
    public static final Integer FOLLOWING = 1;
    public static final String COMINGFROM = "ComingFrom";
    public static final int LOGIN = 1;
    public static final int HOME = 2;
    public static  boolean MOVE_TO_ADD_BIZ = false;
    public static  boolean NEW_FOLLOW = false;
    // Splash screen timer
    public static int SPLASH_TIME_OUT = 1500;
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String TAG = "e-Connecto";
    public static final String RUPEES_SYMBOL = "₹ ";


    public static final String URL_BASE = BuildConfig.BASE_URL;  //"http://econnecto.com/api/";
    public static final String URL_REGISTER = "register.php";
    public static final String URL_LOGIN = "login.php";
    public static final String URL_BIZ_LIST = "business_list.php?email="; //(brandname,banner_image)
    public static final String URL_BIZ_DETAIL = "single_business.php?business_uid="; //(brandname,banner_image)
    public static final String URL_FOLLOW = "follow.php";
    public static final String URL_LOGOUT = "logout.php";
    public static final String URL_FORGOT_PSWD = "forgot_pwd.php";
    public static final String URL_EMAIL_VERIFY = "email_verification.php";
    public static final String URL_ADD_BUSINESS = "add_business.php";
    public static final String URL_MY_BUSINESS = "mybusiness.php";
    public static final String URL_UPDATE_BUSINESS = "update_business.php";
    public static final String URL_BIZ_CATEGORY = URL_BASE+"categories.php";
    public static final String URL_UPLOAD_USER_PROFILE_PIC = "http://econnecto.com/api/user_profile_pic.php";
    public static final String URL_UPLOAD_BUSINESS_PROFILE_PIC = "http://econnecto.com/api/business_profile_pic.php";
    public static final String URL_UPLOAD_BUSINESS_BANNER_PIC = "http://econnecto.com/api/business_banner_pic.php";

    public static final String URL_MY_BUSINESS1 = "\n" +
            "http://econnecto.com/api/business_profile_pic.php" +
            ": http://econnecto.com/api/business_banner_pic.php";


    public static final String URL_DEALER_ADDRESS = "/insert-dealear-address"; //(brandname,banner_image)

    public static final String URL_VERIFY_MOBILE = "/mobile-verify?mobile=";
    public static final String URL_OTP_SERVICE = "/otp_service?mobile=";
    public static final String URL_CHANGE_PASSWORD = "/change-password?mobile=";
    public static final String URL_CP_PASSWORD = "&password=";



    //http://econnecto.com/api/business_list.php
    public static final String baseUrl = "https://reqres.in";
    public static final String listUrl = "/api/users?page=";
    public static final String userByIdUrl = "/api/users/2";
    public static final String registerUrl = "/api/register";
    public static final String loginUrl = "/api/login";


}

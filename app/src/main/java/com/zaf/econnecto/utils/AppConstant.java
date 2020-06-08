package com.zaf.econnecto.utils;

import android.graphics.Bitmap;

import com.zaf.econnecto.BuildConfig;

import org.jetbrains.annotations.NotNull;

public class AppConstant {

    public static final Integer SUCCESS_501 = 501;
    public static final Integer SUCCESS_401 = 401;
    public static final Integer PHONE_NOT_VERIFIED = 400;
    public static final Integer SUCCESS = 1;
    public static final Integer FAILURE = 0;
    public static final Integer FOLLOWING = 1;
    public static final String COMINGFROM = "ComingFrom";
    public static final int LOGIN = 1;
    public static final int HOME = 2;
    public static  boolean MOVE_TO_ADD_BIZ = false;
    public static  boolean NEW_FOLLOW = false;
    public static  boolean BIZNESS_ADDED = false;
    // Splash screen timer
    public static int SPLASH_TIME_OUT = 2100;
    public static int RESEND_OTP_TIME = 40000;
    public static int OTP_TIME = 120000;
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String TAG = "e-Connecto";
    public static final String RUPEES_SYMBOL = "₹ ";


    public static final String URL_BASE_MVP = "http://econnecto.com/andMVP/api/";
    public static final String URL_BASE = "http://econnecto.com/api/";
    public static final String URL_PINCODE = "https://pincode.saratchandra.in/api/pincode/";
    public static final String URL_REGISTER = "register.php";
    public static final String URL_LOGIN = "login.php";
    public static final String URL_LOGIN_MVP = "user_login/login.php";
    public static final String URL_BIZ_LIST = "business_list/business_list.php?id="; //(brandname,banner_image)
    public static final String URL_BIZ_DETAIL = "single_business.php?business_uid="; //(brandname,banner_image)
    public static final String URL_FOLLOW = "follow_unfollow/follow_unfollow.php";
    public static final String URL_ADD_IMAGE = URL_BASE_MVP+"images_api/add_image.php";
    public static final String URL_LOGOUT = "logout.php";
    public static final String URL_FORGOT_PSWD = "forgot_password/forgot_password.php";
    public static final String URL_CHANGE_PSWD = "change_password/change_password.php";
//    public static final String URL_EMAIL_VERIFY = "email_verification.php";
    public static final String URL_EMAIL_VERIFY = "email_verification/email_verification.php";
    public static final String URL_ADD_BUSINESS = "add_business.php";
    public static final String URL_MY_BUSINESS = "mybusiness/mybusiness.php?id=";
    public static final String URL_UPDATE_BUSINESS = "update_business.php";
    public static final String URL_BIZ_CATEGORY = URL_BASE+"categories.php";
    public static final String URL_UPLOAD_USER_PROFILE_PIC = URL_BASE+"user_profile_pic.php";
    public static final String URL_UPLOAD_BUSINESS_PROFILE_PIC = URL_BASE+"business_profile_pic.php";
    public static final String URL_UPLOAD_BUSINESS_BANNER_PIC = URL_BASE+"business_banner_pic.php";
    public static final String URL_DEAL_BACKGROUND = URL_BASE+"deal_backgrounds.php";
    public static final String URL_ADD_DEAL = URL_BASE+"add_deal.php";


    public static final String URL_VERIFY_MOBILE = "/mobile-verify?mobile=";
    public static final String URL_OTP_SERVICE = "/otp_service?mobile=";
    public static final String URL_VIEW_IMAGES = URL_BASE_MVP+"images_api/view_images.php?business_id=39";
    public static final String URL_CHANGE_PASSWORD = "/change-password?mobile=";
    public static final String URL_FIND_MY_EMAIL = "find_email/find_email.php";
    public static final String URL_CP_PASSWORD = "&password=";

    //Crop constants
    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;
    public static final int TABS_COUNT = 3;
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;

    //http://econnecto.com/api/business_list.php
    public static final String baseUrl = "https://reqres.in";
    public static final String listUrl = "/api/users?page=";
    public static final String userByIdUrl = "/api/users/2";
    public static final String registerUrl = "/api/register";
    public static final String loginUrl = "/api/login";


    @NotNull
    public static final String URL_TERMS_CONDITIONS = "http://www.econnecto.com/terms_and_conditions.php";
}

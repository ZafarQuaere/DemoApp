package com.zaf.econnecto.utils;

public class AppConstant {

    public static final Integer SUCCESS = 1;
    public static final Integer FAILURE = 0;
    public static final String COMINGFROM = "ComingFrom";
    public static final int LOGIN = 1;
    public static final int HOME = 2;
    // Splash screen timer
    public static int SPLASH_TIME_OUT = 2000;
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String TAG = "e-Connecto";
    public static final String RUPEES_SYMBOL = "₹ ";
    public static final String ORDER_STATUS_NEW = "2";
    public static final String ORDER_STATUS_COMPLETED = "1";
    public static final String ORDER_STATUS_PENDING = "0";


    public static final String URL_BASE = "http://econnecto.com/api/";
    public static final String URL_REGISTER = "register.php";
    public static final String URL_LOGIN = "login.php";
    public static final String URL_BIZ_LIST = "business_list.php"; //(brandname,banner_image)
    public static final String URL_DEALER_ADDRESS = "/insert-dealear-address"; //(brandname,banner_image)
    public static final String URL_INSERT_DELAER = "/insert-dealer"; //(dName,mobile,brandIds,rating,addressId)
    public static final String URL_INSERT_PRODUCT = "/insert-product"; //{pName,imagePath,dealerId,name,price,productType,isBrand}
    public static final String URL_VERIFY_MOBILE = "/mobile-verify?mobile=";
    public static final String URL_OTP_SERVICE = "/otp_service?mobile=";
    public static final String URL_CHANGE_PASSWORD = "/change-password?mobile=";
    public static final String URL_CP_PASSWORD = "&password=";

    public static final String URL_ORDERS = "/new-orders?dealerId=";
    public static final String URL_ORDER_STATUS = "&status=";

    public static final String URL_PRODUCT_LIST = "/product?dealerId=";
    public static final String URL_TODAY_SALES = "/today-sales?dealerId=";
    public static final String URL_TOTAL_SALES = "/total-sales?dealerId=";

    //http://econnecto.com/api/business_list.php
    public static final String baseUrl = "https://reqres.in";
    public static final String listUrl = "/api/users?page=";
    public static final String userByIdUrl = "/api/users/2";
    public static final String registerUrl = "/api/register";
    public static final String loginUrl = "/api/login";

}

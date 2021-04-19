package com.zaf.econnecto.service

import com.google.gson.JsonObject
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.utils.AppConstant
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EConnectoServices {

    @GET("business_list/business_list.php")
    fun getBusinessList(@Query("id") id: String): Call<JsonObject>

    @POST(AppConstant.URL_OTHER_BIZ_BASIC_DETAILS)
    fun getOtherBizBasicDetails(@Body requestBody: RequestBody): Call<JsonObject>

    @POST("user_registration/register.php")
    fun registerUser(@Body requestBody: RequestBody): Call<JsonObject>

    @POST("user_registration/phone_verification.php")
    fun phoneVerification(@Body requestBody: RequestBody): Call<JsonObject>

    @POST("add_business/add_business.php")
    fun addYourBusiness(@Body requestBody: RequestBody): Call<JsonObject>

    @POST("user_login/login.php")
    fun login(@Body requestBody: RequestBody): Call<JsonObject>


    @POST(AppConstant.URL_BIZ_BASIC_DETAILS)
    fun bizBasicDetails(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_BIZ_ADD_PROD_SERVICES)
    fun addProductServices(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_BIZ_REMOVE_PROD_SERVICES)
    fun removeProductServices(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_BIZ_REMOVE_PROD_SERVICES)
    fun addBrochure(@Body requestBody: RequestBody): Call<JsonObject>

    //all amenity list
    @GET(AppConstant.URL_BIZ_ALL_AMENITY_LIST)
    fun allAmenityList(): Call<AllAmenityList>

    //Add amenity List
    @POST(AppConstant.URL_BIZ_ADD_AMENITIES)
    fun addAmenity(@Body requestBody: RequestBody): Call<JsonObject>

    //Add amenity List
    @POST(AppConstant.URL_BIZ_REMOVE_AMENITIES)
    fun removeAmenity(@Body requestBody: RequestBody): Call<JsonObject>

    //Users Amenity List
    @GET(AppConstant.URL_BIZ_AMENITY_LIST)
    fun bizAmenityList(@Query("business_id") businessId: String): Call<Amenities>

    @GET(AppConstant.URL_BIZ_IMAGES)
    fun bizImageList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_OPERATING_HOURS)
    fun bizOperatingHours(@Query("business_id") businessId: String): Call<OPHours>

    @GET(AppConstant.URL_BIZ_BROCHURE_LIST)
    fun bizBrochureList(@Query("business_id") businessId: String): Call<Brochure>

    @GET(AppConstant.URL_BIZ_PRODUCT_SERVICES_LIST)
    fun bizProductServicesList(@Query("business_id") businessId: String): Call<ProductNService>

    @GET(AppConstant.URL_BIZ_PRICING)
    fun bizPricingList(@Query("business_id") businessId: String): Call<Pricing>

    @GET(AppConstant.URL_BIZ_CATEGORIES)
    fun bizCategoryList(@Query("business_id") businessId: String): Call<UserCategories>

    //business using payment list
    @GET(AppConstant.URL_BIZ_PAYMENT_LIST)
    fun bizPaymentList(@Query("business_id") businessId: String): Call<PaymentMethods>

    //all payment method list
    @GET(AppConstant.URL_ALL_PAYMENT_METHODS)
    fun allPaymentMethods(): Call<AllPaymentMethods>

    //Add payment methods
    @POST(AppConstant.URL_BIZ_ADD_PAYMENT_METHOD)
    fun addPaymentMethods(@Body requestBody: RequestBody): Call<JsonObject>


    @POST(AppConstant.URL_CHANGE_PSWD)
    fun changePassword(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_FORGOT_PSWD)
    fun resetPassword(@Body requestBody: RequestBody): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_ALL_CATEGORIES)
    fun bizAllCategories(): Call<BizCategories>

    //VIEW OTHER BUSINESS API CALLS
    @GET(AppConstant.URL_OTHER_BIZ_AMENITY_LIST)
    fun otherBizAmenityList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_OTHER_BIZ_PAYMENT_METHODS)
    fun otherBizPaymentList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_OTHER_BIZ_OP_HOURS)
    fun otherBizOperatingHours(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_OTHER_BIZ_PRODUCT_SERVICES)
    fun otherBizProductServicesList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_OTHER_BIZ_BROCHURE)
    fun otherBizBrochureList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_OTHER_BIZ_PRICING)
    fun otherBizPricingList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_OTHER_BIZ_CATEGORIES)
    fun otherBizCategoryList(@Query("business_id") businessId: String): Call<JsonObject>
}
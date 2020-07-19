package com.zaf.econnecto.service

import com.google.gson.JsonObject
import com.zaf.econnecto.network_call.request_model.UserRegisterData
import com.zaf.econnecto.network_call.response_model.UserRegisterResponse
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import com.zaf.econnecto.utils.AppConstant
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface EConnectoServices {

    @GET("business_list/business_list.php?id=")
    fun getBusinessList(@Query("email") email: String): Call<JsonObject>

    @POST("user_registration/register.php")
    fun registerUser(@Body requestBody : RequestBody): Call<JsonObject>

    @POST("user_registration/phone_verification.php")
    fun phoneVerification(@Body requestBody: RequestBody): Call<JsonObject>

    @POST("add_business/add_business.php")
    fun addYourBusiness(@Body requestBody: RequestBody): Call<JsonObject>

    @POST("user_login/login.php")
    fun login(@Body requestBody: RequestBody): Call<JsonObject>


    @POST(AppConstant.URL_BIZ_BASIC_DETAILS)
    fun bizBasicDetails(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_BIZ_PROD_SERVICES)
    fun addProductServices(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_BIZ_REMOVE_PROD_SERVICES)
    fun removeProductServices(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_BIZ_REMOVE_PROD_SERVICES)
    fun addBrochure(@Body requestBody: RequestBody): Call<JsonObject>


    @GET(AppConstant.URL_BIZ_AMENITY_LIST)
    fun bizAmenityList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_IMAGES)
    fun bizImageList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_OPERATING_HOURS)
    fun bizOperatingHours(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_BROCHURE_LIST)
    fun bizBrochureList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_PROD_SERVICES)
    fun bizProductServicesList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_PRICING)
    fun bizPricingList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_CATEGORIES)
    fun bizCategoryList(@Query("business_id") businessId: String): Call<JsonObject>

    @GET(AppConstant.URL_BIZ_PAYMENT_LIST)
    fun bizPaymentList(@Query("business_id") businessId: String): Call<JsonObject>

}
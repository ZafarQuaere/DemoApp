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

    @GET("business_list/business_list.php?id=")
    fun getBusinessList(@Query("email") email: String): Call<JsonObject>

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
    fun bizCategoryList(@Query("business_id") businessId: String): Call<Categories>

    @GET(AppConstant.URL_BIZ_PAYMENT_LIST)
    fun bizPaymentList(@Query("business_id") businessId: String): Call<PaymentMethods>

    @POST(AppConstant.URL_CHANGE_PSWD)
    fun changePassword(@Body requestBody: RequestBody): Call<JsonObject>

    @POST(AppConstant.URL_FORGOT_PSWD)
    fun resetPassword(@Body requestBody: RequestBody): Call<JsonObject>
}
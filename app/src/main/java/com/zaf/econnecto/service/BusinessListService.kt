package com.zaf.econnecto.service

import com.zaf.econnecto.network_call.request_model.UserRegisterData
import com.zaf.econnecto.network_call.response_model.UserRegisterResponse
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface BusinessListService {

    @GET("business_list.php?")
    fun getBusinessList(@Query("email") email: String): Call<BizListData>

    @POST("user_registration/register.php")
    fun registerUser(@Body requestBody : RequestBody): Call<UserRegisterResponse>

    @POST("usr_registration/phone_verification.php")
    fun phoneVerification(@Body requestBody: RequestBody): Call<JSONObject>
}
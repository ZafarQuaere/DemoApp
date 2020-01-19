package com.zaf.econnecto.service

import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BusinessListService {

    @GET("business_list.php?")
    fun getBusinessList(@Query("email") email: String): Call<BizListData>

}
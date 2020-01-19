package com.zaf.econnecto.service

import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import retrofit2.Call
import retrofit2.http.GET

interface EConnectoService {

    @GET("business")
    fun getBusinessList(): Call<BizListData>
}
package com.zaf.econnecto.network_call.response_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
//import com.zaf.econnecto.network_call.response_model.product_list.RegisterData


class UserRegisterResponse {

    @SerializedName("message")
    @Expose
    private var message: List<String?>? = null
    @SerializedName("status")
    @Expose
    private var status: Int? = null
    @SerializedName("data")
    @Expose
    private var data: Object? = null

    fun getMessage(): List<String?>? {
        return message
    }

    fun setMessage(message: List<String?>?) {
        this.message = message
    }

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

    fun getData(): Object? {
        return data
    }

    fun setData(data: Object?) {
        this.data = data
    }

}
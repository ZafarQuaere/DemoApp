package com.zaf.econnecto.network_call.response_model.add_biz

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class PinCodeResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null
    @SerializedName("data")
    @Expose
    private var data: List<PinCodeData?>? = null

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

    fun getData(): List<PinCodeData?>? {
        return data
    }

    fun setData(data: List<PinCodeData?>?) {
        this.data = data
    }
}
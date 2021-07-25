package com.zaf.econnecto.network_call.response_model.add_biz

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class PinCodeResponse {

    @SerializedName("Message")
    @Expose
    private var message: String? = null

    @SerializedName("Status")
    @Expose
    private var status: String? = null

    @SerializedName("PostOffice")
    @Expose
    private var postOffice: List<PostOffice?>? = null

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getPostOffice(): List<PostOffice?>? {
        return postOffice
    }

    fun setPostOffice(postOffice: List<PostOffice?>?) {
        this.postOffice = postOffice
    }

}
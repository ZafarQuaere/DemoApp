package com.zaf.econnecto.network_call.response_model.my_business;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BasicDetailsResponse {

    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<BasicDetailsData> data = null;

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<BasicDetailsData> getData() {
        return data;
    }

    public void setData(List<BasicDetailsData> data) {
        this.data = data;
    }
}

package com.zaf.econnecto.network_call.response_model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BizCategoryData {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<CategoryData> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CategoryData> getData() {
        return data;
    }

    public void setData(List<CategoryData> data) {
        this.data = data;
    }
}

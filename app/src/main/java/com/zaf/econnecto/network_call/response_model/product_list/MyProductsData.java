package com.zaf.econnecto.network_call.response_model.product_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyProductsData {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<ProductList> data = null;
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

    public List<ProductList> getData() {
        return data;
    }

    public void setData(List<ProductList> data) {
        this.data = data;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}

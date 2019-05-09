package com.zaf.econnecto.network_call.response_model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sales {
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("totalUnit")
    @Expose
    private String totalUnit;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalUnit() {
        return totalUnit;
    }

    public void setTotalUnit(String totalUnit) {
        this.totalUnit = totalUnit;
    }
}

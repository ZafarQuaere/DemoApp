package com.zaf.econnecto.network_call.response_model.my_business;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealsBgData {
    @SerializedName("bg_image_id")
    @Expose
    private String bgImageId;
    @SerializedName("image_link")
    @Expose
    private String imageLink;

    public String getBgImageId() {
        return bgImageId;
    }

    public void setBgImageId(String bgImageId) {
        this.bgImageId = bgImageId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}

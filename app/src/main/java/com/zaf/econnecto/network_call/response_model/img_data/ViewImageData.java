package com.zaf.econnecto.network_call.response_model.img_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewImageData {
    @SerializedName("img_id")
    @Expose
    private String imgId;
    @SerializedName("image_link")
    @Expose
    private String imageLink;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}

package com.zaf.econnecto.network_call.response_model.biz_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BizData {

    @SerializedName("business_pic")
    @Expose
    private String businessPic;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("year_founded")
    @Expose
    private String yearFounded;
    @SerializedName("business_uid")
    @Expose
    private String businessUid;
    @SerializedName("followers_count")
    @Expose
    private String followers_count;

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public String getBusinessPic() {
        return businessPic;
    }

    public void setBusinessPic(String businessPic) {
        this.businessPic = businessPic;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getYearFounded() {
        return yearFounded;
    }

    public void setYearFounded(String yearFounded) {
        this.yearFounded = yearFounded;
    }

    public String getBusinessUid() {
        return businessUid;
    }

    public void setBusinessUid(String businessUid) {
        this.businessUid = businessUid;
    }
}

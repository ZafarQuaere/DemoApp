package com.zaf.econnecto.network_call.response_model.biz_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BizData {

    @SerializedName("business_id")
    @Expose
    private String businessId;
    @SerializedName("owner_id")
    @Expose
    private String ownerId;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("year_established")
    @Expose
    private String yearEstablished;
    @SerializedName("mobile_1")
    @Expose
    private String mobile1;
    @SerializedName("biz_profile_pic")
    @Expose
    private String bizProfilePic;
    @SerializedName("followers_count")
    @Expose
    private String followersCount;
    @SerializedName("is_following")
    @Expose
    private Integer isFollowing;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getYearEstablished() {
        return yearEstablished;
    }

    public void setYearEstablished(String yearEstablished) {
        this.yearEstablished = yearEstablished;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getBizProfilePic() {
        return bizProfilePic;
    }

    public void setBizProfilePic(String bizProfilePic) {
        this.bizProfilePic = bizProfilePic;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Integer isFollowing) {
        this.isFollowing = isFollowing;
    }

}

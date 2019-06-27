package com.zaf.econnecto.network_call.response_model.biz_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BizData {

    @SerializedName("business_pic")
    @Expose
    private String businessPic;
    @SerializedName("banner_pic")
    @Expose
    private String bannerPic;
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
    private String followersCount;
    @SerializedName("is_following")
    @Expose
    private Integer isFollowing;

    public String getBusinessPic() {
        return businessPic;
    }

    public void setBusinessPic(String businessPic) {
        this.businessPic = businessPic;
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
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

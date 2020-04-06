package com.zaf.econnecto.network_call.response_model.my_business;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyBusinessData {

    @SerializedName("business_id")
    @Expose
    private String businessId;
    @SerializedName("owner_id")
    @Expose
    private String ownerId;
    @SerializedName("business_uid")
    @Expose
    private String businessUid;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("year_established")
    @Expose
    private String yearEstablished;
    @SerializedName("address_1")
    @Expose
    private String address1;
    @SerializedName("address_2")
    @Expose
    private String address2;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("area_locality")
    @Expose
    private String areaLocality;
    @SerializedName("city_town")
    @Expose
    private String cityTown;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("is_outside_serve")
    @Expose
    private String isOutsideServe;
    @SerializedName("mobile_1")
    @Expose
    private String mobile1;
    @SerializedName("mobile_2")
    @Expose
    private String mobile2;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("biz_banner_pic")
    @Expose
    private String bizBannerPic;
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

    public String getBusinessUid() {
        return businessUid;
    }

    public void setBusinessUid(String businessUid) {
        this.businessUid = businessUid;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAreaLocality() {
        return areaLocality;
    }

    public void setAreaLocality(String areaLocality) {
        this.areaLocality = areaLocality;
    }

    public String getCityTown() {
        return cityTown;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIsOutsideServe() {
        return isOutsideServe;
    }

    public void setIsOutsideServe(String isOutsideServe) {
        this.isOutsideServe = isOutsideServe;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBizBannerPic() {
        return bizBannerPic;
    }

    public void setBizBannerPic(String bizBannerPic) {
        this.bizBannerPic = bizBannerPic;
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

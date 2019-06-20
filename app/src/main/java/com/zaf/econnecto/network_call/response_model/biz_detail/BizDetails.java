package com.zaf.econnecto.network_call.response_model.biz_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BizDetails {
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("business_category")
    @Expose
    private String businessCategory;
    @SerializedName("detailed_description")
    @Expose
    private String detailedDescription;
    @SerializedName("year_founded")
    @Expose
    private String yearFounded;
    @SerializedName("awards")
    @Expose
    private String awards;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone1")
    @Expose
    private String phone1;
    @SerializedName("phone2")
    @Expose
    private String phone2;
    @SerializedName("business_email")
    @Expose
    private String businessEmail;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("charges_unit")
    @Expose
    private Object chargesUnit;
    @SerializedName("charges_amount")
    @Expose
    private Object chargesAmount;
    @SerializedName("followers_count")
    @Expose
    private String followersCount;
    @SerializedName("banner_pic")
    @Expose
    private String bannerPic;
    @SerializedName("business_pic")
    @Expose
    private String businessPic;

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

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getYearFounded() {
        return yearFounded;
    }

    public void setYearFounded(String yearFounded) {
        this.yearFounded = yearFounded;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Object getChargesUnit() {
        return chargesUnit;
    }

    public void setChargesUnit(Object chargesUnit) {
        this.chargesUnit = chargesUnit;
    }

    public Object getChargesAmount() {
        return chargesAmount;
    }

    public void setChargesAmount(Object chargesAmount) {
        this.chargesAmount = chargesAmount;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    public String getBusinessPic() {
        return businessPic;
    }

    public void setBusinessPic(String businessPic) {
        this.businessPic = businessPic;
    }


}

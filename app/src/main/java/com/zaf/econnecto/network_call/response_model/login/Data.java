package com.zaf.econnecto.network_call.response_model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("year_of_birth")
    @Expose
    private String yearOfBirth;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("is_email_verified")
    @Expose
    private String isEmailVerified;
    @SerializedName("is_phone_verified")
    @Expose
    private String isPhoneVerified;
    @SerializedName("business_status")
    @Expose
    private String businessStatus;
    @SerializedName("following_count")
    @Expose
    private String followingCount;
    @SerializedName("JWT_Token")
    @Expose
    private String jWTToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(String isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(String isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getJWTToken() {
        return jWTToken;
    }

    public void setJWTToken(String jWTToken) {
        this.jWTToken = jWTToken;
    }
}

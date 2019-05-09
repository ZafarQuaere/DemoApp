package com.zaf.econnecto.network_call.request_model;

import android.os.Parcel;
import android.os.Parcelable;

public class Register  implements Parcelable{

    private String name;
    private String password;
    private String mobile;
    private String email;
    private int userType;
    private int dealerId;

    public Register(Parcel in) {
        name = in.readString();
        password = in.readString();
        mobile = in.readString();
        email = in.readString();
        userType = in.readInt();
        dealerId = in.readInt();
    }

    public static final Creator<Register> CREATOR = new Creator<Register>() {
        @Override
        public Register createFromParcel(Parcel in) {
            return new Register(in);
        }

        @Override
        public Register[] newArray(int size) {
            return new Register[size];
        }
    };

    public Register(String name, String password, String mobile, String email, int userType, int dealerId) {
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.userType = userType;
        this.dealerId = dealerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeInt(userType);
        dest.writeInt(dealerId);
    }
}

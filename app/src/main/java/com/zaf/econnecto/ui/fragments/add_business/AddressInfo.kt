package com.zaf.econnecto.ui.fragments.add_business

import android.os.Parcel
import android.os.Parcelable

data class AddressInfo(val bizName: String?,val shortDesc: String?, val category1: String?, val estdYear: Int, val category2: String?, val category3: String?
                       , val address1: String?, val address2: String?, val landmark: String?, val pincode: String?, val locality: String?
                       , val city: String?, val state: String?, val country: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bizName)
        parcel.writeString(shortDesc)
        parcel.writeString(category1)
        parcel.writeInt(estdYear)
        parcel.writeString(category2)
        parcel.writeString(category3)
        parcel.writeString(address1)
        parcel.writeString(address2)
        parcel.writeString(landmark)
        parcel.writeString(pincode)
        parcel.writeString(locality)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressInfo> {
        override fun createFromParcel(parcel: Parcel): AddressInfo {
            return AddressInfo(parcel)
        }

        override fun newArray(size: Int): Array<AddressInfo?> {
            return arrayOfNulls(size)
        }
    }
}
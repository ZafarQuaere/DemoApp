package com.zaf.econnecto.ui.fragments.add_business

import android.os.Parcel
import android.os.Parcelable

class AddBizModel(val productId: String?, val name: String?, val age: Int, val email: String?, val phone: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(name)
        parcel.writeInt(age)
        parcel.writeString(email)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddBizModel> {
        override fun createFromParcel(parcel: Parcel): AddBizModel {
            return AddBizModel(parcel)
        }

        override fun newArray(size: Int): Array<AddBizModel?> {
            return arrayOfNulls(size)
        }
    }

}
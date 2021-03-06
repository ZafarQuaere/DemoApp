package com.zaf.econnecto.ui.fragments.add_business

import android.os.Parcel
import android.os.Parcelable

data class BizDetailData(val bizName: String?,val shortDesc: String?, val estdYear: Int, val category1: String?, val category2: String?, val category3: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bizName)
        parcel.writeString(shortDesc)
        parcel.writeInt(estdYear)
        parcel.writeString(category1)
        parcel.writeString(category2)
        parcel.writeString(category3)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BizDetailData> {
        override fun createFromParcel(parcel: Parcel): BizDetailData {
            return BizDetailData(parcel)
        }

        override fun newArray(size: Int): Array<BizDetailData?> {
            return arrayOfNulls(size)
        }
    }
}
package com.zaf.econnecto.network_call.response_model.add_biz

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class PinCodeData {

    @SerializedName("pincode")
    @Expose
    private var pincode: String? = null
    @SerializedName("office_name")
    @Expose
    private var officeName: String? = null
    @SerializedName("delivery_status")
    @Expose
    private var deliveryStatus: String? = null
    @SerializedName("division_name")
    @Expose
    private var divisionName: String? = null
    @SerializedName("region_name")
    @Expose
    private var regionName: String? = null
    @SerializedName("circle_name")
    @Expose
    private var circleName: String? = null
    @SerializedName("district")
    @Expose
    private var district: String? = null
    @SerializedName("state_name")
    @Expose
    private var stateName: String? = null
    @SerializedName("taluk")
    @Expose
    private var taluk: String? = null

    fun getPincode(): String? {
        return pincode
    }

    fun setPincode(pincode: String?) {
        this.pincode = pincode
    }

    fun getOfficeName(): String? {
        return officeName
    }

    fun setOfficeName(officeName: String?) {
        this.officeName = officeName
    }

    fun getDeliveryStatus(): String? {
        return deliveryStatus
    }

    fun setDeliveryStatus(deliveryStatus: String?) {
        this.deliveryStatus = deliveryStatus
    }

    fun getDivisionName(): String? {
        return divisionName
    }

    fun setDivisionName(divisionName: String?) {
        this.divisionName = divisionName
    }

    fun getRegionName(): String? {
        return regionName
    }

    fun setRegionName(regionName: String?) {
        this.regionName = regionName
    }

    fun getCircleName(): String? {
        return circleName
    }

    fun setCircleName(circleName: String?) {
        this.circleName = circleName
    }

    fun getDistrict(): String? {
        return district
    }

    fun setDistrict(district: String?) {
        this.district = district
    }

    fun getStateName(): String? {
        return stateName
    }

    fun setStateName(stateName: String?) {
        this.stateName = stateName
    }

    fun getTaluk(): String? {
        return taluk
    }

    fun setTaluk(taluk: String?) {
        this.taluk = taluk
    }
}
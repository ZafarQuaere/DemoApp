package com.zaf.econnecto.network_call.response_model.add_biz

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PostOffice {
    @SerializedName("Name")
    @Expose
    private var name: String? = null

    @SerializedName("Description")
    @Expose
    private var description: Any? = null

    @SerializedName("BranchType")
    @Expose
    private var branchType: String? = null

    @SerializedName("DeliveryStatus")
    @Expose
    private var deliveryStatus: String? = null

    @SerializedName("Circle")
    @Expose
    private var circle: String? = null

    @SerializedName("District")
    @Expose
    private var district: String? = null

    @SerializedName("Division")
    @Expose
    private var division: String? = null

    @SerializedName("Region")
    @Expose
    private var region: String? = null

    @SerializedName("Block")
    @Expose
    private var block: String? = null

    @SerializedName("State")
    @Expose
    private var state: String? = null

    @SerializedName("Country")
    @Expose
    private var country: String? = null

    @SerializedName("Pincode")
    @Expose
    private var pincode: String? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getDescription(): Any? {
        return description
    }

    fun setDescription(description: Any?) {
        this.description = description
    }

    fun getBranchType(): String? {
        return branchType
    }

    fun setBranchType(branchType: String?) {
        this.branchType = branchType
    }

    fun getDeliveryStatus(): String? {
        return deliveryStatus
    }

    fun setDeliveryStatus(deliveryStatus: String?) {
        this.deliveryStatus = deliveryStatus
    }

    fun getCircle(): String? {
        return circle
    }

    fun setCircle(circle: String?) {
        this.circle = circle
    }

    fun getDistrict(): String? {
        return district
    }

    fun setDistrict(district: String?) {
        this.district = district
    }

    fun getDivision(): String? {
        return division
    }

    fun setDivision(division: String?) {
        this.division = division
    }

    fun getRegion(): String? {
        return region
    }

    fun setRegion(region: String?) {
        this.region = region
    }

    fun getBlock(): String? {
        return block
    }

    fun setBlock(block: String?) {
        this.block = block
    }

    fun getState(): String? {
        return state
    }

    fun setState(state: String?) {
        this.state = state
    }

    fun getCountry(): String? {
        return country
    }

    fun setCountry(country: String?) {
        this.country = country
    }

    fun getPincode(): String? {
        return pincode
    }

    fun setPincode(pincode: String?) {
        this.pincode = pincode
    }
}

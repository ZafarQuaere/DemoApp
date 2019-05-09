package com.zaf.econnecto.network_call.response_model.orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderList {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("brandId")
    @Expose
    private String brandId;
    @SerializedName("paymentId")
    @Expose
    private String paymentId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("dealerId")
    @Expose
    private String dealerId;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("deliverDate")
    @Expose
    private String deliverDate;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("OrderType")
    @Expose
    private String orderType;
    @SerializedName("AlternateDay")
    @Expose
    private String alternateDay;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAlternateDay() {
        return alternateDay;
    }

    public void setAlternateDay(String alternateDay) {
        this.alternateDay = alternateDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

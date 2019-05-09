package com.zaf.econnecto.network_call.response_model.product_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("brandId")
    @Expose
    private String brandId;
    @SerializedName("pName")
    @Expose
    private String pName;
    @SerializedName("dealerId")
    @Expose
    private String dealerId;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("IsBrand")
    @Expose
    private String isBrand;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getIsBrand() {
        return isBrand;
    }

    public void setIsBrand(String isBrand) {
        this.isBrand = isBrand;
    }
}

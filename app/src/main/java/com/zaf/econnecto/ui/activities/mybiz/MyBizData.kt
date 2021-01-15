package com.zaf.econnecto.ui.activities.mybiz

import android.os.Parcelable
import java.io.Serializable

data class AllAmenityList(val status: Int, val message: String, val data: List<GeneralAmenities>)
data class GeneralAmenities(val amenity_id: String, val amenity_name: String, val amenity_icon: String)

data class Amenities(val status: Int, val message:List<String>,val data: List<AmenityData>)
data class AmenityData(val amenity_id: String, val amenity_name: String)

data class AmenitySelectedData(var isChecked: Boolean = false, val amenity_id: String, val amenity_name: String) {}

data class OPHours(val status: Int, val message:List<String>, val data: OPHoursData)
data class OPHoursData(val CurrentStatus: String, val Mon: List<String>, val Tue: List<String>, val Wed: List<String>, val Thu: List<String>, val Fri: List<String>, val Sat: List<String>, val Sun: List<String>)


data class ProductNService(val status: Int, val message:List<String>, val data: List<ProductNServiceData>)
data class ProductNServiceData(val prod_serv_id: String, val prod_serv_name: String)

data class PaymentMethods(val status: Int, val message:List<String>, val data: List<PaymentMethodData>)
data class PaymentMethodData(val p_method_id: String, val p_method_name: String)

data class Pricing(val status: Int, val message:List<String>, val data: List<PricingData>)
data class PricingData(val prod_serv_id: String, val prod_serv_name: String, val price: String)

data class Categories(val status: Int, val message:List<String>, val data: List<CategoryData>)
data class CategoryData(val category_id: String, val category_name: String)

data class Brochure(val status: Int, val message:List<String>, val data: List<BrochureData>)
data class BrochureData(val brochure_id: String, val brochure_link: String)




data class AllPaymentMethods(val status: Int, val message: String, val data: List<GeneralPaymentMethods>)
data class GeneralPaymentMethods(val p_method_id: String, val p_method_name: String, val p_method_icon: String)
data class PaymentSelectedData(var isChecked: Boolean = false, val p_method_id: String, val p_method_name: String) : Serializable
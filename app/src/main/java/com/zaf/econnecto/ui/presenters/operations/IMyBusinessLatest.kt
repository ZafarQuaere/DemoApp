package com.zaf.econnecto.ui.presenters.operations

import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.mybiz.*

interface IMyBusinessLatest {

    fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse, imageUpdate: Boolean)
    fun updateOperatingHours(data: OPHoursData?)
    fun updateProductServiceSection(data: List<ProductNServiceData>?)
    fun updateBrochureSection(data: List<BrochureData>?)
    fun updateAmenitiesSection(data: List<AmenityData>?)
    fun updatePaymentSection(data: List<PaymentMethodData>?)
    fun updatePricingSection(data: List<PricingData>?)
    fun updateCategories(data: List<UserCategoryData>?)
}
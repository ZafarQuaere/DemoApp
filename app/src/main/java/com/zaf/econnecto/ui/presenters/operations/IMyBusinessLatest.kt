package com.zaf.econnecto.ui.presenters.operations

import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse

interface IMyBusinessLatest {

    fun updateBannerImage(data: MutableList<ViewImageData>)
    fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse, imageUpdate: Boolean)
    fun updateOperatingHours()
    fun updateProductServiceSection()
    fun updateBrochureSection()
    fun updateAmenitiesSection()
    fun updatePaymentSection()
    fun updatePricingSection()
    fun updateCategories()
}
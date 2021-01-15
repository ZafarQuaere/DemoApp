package com.zaf.econnecto.ui.interfaces

import com.zaf.econnecto.ui.activities.mybiz.GeneralAmenities
import com.zaf.econnecto.ui.activities.mybiz.GeneralPaymentMethods
import com.zaf.econnecto.ui.activities.mybiz.ProductNServiceData

interface DeleteProductListener {
    fun deleteProd(prodData: ProductNServiceData)
}

interface IPaymentOptionList {
    fun updatePaymentListUI(allPaymentMethod: List<GeneralPaymentMethods>?)
}

interface IGeneralAmenityList {
    fun updateAmenityList(allAmenityList: List<GeneralAmenities>?)
}

interface AmenityAddedListener {
    fun updateAmenities()
}

interface PaymentMethodAddListener {
    fun updatePaymentMethod()
}
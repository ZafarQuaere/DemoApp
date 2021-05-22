package com.zaf.econnecto.ui.interfaces

import com.zaf.econnecto.model.CategoryListData
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

interface AllCategoriesListener {
    fun updateCategoriesUI(categories: MutableList<CategoryListData>?)
}

interface OnBizCategoryItemClickListener {
    fun onBizCategoryItemClick(mItem: CategoryListData)
}

interface CategoryAddedListener {
    fun updateCategory()
}

interface PricingAddedListener {
    fun updatePricing()
}
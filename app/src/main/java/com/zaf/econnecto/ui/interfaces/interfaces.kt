package com.zaf.econnecto.ui.interfaces

import com.zaf.econnecto.ui.activities.mybiz.CategoryData
import com.zaf.econnecto.ui.activities.mybiz.ProductNServiceData

interface DeleteProductListener{
    fun deleteProd(prodData: ProductNServiceData)
}
package com.zaf.econnecto.ui.presenters.operations

import com.zaf.econnecto.network_call.response_model.home.CategoryData

interface IViewBizns {

    fun updateCategory( todaySalesData : List<CategoryData>)
}
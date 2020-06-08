package com.zaf.econnecto.ui.presenters.operations

import com.zaf.econnecto.network_call.response_model.home.CategoryData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData

interface IViewBizns {

    fun updateCategory( categoryData:  List<CategoryData>)

    fun updateBannerImage(viewImgData: MutableList<ViewImageData>)
}
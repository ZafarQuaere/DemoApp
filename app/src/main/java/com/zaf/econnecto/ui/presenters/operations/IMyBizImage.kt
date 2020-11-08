package com.zaf.econnecto.ui.presenters.operations

import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData

interface IMyBizImage {
    fun updateBannerImage(data: MutableList<ViewImageData>)
}
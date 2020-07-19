package com.zaf.econnecto.ui.interfaces

import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData

interface DeleteImageListener {
    fun onDeleteClick(s: ViewImageData?, position: Int)
}

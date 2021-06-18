package com.zaf.econnecto.ui.activities.mybiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData

open class BaseViewModel : ViewModel() {

    val imageList: MutableLiveData<MutableList<ViewImageData>> by lazy { MutableLiveData<MutableList<ViewImageData>>() }
    val isImageDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
}
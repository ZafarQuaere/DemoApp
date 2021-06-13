package com.zaf.econnecto.ui.activities.mybiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData

public open class BaseViewModel : ViewModel() {

    val imageList: MutableLiveData<ViewImageData> by lazy { MutableLiveData<ViewImageData>() }
    val imagePosition: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
}
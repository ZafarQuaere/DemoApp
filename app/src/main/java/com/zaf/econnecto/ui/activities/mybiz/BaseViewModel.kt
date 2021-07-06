package com.zaf.econnecto.ui.activities.mybiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import retrofit2.Response

open class BaseViewModel : ViewModel() {

    val imageList: MutableLiveData<MutableList<ViewImageData>> by lazy { MutableLiveData<MutableList<ViewImageData>>() }
    val isImageDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val bizAmenityList: MutableLiveData<Amenities> by lazy { MutableLiveData<Amenities>() }
    val isAmenityDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val allAmenityList: MutableLiveData<MutableList<GeneralAmenities>> by lazy { MutableLiveData<MutableList<GeneralAmenities>>() }

    val mbCategoryList: MutableLiveData<UserCategories> by lazy { MutableLiveData<UserCategories>() }
    val isCategoryDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val mbPayOptionList :  MutableLiveData<PaymentMethods> by lazy { MutableLiveData<PaymentMethods>() }
    val isPayOptionDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val mbPricingList :  MutableLiveData<Pricing> by lazy { MutableLiveData<Pricing>() }
    val isPricingDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val opHourList: MutableLiveData<List<OPHoursData>> by lazy { MutableLiveData<List<OPHoursData>>() }
}
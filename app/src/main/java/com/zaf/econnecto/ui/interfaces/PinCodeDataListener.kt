package com.zaf.econnecto.ui.interfaces

import com.zaf.econnecto.network_call.response_model.add_biz.PinCodeResponse

interface PinCodeDataListener {
    fun onDataFetched(pincodeData : PinCodeResponse?)
}
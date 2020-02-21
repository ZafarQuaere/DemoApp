package com.zaf.econnecto.ui.fragments.add_business

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.google.android.material.textfield.TextInputEditText
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppController
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils

class AddBizScreen2ViewModel : ViewModel() {


    fun updatePinCodeData(activity: Activity?, editPinCode: TextInputEditText?) {

        editPinCode!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 6) {
                    LogUtils.showToast(activity, s.toString())
                    callPinCodeApi(activity, s.toString())
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun callPinCodeApi(activity: Activity?, pincode: String) {
        var loader = AppDialogLoader.getLoader(activity)
        var url = AppConstant.URL_PINCODE + pincode
        LogUtils.DEBUG("URL : $url")
        val objectRequest = MyJsonObjectRequest(activity, Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("PinCode Response ::$response")
            val status = response.optInt("status")

            when (status) {
                200 -> {
                    LogUtils.showToast(activity, "List is fetched successfully")
                    //update ui from here
                    // mProductFrag.updateList(data.getData())
                }

                else -> {
                    //update UI with null
                    // mProductFrag.updateList(null)
                }

            }

            loader.dismiss()
        }, Response.ErrorListener { error ->
            val statusCode = error.networkResponse.statusCode
            loader.dismiss()
            LogUtils.DEBUG("PinCode  error.networkResponse.statusCode:  $statusCode")
            updateErrorStatus(activity,statusCode)

        })
        AppController.getInstance().addToRequestQueue(objectRequest, "PinCode")
    }

    private fun updateErrorStatus(activity: Activity?,statusCode: Int) {
        when(statusCode) {
            404 -> {
                LogUtils.showToast(activity, "PinCode doesn't exist")
            }
            500 -> {
                LogUtils.showToast(activity, "Internal Server Error – We had a problem with our server. Try again later")
            }
            503 -> {
                LogUtils.showToast(activity, "Service Unavailable – We’re temporarily offline for maintenance. Please try again later")
            }
        }
    }

}

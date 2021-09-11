package com.zaf.econnecto.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersBusinessViewModel : BaseViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mActivity: Activity

    var basicDetailsData = MutableLiveData<MutableList<BasicDetailsData>>()
    lateinit var basicDetailsResponse: LiveData<BasicDetailsResponse>

    fun otherBizBasicDetails(activity: Activity?, listener: IMyBusinessLatest, businessId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("id", Utils.getUserID(mActivity))
        jsonObject.put("business_id", businessId)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.getOtherBizBasicDetails(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  requestBody $requestBody")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("otherBizBasicDetails Api Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("otherBizBasicDetailsApi Response:->> $body")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    val basicDetailsResponse = ParseManager.getInstance().fromJSON(body.toString(), BasicDetailsResponse::class.java)
                    listener.updateBasicDetails(basicDetailsResponse, false)
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { /*(mActivity).onBackPressed()*/ }
                }
            }
        })
    }

    fun bizAmenityList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizAmenityList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizAmenityList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                LogUtils.DEBUG("bizAmenityList response: " + ParseManager.getInstance().toJSON(response.body()))
                loader.dismiss()
                val body = JSONObject(Gson().toJson(response.body()))
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val amenities = ParseManager.getInstance().fromJSON(body.toString(), Amenities::class.java)
                    listener.updateAmenitiesSection(amenities!!.data)
                } else {
                    listener.updateAmenitiesSection(null)
                }

            }
        })
    }

    fun bizImageList(activity: Activity?, listener: IMyBizImage, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizImageList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizImageList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizImageList Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val data = ParseManager.getInstance().fromJSON(body.toString(), ViewImages::class.java)
                    listener.updateBannerImage(data.data)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.ERROR(message)
                }
                loader.dismiss()
            }
        })
    }

    fun bizOperatingHours(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizOperatingHours(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizOperatingHours Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val opHours = ParseManager.getInstance().fromJSON(body.toString(), OPHours::class.java)
                    opHourList.value = opHours.data
                    listener.updateOperatingHours(opHours.data)
                } else {
                    listener.updateOperatingHours(null)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizOperatingHours error: " + t.localizedMessage)
            }
        })
    }

    fun bizBrochureList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizBrochureList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizBrochureList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizBrochureList Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val brochure = ParseManager.getInstance().fromJSON(body.toString(), Brochure::class.java)
                    listener.updateBrochureSection(brochure.data)
                } else {
                    listener.updateBrochureSection(null)
                }
                loader.dismiss()
            }
        })
    }

    fun bizProductServicesList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizProductServicesList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizProductServicesList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizProductServicesList Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val PnService = ParseManager.getInstance().fromJSON(body.toString(), ProductNService::class.java)
                    listener.updateProductServiceSection(PnService.data)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    listener.updateProductServiceSection(null)
                }
                loader.dismiss()
            }
        })
    }

    fun bizPaymentMethodList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizPaymentList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizPaymentMethodList Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                LogUtils.DEBUG("bizPaymentMethodList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                val body = JSONObject(Gson().toJson(response.body()))
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val paymentMethod = ParseManager.getInstance().fromJSON(body.toString(), PaymentMethods::class.java)
                    listener.updatePaymentSection(paymentMethod.data)
                } else {
                    listener.updatePaymentSection(null)
                }
                loader.dismiss()
            }
        })
    }

    fun bizPricingList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizPricingList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizPricingList Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizPricingList Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val pricing = ParseManager.getInstance().fromJSON(body.toString(), Pricing::class.java)
                    listener.updatePricingSection(pricing.data)
                } else {
                    listener.updatePricingSection(null)
                }
                loader.dismiss()
            }
        })
    }

    fun bizCategoryList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizCategoryList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizCategoryList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizCategoryList Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val userCategories = ParseManager.getInstance().fromJSON(body.toString(), UserCategories::class.java)
                    listener.updateCategories(userCategories.data)
                } else {
                    listener.updateCategories(null)
                }
            }
        })
    }

    fun callFollowApi(mContext: Context, action: String, businessUid: String) {
        val url = AppConstant.URL_BASE_MVP + AppConstant.URL_FOLLOW
        val requestObject = JSONObject()
        try {
            requestObject.put("jwt_token", Utils.getAccessToken(mContext))
            requestObject.put("action", action)
            requestObject.put("id", Utils.getUserID(mContext))
            requestObject.put("business_id", businessUid)
        } catch (e: JSONException) {
            e.printStackTrace()
            LogUtils.ERROR(e.message)
        }
        LogUtils.DEBUG("URL : $url\nRequest Body :: $requestObject")
        val objectRequest = MyJsonObjectRequest(mActivity, Request.Method.POST, url, requestObject,
            { response ->
                LogUtils.DEBUG("Follow Response ::$response")
                if (response != null && response.toString().isNotEmpty()) {
                    val status = response.optInt("status")
                    followStatus.value = status
                }
            }
        ) { error -> LogUtils.DEBUG("Follow Error ::" + error.message) }
        AppController.getInstance().addToRequestQueue(objectRequest, "Follow")
    }
}


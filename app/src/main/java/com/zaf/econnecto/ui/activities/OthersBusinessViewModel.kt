package com.zaf.econnecto.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import com.zaf.econnecto.utils.parser.ParseManager
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersBusinessViewModel : ViewModel() {

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

        requestCall.enqueue(object : Callback<Amenities> {
            override fun onFailure(call: Call<Amenities>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizAmenityList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<Amenities>, response: Response<Amenities>) {
                LogUtils.DEBUG("bizAmenityList response: " + ParseManager.getInstance().toJSON(response.body()))
                loader.dismiss()
                if (response.isSuccessful) {
                    val amenities = response.body()
                    if (amenities?.status == AppConstant.SUCCESS) {
                        listener.updateAmenitiesSection(amenities!!.data)
                    } else {
                        listener.updateAmenitiesSection(null)
                    }
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
                var status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val data = ParseManager.getInstance().fromJSON(body.toString(), ViewImages::class.java)
                    listener!!.updateBannerImage(data.data)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
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
        val requestCall = categoryService.otherBizOperatingHours("21"/*bizId*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<OPHours> {
            override fun onResponse(call: Call<OPHours>, response: Response<OPHours>) {
                loader.dismiss()
                LogUtils.DEBUG("bizOperatingHours response: " + ParseManager.getInstance().toJSON(response.body()))
                if (response.isSuccessful) {
                    val opHours = response.body()
                    if (opHours!!.status == AppConstant.SUCCESS) {
                        listener.updateOperatingHours(opHours.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), opHours.message[0])
                    }
                }
            }

            override fun onFailure(call: Call<OPHours>, t: Throwable) {
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
        val requestCall = categoryService.otherBizBrochureList("21"/*bizId*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Brochure> {
            override fun onFailure(call: Call<Brochure>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizBrochureList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<Brochure>, response: Response<Brochure>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizBrochureList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    val brochure: Brochure = response.body()!!
                    if (brochure.status == AppConstant.SUCCESS) {
                        listener.updateBrochureSection(brochure.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), brochure.message[0])
                    }
                }
                loader.dismiss()
            }
        })
    }

    fun bizProductServicesList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
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
                LogUtils.DEBUG("bizProductServicesList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")


                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizImageList Response:->> $body")
                var status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val PnService = ParseManager.getInstance().fromJSON(body.toString(), ProductNService::class.java)
                    listener.updateProductServiceSection(PnService.data)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
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
        requestCall.enqueue(object : Callback<PaymentMethods> {
            override fun onFailure(call: Call<PaymentMethods>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizPaymentMethodList Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<PaymentMethods>, response: Response<PaymentMethods>) {
                LogUtils.DEBUG("bizPaymentMethodList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    val paymentMethod: PaymentMethods = response.body()!!
                    if (paymentMethod.status == AppConstant.SUCCESS) {
                        listener.updatePaymentSection(paymentMethod.data)
                    } else {
                        listener.updatePaymentSection(null)
                    }
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
        val requestCall = categoryService.otherBizPricingList("21"/*bizId*/)
//        val requestCall = categoryService.bizPricingList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Pricing> {
            override fun onFailure(call: Call<Pricing>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizPricingList Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<Pricing>, response: Response<Pricing>) {
                LogUtils.DEBUG("bizPricingList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    val pricing: Pricing = response.body()!!
                    if (pricing.status == AppConstant.SUCCESS) {
                        listener.updatePricingSection(pricing.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), pricing.message[0])
                    }
                }
                loader.dismiss()
            }
        })
    }


    fun bizCategoryList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.otherBizCategoryList("21"/*bizId*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<UserCategories> {
            override fun onFailure(call: Call<UserCategories>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizCategoryList() Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<UserCategories>, response: Response<UserCategories>) {
                LogUtils.DEBUG("bizCategoryList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                loader.dismiss()
                val userCategories: UserCategories = response.body()!!
                if (userCategories.status == AppConstant.SUCCESS) {
                    listener.updateCategories(userCategories.data)
                } else {
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), userCategories.message[0])
                }
            }
        })
    }

}


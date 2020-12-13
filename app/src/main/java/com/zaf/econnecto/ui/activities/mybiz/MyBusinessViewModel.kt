package com.zaf.econnecto.ui.activities.mybiz

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
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBusinessViewModel : ViewModel() {

    lateinit var mActivity: Activity
    var basicDetailsData =  MutableLiveData<MutableList<BasicDetailsData>>()
    lateinit var basicDetailsResponse:  LiveData<BasicDetailsResponse>

    fun callBasicDetailsApi(activity: Activity?, imageUpdate: Boolean, listener : IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.bizBasicDetails(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: ${jsonObject.toString()}")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("callBasicDetailsApi Response:->> $body")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    val basicDetailsResponse = ParseManager.getInstance().fromJSON(body.toString(), BasicDetailsResponse::class.java)
//                   basicDetailsData = basicDetailsResponse.data.toMutableList()
                    PrefUtil.setBasicDetailsData(mActivity, body.toString())
                    listener.updateBasicDetails(basicDetailsResponse, imageUpdate)

                }  else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity ).onBackPressed() }
                }
            }
        })
    }

    fun bizAmenityList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizAmenityList("21"/*PrefUtil.getBizId(mActivity)*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Amenities> {
            override fun onFailure(call: Call<Amenities>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.message)
            }

            override fun onResponse(call: Call<Amenities>, response: Response<Amenities>) {
                LogUtils.DEBUG("bizAmenityList response: " + response.body().toString())
                loader.dismiss()
                if (response.isSuccessful) {
                    val amenities = response.body()
                    if (amenities?.status == AppConstant.SUCCESS) {
                        listener.updateAmenitiesSection(amenities!!.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), amenities!!.message[0])
                    }
                }
            }
        })
    }

    fun bizImageList(activity: Activity?, listener: IMyBizImage?) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizImageList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizImageList Response:->> ${body.toString()}")
                var status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val data = ParseManager.getInstance().fromJSON(body.toString(), ViewImages::class.java)
                    listener!!.updateBannerImage(data.data)
//                    PrefUtil.saveImageData(mActivity, response.toString())
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
                loader.dismiss()
            }
        })
    }

    fun bizOperatingHours(activity: Activity?, listener: IMyBusinessLatest){
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizOperatingHours("21"/*PrefUtil.getBizId(mActivity)*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue( object : Callback<OPHours> {
            override fun onResponse(call: Call<OPHours>, response: Response<OPHours>) {
                loader.dismiss()
                LogUtils.DEBUG("bizOperatingHours response: " + response.body().toString())
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
                LogUtils.DEBUG("bizOperatingHours error: "+t.localizedMessage)
            }

        })
    }

    fun bizBrochureList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizBrochureList("21"/*PrefUtil.getBizId(mActivity)*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Brochure> {
            override fun onFailure(call: Call<Brochure>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<Brochure>, response: Response<Brochure>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizBrochureList Response:->> $body")
                if (response != null && response.isSuccessful ){
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

    fun bizProductServicesList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizProductServicesList("21"/*PrefUtil.getBizId(mActivity)*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<ProductNService> {
            override fun onFailure(call: Call<ProductNService>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }
            override fun onResponse(call: Call<ProductNService>, response: Response<ProductNService>) {
                LogUtils.DEBUG("bizProductServicesList Response:->> ${response.toString()}")
                if (response != null && response.isSuccessful ){
                    val PnService: ProductNService = response.body()!!
                    if (PnService.status == AppConstant.SUCCESS) {
                        listener.updateProductServiceSection(PnService.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), PnService.message[0])
                    }
                }
                loader.dismiss()
            }
        })
    }

    fun bizPaymentMethodList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPaymentList("21"/*PrefUtil.getBizId(mActivity)*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<PaymentMethods> {
            override fun onFailure(call: Call<PaymentMethods>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<PaymentMethods>, response: Response<PaymentMethods>) {
                LogUtils.DEBUG("bizPaymentMethodList Response:->> ${response.body().toString()}")
                if (response != null && response.isSuccessful ){
                    val paymentMethod: PaymentMethods = response.body()!!
                    if (paymentMethod.status == AppConstant.SUCCESS) {
                        listener.updatePaymentSection(paymentMethod.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), paymentMethod.message[0])
                    }
                }
                loader.dismiss()
            }
        })
    }

    fun bizPricingList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPricingList("21"/*PrefUtil.getBizId(mActivity)*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Pricing> {
            override fun onFailure(call: Call<Pricing>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<Pricing>, response: Response<Pricing>) {
                LogUtils.DEBUG("bizPricingList Response:->> ${response.body().toString()}")
                if (response != null && response.isSuccessful ){
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

    fun bizCategoryList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizCategoryList("21"/*PrefUtil.getBizId(mActivity)*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<Categories> {
            override fun onFailure(call: Call<Categories>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                LogUtils.DEBUG("bizCategoryList Response:->> ${response.body().toString()}")
                loader.dismiss()
                val categories : Categories = response.body()!!
                if (categories.status == AppConstant.SUCCESS) {
                    listener.updateCategories(categories.data)
                } else {
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), categories.message[0])
                }
            }
        })
    }

    fun removeProductServiceApi(activity: Activity?, imageUpdate: Boolean, listener : IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_id", 0)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addProductServices(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("removeProductServiceApi Response:->> ${body.toString()}")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
//                    listener.updateBasicDetails(basicDetailsResponse, imageUpdate)

                }  else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity ).onBackPressed() }
                }
            }
        })
    }

    fun deleteCategoriesApi(activity: Activity?, imageUpdate: Boolean, listener : IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_id", 0)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addProductServices(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("removeProductServiceApi Response:->> ${body.toString()}")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    bizCategoryList(mActivity,listener)

                }  else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity ).onBackPressed() }
                }
            }
        })
    }
}


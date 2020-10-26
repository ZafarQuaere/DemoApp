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
//                val body = JSONObject(Gson().toJson(response.body()))
//                LogUtils.DEBUG("BizList Response:->> ${body.toString()}")
//                var status = body.optInt("status")
                val status = response.body()!!.status
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    LogUtils.DEBUG("bizAmenityList response: "+response.body()!!.message)
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), response.body()!!.message[0])
                    listener.updateAmenitiesSection()
                } else {
//                    val jsonArray = body.optJSONArray("message")
//                    val message = jsonArray!!.get(0) as String
                    val message = response.body()!!.message[0]
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
            }
        })
    }

    fun bizImageList(activity: Activity?, listener: IMyBusinessLatest) {
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
                LogUtils.DEBUG("BizList Response:->> ${body.toString()}")
                var status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val data = ParseManager.getInstance().fromJSON(body.toString(), ViewImages::class.java)
                    listener.updateBannerImage(data.data)
                    PrefUtil.saveImageData(mActivity, response.toString())
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
                if (response.body()!!.status == AppConstant.SUCCESS) {
                    LogUtils.DEBUG("bizOperatingHours response: "+response.body()!!.message)
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), response.body()!!.message[0])
                    listener.updateOperatingHours()
                } else {
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), response.body()!!.message[0])
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
        val requestCall = categoryService.bizBrochureList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizBrochureList Response:->> $body")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
//                    val bizListData: BizListData = ParseManager.getInstance().fromJSON(body, BizListData::class.java)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
            }
        })
    }

    fun bizProductServicesList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizProductServicesList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("BizList Response:->> ${body.toString()}")
                var status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
//                    val bizListData: BizListData = ParseManager.getInstance().fromJSON(body, BizListData::class.java)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
            }
        })
    }

    fun bizPaymentMethodList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPaymentList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("BizList Response:->> ${body.toString()}")
                var status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
//                    val bizListData: BizListData = ParseManager.getInstance().fromJSON(body, BizListData::class.java)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
            }
        })
    }

    fun bizPricingList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPricingList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("BizList Response:->> ${body.toString()}")
                var status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
//                    val bizListData: BizListData = ParseManager.getInstance().fromJSON(body, BizListData::class.java)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
            }
        })
    }

    fun bizCategoryList(activity: Activity?, listener: IMyBusinessLatest) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizCategoryList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("BizList Response:->> ${body.toString()}")
                var status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
//                    val bizListData: BizListData = ParseManager.getInstance().fromJSON(body, BizListData::class.java)
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
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
                LogUtils.DEBUG("Login Response:->> ${body.toString()}")
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
}


package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.*
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.ui.presenters.operations.IProductNService
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBusinessViewModel : BaseViewModel() {

    lateinit var mActivity: Activity
    var basicDetailsData = MutableLiveData<MutableList<BasicDetailsData>>()
    lateinit var basicDetailsResponse: LiveData<BasicDetailsResponse>

    fun callMyBizBasicDetails(activity: Activity?, imageUpdate: Boolean, listener: IMyBusinessLatest?, ownerId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", ownerId)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.bizBasicDetails(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: ${jsonObject.toString()}")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("callBasicDetailsApi Failure: ${t.localizedMessage}")
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
                    listener?.updateBasicDetails(basicDetailsResponse, imageUpdate)
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { /*(mActivity).onBackPressed()*/ }
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
                    imageList.value = data.data
                    listener.updateBannerImage(data.data)
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

    suspend fun callDeleteImageApi(mContext: Context, imageData: ViewImageData?, position: Int) {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_DELETE_IMAGE
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token", Utils.getAccessToken(mContext))
            jObj.put("owner_id", Utils.getUserID(mContext))
            jObj.put("img_id", imageData?.imgId)
        } catch (e: JSONException) {
            e.printStackTrace();
        }
        LogUtils.DEBUG("URL : $url\nRequest Body ::${jObj.toString()}")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, jObj, { response: JSONObject? ->
            LogUtils.DEBUG("DeletePhoto Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0))
                        isImageDeleted.value = true
                        ImageUpdateModelListener.getInstance().changeState(true)
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok),
                                response.optJSONArray("message").optString(0))
                    }
                    loader.dismiss()
                }
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, { error: VolleyError ->
            LogUtils.DEBUG("DeletePhoto Error ::" + error.message)
            loader.dismiss()
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "DeletePhoto")
    }

    fun bizOperatingHours(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizOperatingHours("21"/*bizId*/)
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
        val requestCall = categoryService.bizBrochureList("21"/*bizId*/)
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
        val requestCall = categoryService.bizProductServicesList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<ProductNService> {
            override fun onFailure(call: Call<ProductNService>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizProductServicesList() Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<ProductNService>, response: Response<ProductNService>) {
                LogUtils.DEBUG("bizProductServicesList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
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

    fun addProductServicesApi(activity: Activity?, imageUpdate: Boolean, listener: IProductNService?, prodNService: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_name", prodNService)

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
                    listener?.updateProductServices()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity).onBackPressed() }
                }
            }
        })
    }

    fun removeProductOrService(activity: Activity?, prodId: String, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_id", Integer.parseInt(prodId))

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removeProductServices(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("removeProductServiceApi Response:->> $body")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    bizProductServicesList(mActivity, listener,bizId)
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity).onBackPressed() }
                }
            }
        })
    }
}


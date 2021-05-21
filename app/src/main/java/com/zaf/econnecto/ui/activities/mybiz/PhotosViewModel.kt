package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.IPaymentOptionList
import com.zaf.econnecto.ui.interfaces.PaymentMethodAddListener
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

class PhotosViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mActivity: Activity
    var mbImageList =  MutableLiveData<Response<JsonObject>>()
    var removePayOption =  MutableLiveData<Response<JsonObject>>()
    var addPayOption =  MutableLiveData<Response<JsonObject>>()


    fun bizImageList(activity: Activity?, bizId: String) {
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

                mbImageList.value = response


                var status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val data = ParseManager.getInstance().fromJSON(body.toString(), ViewImages::class.java)
//                    listener!!.updateBannerImage(data.data)
//                    PrefUtil.saveImageData(mActivity, response.toString())
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
//                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
                loader.dismiss()
            }
        })
    }

    fun removePayType(activity: Activity?, payMethodId: String, listener: IMyBusinessLatest?, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("p_method_id", payMethodId)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removePayType(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                removePayOption.value = response
            }
        })
    }


    fun addPaymentMethodsApi(activity: Activity?, listener: PaymentMethodAddListener?, paymentData: GeneralPaymentMethods) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("p_method_id", paymentData.p_method_id)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addPaymentMethods(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("addPaymentMethodsApi failure:->> ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("addPaymentMethodsApi Response:->> $body")
                loader.dismiss()
                addPayOption.value = response
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    listener?.updatePaymentMethod()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) {}
                }
            }
        })
    }

    fun bizAllPaymentTypes(activity: Activity?, listener: IPaymentOptionList) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val eConnectoServices = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = eConnectoServices.allPaymentMethods()
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<AllPaymentMethods> {
            override fun onFailure(call: Call<AllPaymentMethods>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<AllPaymentMethods>, response: Response<AllPaymentMethods>) {
                LogUtils.DEBUG("AllPaymentMethods response: " + ParseManager.getInstance().toJSON(response.body()))
                loader.dismiss()
                if (response.isSuccessful) {
                    val paymentMethods = response.body()
                    if (paymentMethods?.status == AppConstant.SUCCESS) {
                        listener.updatePaymentListUI(paymentMethods!!.data)
                    } else {
                        listener.updatePaymentListUI(null)
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), paymentMethods!!.message)
                    }
                }
            }
        })
    }


    suspend fun callDeleteImageApi(mContext: Context, imageData: MutableList<ViewImageData>, position: Int) {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_DELETE_IMAGE
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token", Utils.getAccessToken(mContext))
            jObj.put("owner_id", Utils.getUserID(mContext))
            jObj.put("img_id", imageData[position].imgId)
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
//                        imageList.remove(imageData)
//                        adapter.notifyDataSetChanged()
//                        recyclerPhotos.removeViewAt(position)
//                        adapter.notifyItemRemoved(position)
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
}
package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.IPaymentOptionList
import com.zaf.econnecto.ui.interfaces.PaymentMethodAddListener
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

class PricingViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mActivity: Activity
    var mbPricingList =  MutableLiveData<Pricing>()
    var removePayOption =  MutableLiveData<Response<JsonObject>>()
    var addPayOption =  MutableLiveData<Response<JsonObject>>()

    fun bizPricingList(activity: Activity?, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPricingList("21"/*bizId*/)
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
                    mbPricingList.value = response.body()
                    /*val pricing: Pricing = response.body()!!
                    if (pricing.status == AppConstant.SUCCESS) {
                        listener.updatePricingSection(pricing.data)
                    } else {
                        listener.updatePricingSection(null)
//                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), pricing.message[0])
                    }*/
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
}
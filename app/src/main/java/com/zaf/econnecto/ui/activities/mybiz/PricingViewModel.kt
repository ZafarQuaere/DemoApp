package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.activities.mybiz.fragments.PricingFragment
import com.zaf.econnecto.ui.interfaces.PricingAddedListener
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
    var removePricing =  MutableLiveData<Response<JsonObject>>()

    fun bizPricingList(activity: Activity?, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPricingList(bizId)
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

    fun removePricing(activity: Context, payMethodId: String) {
        val loader = AppDialogLoader.getLoader(activity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(activity))
        jsonObject.put("owner_id", Utils.getUserID(activity))
        jsonObject.put("prod_serv_id", payMethodId)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removePricing(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                PricingFragment.removePricing = true
                removePricing.value = response
            }
        })
    }


    fun callAddPricingApi(activity: Activity?, listener: PricingAddedListener, desc: String, price: String, unit: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_name", desc)
        jsonObject.put("prod_serv_price", price)
        jsonObject.put("unit", unit)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addPricing(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("AddPricingApi Response:->> ${body.toString()}")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    listener.updatePricing()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) {  }
                }
            }
        })
    }
}
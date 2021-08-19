package com.zaf.econnecto.version2.ui.home

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.model.HomeResponse
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.activities.mybiz.BaseViewModel
import com.zaf.econnecto.ui.activities.mybiz.Pricing
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import com.zaf.econnecto.utils.parser.ParseManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : BaseViewModel() {

    fun callHomeApi(activity: Activity?) {
        var loader = AppDialogLoader.getLoader(activity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.homeScreenData()
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<HomeResponse> {
            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("HomeResponse Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                LogUtils.DEBUG("HomeResponse Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    homeResponse.value = response.body()
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

    fun callBizListApi(activity: Activity) {
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.getBusinessList(Utils.getUserID(activity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                LogUtils.ERROR(activity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("BizList Response:->> ${body.toString()}")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS_501) {
                    val bizData: BizListData = ParseManager.getInstance().fromJSON(body, BizListData::class.java)
                    bizListData.value = bizData
                } else {
                    bizListData.value = null
                }
            }
        })
    }
}

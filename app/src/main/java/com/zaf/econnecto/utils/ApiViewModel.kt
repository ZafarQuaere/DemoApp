package com.zaf.econnecto.utils

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import com.zaf.econnecto.network_call.response_model.login.LoginData
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.activities.PhoneVerificationActivity
import com.zaf.econnecto.ui.presenters.operations.IFragListing
import com.zaf.econnecto.utils.parser.ParseManager
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel : ViewModel() {

    lateinit var mActivity: Activity

    fun callBizListApi(activity: Activity?, iFragListing: IFragListing) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.getBusinessList(Utils.getUserEmail(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  ")

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
                if (status == AppConstant.SUCCESS_501) {
                    val bizListData: BizListData = ParseManager.getInstance().fromJSON(body, BizListData::class.java)
                    iFragListing.updateList(bizListData.data)
                } else {
                    iFragListing.updateList(null)
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
            }
        })
    }


    fun callLoginApi(activity: Activity?, vmTempInterface: VmTempInterface, phoneNo: String, password: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        var jsonObject = JSONObject()
        jsonObject.put("phone", phoneNo);
        jsonObject.put("password", password);
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.login(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: ${jsonObject.toString()}")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("Login Response:->> ${body.toString()}")
                var status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    Utils.setLoggedIn(activity, true)
                    val loginData = ParseManager.getInstance().fromJSON(body.toString(), LoginData::class.java)
                    Utils.saveLoginData(activity, body.toString())
                    storeOtherValue(activity, loginData)
                    vmTempInterface.doLoginFromVm()
                } else if (status == AppConstant.PHONE_NOT_VERIFIED) {
                    LogUtils.showDialogSingleActionButton(activity, activity!!.getString(R.string.ok), activity.getString(R.string.your_phone_number_is_not_verified_plz_verify_it)) {
                        activity.startActivity(Intent(activity, PhoneVerificationActivity::class.java).putExtra("mobile", phoneNo))
                    }
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
            }
        })
    }

    private fun storeOtherValue(mContext: Activity?, loginData: LoginData) {
        Utils.storeProfileImage(mContext, loginData)
        Utils.setBusinessStatus(mContext, loginData.data.businessStatus)
        Utils.setEmailVerified(mContext, loginData.data.isEmailVerified == "1")
        DateUtils.setLoginDate(mContext)
    }
}
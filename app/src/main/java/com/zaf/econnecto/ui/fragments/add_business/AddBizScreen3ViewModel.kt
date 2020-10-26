package com.zaf.econnecto.ui.fragments.add_business

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.DialogButtonClick
import com.zaf.econnecto.ui.interfaces.DialogSingleButtonListener
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddBizScreen3ViewModel : ViewModel() {

    private var mContext: Activity? = null

    fun callAddBizApi(activity: Activity?, addressInfo: AddressInfo, mobileNo: String, emailId: String, alternateMobile: String, telephone: String, website: String) {
        mContext = activity
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
         var myWebsite = website
        if (myWebsite.isNotEmpty()){
            myWebsite = "http://$website"
        }
        var category = addressInfo.category1
        val category2 = addressInfo.category2
        val category3 = addressInfo.category3
        if (category.isNullOrEmpty()) {
            category = if (category2!!.isNotEmpty()) category2
            else category3
        }
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mContext))
        jsonObject.put("owner_id", Utils.getUserID(mContext))
        jsonObject.put("business_name", addressInfo.bizName)
        jsonObject.put("short_description", addressInfo.shortDesc)
        jsonObject.put("category_id_1", category)
        jsonObject.put("category_id_2", category2)
        jsonObject.put("category_id_3", category3)
        jsonObject.put("year_established", addressInfo.estdYear)
        jsonObject.put("address_1", addressInfo.address1)
        jsonObject.put("address_2", addressInfo.address2)
        jsonObject.put("landmark", addressInfo.landmark)
        jsonObject.put("pin_code", addressInfo.pincode)
        jsonObject.put("area_locality", addressInfo.locality)
        jsonObject.put("city_town", addressInfo.city)
        jsonObject.put("state", addressInfo.state)
        jsonObject.put("country", addressInfo.country)
        jsonObject.put("is_outside_serve", 2)//1 if server outside, 2 if not
        jsonObject.put("mobile_1", mobileNo)
        jsonObject.put("mobile_2", alternateMobile)
        jsonObject.put("telephone", telephone)
        jsonObject.put("email", emailId)
        jsonObject.put("website", myWebsite)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val destinationService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = destinationService.addYourBusiness(requestBody)

        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext, mContext!!.getString(R.string.ok), mContext!!.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                var status = body.optInt("status")
                loader.dismiss()

                if (status == AppConstant.SUCCESS_501) {
                    Utils.setBusinessStatus(mContext,"1")
                    AppConstant.BIZNESS_ADDED = true
                    LogUtils.showDialogSingleActionButton(mContext,mContext!!.getString(R.string.ok),mContext!!.getString(R.string.add_business_success_msg)) { mContext!!.finish(); };

                } else {
                    LogUtils.showErrorDialog(mContext!!, mContext!!.getString(R.string.ok), body.optJSONArray("message").optString(0));
            }
            }
        })
    }
}

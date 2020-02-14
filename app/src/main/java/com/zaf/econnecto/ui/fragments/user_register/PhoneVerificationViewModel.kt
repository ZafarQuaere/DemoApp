package com.zaf.econnecto.ui.fragments.user_register

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.zaf.econnecto.R
import com.zaf.econnecto.service.BusinessListService
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.DialogButtonClick
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneVerificationViewModel : ViewModel() {



    fun callResendApi(mContext: Activity, mobileNo: String) {
        var loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        //TODO request OTP API call
        var jsonObject = JSONObject()
        jsonObject.put("action","request_otp")
        jsonObject.put("phone",mobileNo)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())


        val destinationService = ServiceBuilder.buildConnectoService(BusinessListService::class.java)
        val requestCall = destinationService.phoneVerification(requestBody)

        requestCall.enqueue(object : Callback<JSONObject> {
            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),mContext.getString(R.string.something_wrong_from_server_plz_try_again))
            }

            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                val body = response.body()
                loader.dismiss()
                if(body!!.getInt("status")== AppConstant.FAILURE){
                    val jsonArray = body!!.getJSONArray("message")
                    val message = jsonArray.get(0) as String
                    LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),message)
                    // KotUtil.displayResponseError(mContext,message.toString())
                }else{
                    LogUtils.showToast(mContext,"Please check your mobile, OTP has been sent")
                }
            }

        })
    }

    fun callVerifyPhoneApi(mContext: Activity, mobileNo: String, OTP: String) {
        var loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        //TODO request OTP API call
        var jsonObject = JSONObject()
        jsonObject.put("action","validate_otp")
        jsonObject.put("phone",mobileNo)
        jsonObject.put("otp",OTP)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())


        val destinationService = ServiceBuilder.buildConnectoService(BusinessListService::class.java)
        val requestCall = destinationService.phoneVerification(requestBody)

        requestCall.enqueue(object : Callback<JSONObject> {
            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),mContext.getString(R.string.something_wrong_from_server_plz_try_again))
            }

            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                val body = response.body()
                loader.dismiss()
                if(body!!.getInt("status")== AppConstant.FAILURE){
                    val jsonArray = body!!.getJSONArray("message")
                    val message = jsonArray.get(0) as String
                    LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),message)
                    // KotUtil.displayResponseError(mContext,message.toString())
                }else{
                    LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.register_successful_plz_login), object : DialogButtonClick {
                        override fun onOkClick() {
                           mContext.finish()
                        }

                        override fun onCancelClick() {}
                    })
                }
            }

        })
    }

}

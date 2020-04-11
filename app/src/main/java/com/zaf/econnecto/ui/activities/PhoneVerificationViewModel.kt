package com.zaf.econnecto.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.DialogSingleButtonListener
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

        var jsonObject = JSONObject()
        jsonObject.put("action", "request_otp")
        jsonObject.put("phone", mobileNo)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        val destinationService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = destinationService.phoneVerification(requestBody)

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.something_wrong_from_server_plz_try_again))
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(response.body().toString())
                LogUtils.DEBUG("status : ${body!!.optInt("status")}   message ${body!!.optJSONArray("message").get(0)}")
                var status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.FAILURE) {
                    val jsonArray = body!!.getJSONArray("message")
                    val message = jsonArray.get(0) as String
                    LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), message)
                    // KotUtil.displayResponseError(mContext,message.toString())
                } else {
                    LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok), mContext.getString(R.string.otp_is_sent_to_your_mobile_no_plz_enter_the_otp))
                }
            }
        })
    }

    fun callVerifyPhoneApi(mContext: Activity, mobileNo: String, OTP: String) {
        var loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        var jsonObject = JSONObject()
        jsonObject.put("action", "validate_otp")
        jsonObject.put("phone", mobileNo)
        jsonObject.put("otp", OTP)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val destinationService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = destinationService.phoneVerification(requestBody)
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.something_wrong_from_server_plz_try_again))
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(response.body().toString())
                LogUtils.DEBUG("status : ${body!!.optInt("status")}   message ${body!!.optJSONArray("message").get(0)}")
                var status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.FAILURE) {
                    val jsonArray = body!!.getJSONArray("message")
                    val message = jsonArray.get(0) as String
                    LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), message)
                    // KotUtil.displayResponseError(mContext,message.toString())
                } else {
                    LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.register_successful_plz_login), object : DialogSingleButtonListener {
                        override fun okClick() {
                            mContext.finish()
                        }
                    })
                }
            }
        })
    }


    public fun callRequestOTPApi(mContext: Activity, mobileNo: String) {
        var loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        var jsonObject = JSONObject()
        jsonObject.put("action","request_otp")
        jsonObject.put("phone",mobileNo)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        val destinationService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = destinationService.phoneVerification(requestBody)

        requestCall.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),mContext.getString(R.string.something_wrong_from_server_plz_try_again))
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                val body = JSONObject(response.body().toString())
                LogUtils.DEBUG("status : ${body!!.optInt("status")}   message ${body!!.optJSONArray("message").get(0)}")
                var status = body.optInt("status")
                if(status == AppConstant.FAILURE){
                    val jsonArray = body!!.getJSONArray("message")
                    val message = jsonArray.get(0) as String
                    LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),message)
                    // KotUtil.displayResponseError(mContext,message.toString())
                }else{
                    LogUtils.showToast(mContext,"an OTP has been sent to your mobile number")
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateTimerUI(mActivity: Activity, txtResendOTP: TextView, chronoResendTime: Chronometer, time: Int) {
        txtResendOTP.isEnabled = false
        chronoResendTime.visibility = View.VISIBLE
        chronoResendTime.isCountDown = true
        chronoResendTime.base = SystemClock.elapsedRealtime() + time
        chronoResendTime.start()
        val timer: CountDownTimer = object : CountDownTimer(time.toLong(), 100) {
            override fun onTick(l: Long) {}

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                chronoResendTime.stop()
                chronoResendTime.visibility = View.GONE
                txtResendOTP.isEnabled = true
                txtResendOTP.setTextColor(mActivity.resources.getColor(R.color.colorPrimary))
            }
        }
        timer.start()
    }
}

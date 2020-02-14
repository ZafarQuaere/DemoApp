package com.zaf.econnecto.ui.activities

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.UserRegisterResponse
import com.zaf.econnecto.service.BusinessListService
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.fragments.user_register.UserRegisterFragment
import com.zaf.econnecto.ui.interfaces.FragmentNavigation
import com.zaf.econnecto.utils.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRegisterViewModel : ViewModel() {

     private lateinit var mSelectedGender : String
     private lateinit var fragNavigation : FragmentNavigation

    fun validateUI(mContext: Activity, editEmail: TextInputEditText, editPassword: TextInputEditText,
                   editConfirmPassword: TextInputEditText, radioGroup: RadioGroup,
                   editBirthYear: TextInputEditText, editPhone: TextInputEditText) {

        var email = editEmail.text.toString().trim()
        var mobileNo = editPhone.text.toString().trim()
        var password = editPassword.text.toString().trim()
        var confirmPassword = editConfirmPassword.text.toString().trim()
        var dobYear = editBirthYear.text.toString().trim()
        var gender  = 1

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRb = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = selectedRb.isChecked
            if (isChecked) {
                mSelectedGender = selectedRb.text.toString().trim { it <= ' ' }
                if (mSelectedGender == "Male")
                    gender =1
                else if (mSelectedGender == "Female")
                    gender = 2
                    else if (mSelectedGender == "Other")
                    gender =3
                LogUtils.showToast(mContext, "$mSelectedGender  and Code is $gender")
            }
        }
       if (email.isNullOrEmpty() || !Utils.isValidEmail(email)) {
            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_valid_email))
        } else if (mobileNo.isNullOrEmpty() || mobileNo.length < 10) {
            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_valid_mobile_number))
        } else if (password.isNullOrEmpty() || password.length < 8) {
            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.enter_valid_password))
        } else if (confirmPassword.isNullOrEmpty() || confirmPassword!! != password) {
            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_same_pswd))
        } else if (!KotUtil.validateDOB(dobYear)) {
            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.enter_valid_year_of_birth))
        }else{
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                callRegisterApi(mContext,email,mobileNo,password,dobYear,gender)
            } else {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_check_your_network_connection))
            }
        }
    }


    private fun callRegisterApi(mContext: Context,email: String, mobileNo: String, password: String, dobYear: String, gender: Int) {

       /* val userData = UserRegisterData()
        userData.email = email
        userData.phone = mobileNo
        userData.password = password
        userData.year_of_birth = Integer.parseInt(dobYear)
        userData.gender = gender*/
        var jsonObject = JSONObject()
        jsonObject.put("email",email)
        jsonObject.put("phone",mobileNo)
        jsonObject.put("password",password)
        jsonObject.put("year_of_birth",Integer.parseInt(dobYear))
        jsonObject.put("gender",gender)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        var loader = AppDialogLoader.getLoader(mContext)
        loader.show()

        val destinationService = ServiceBuilder.buildConnectoService(BusinessListService::class.java)
        val requestCall = destinationService.registerUser(requestBody)

        requestCall.enqueue(object : Callback<UserRegisterResponse>{
            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),mContext.getString(R.string.something_wrong_from_server_plz_try_again))
            }

            override fun onResponse(call: Call<UserRegisterResponse>, response: Response<UserRegisterResponse>) {
                val body = response.body()
                loader.dismiss()
                if(body!!.getStatus()== AppConstant.FAILURE){
                    KotUtil.displayResponseError(mContext,body!!.getMessage())
                }else{
                    callRequestOTPApi(mContext,mobileNo)
                   // fragNavigation.navigate()
                }
            }

        })
    }

    private fun callRequestOTPApi(mContext: Context, mobileNo: String) {
        var loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        //TODO request OTP API call
        var jsonObject = JSONObject()
        jsonObject.put("action","request_otp")
        jsonObject.put("phone",mobileNo)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())


        val destinationService = ServiceBuilder.buildConnectoService(BusinessListService::class.java)
        val requestCall = destinationService.phoneVerification(requestBody)

        requestCall.enqueue(object : Callback<JSONObject>{
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
                    fragNavigation.navigate()
                }
            }

        })
    }

    fun registerNavigation(userFragment: UserRegisterFragment) {
        fragNavigation = userFragment
    }

}

package com.zaf.econnecto.ui.fragments.user_register

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.DialogButtonClick
import com.zaf.econnecto.ui.interfaces.DialogSingleButtonListener
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

        val destinationService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = destinationService.registerUser(requestBody)

        requestCall.enqueue(object : Callback<JsonObject>{

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),mContext.getString(R.string.something_wrong_from_server_plz_try_again) +"\n"+ t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("User Register Response >>: >>  ${body.toString()}")
                LogUtils.DEBUG(" >>>  status : ${body!!.optInt("status")}  message>>  ${body.optJSONArray("message")!!.get(0)}")
                   // LogUtils.DEBUG("status : ${body!!.optInt("status")}   message ${body!!.getJSONArray("message")} Data : ${body!!.optInt("data")}")
                var status = body.optInt("status")
                loader.dismiss()

                if(status == AppConstant.SUCCESS){
                    LogUtils.showDialogSingleActionButton(mContext,mContext.getString(R.string.ok),mContext.getString(R.string.you_are_successfully_registered_plz_verify_your_phone_no),object :DialogSingleButtonListener{
                        override fun okClick() {
                             fragNavigation.navigate()
                        }
                    })
                }else{
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mContext,mContext.getString(R.string.ok),message)
                }
            }

        })
    }

    fun registerNavigation(userFragment: UserRegisterFragment) {
        fragNavigation = userFragment
    }

}

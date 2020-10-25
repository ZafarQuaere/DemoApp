package com.zaf.econnecto.ui.presenters

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.my_business.AddDealsBg
import com.zaf.econnecto.network_call.response_model.my_business.MyBusiness
import com.zaf.econnecto.ui.presenters.operations.IEditDetails
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import org.json.JSONObject



class EditDetailsPresenter(private val mContext: Context, private val iEditDetails: IEditDetails) : BasePresenter(mContext) {
    private var bizDetailData: MyBusiness? = null



    fun validateBasicInputs(bizName: String, shortDesc: String, estdYear: String) {
        when {
            bizName.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.enter_valid_business_name))
            }
            shortDesc.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_a_short_description))
            }
            bizName.length < 3 -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.enter_valid_business_name))
            }
            estdYear.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_foundation_year_of_business))
            }
            !KotUtil.validateEstd(estdYear.toInt()) -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.enter_valid_establishment_year))
            }
            else -> {
                updateBasicInfoApi(bizName,shortDesc,estdYear)
            }
        }


    }

    private fun updateBasicInfoApi(bizName: String, shortDesc: String, estdYear: String) {
        val url = AppConstant.URL_EDIT_BIZ_DETAILS
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mContext))
        jsonObject.put("owner_id",Utils.getUserID(mContext))
        jsonObject.put("business_name",bizName)
        jsonObject.put("short_description",shortDesc)
        jsonObject.put("year_established",estdYear)
        
        LogUtils.DEBUG("URL : $url\nRequest Body :: ${jsonObject.toString()}")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, jsonObject, { response: JSONObject? ->
            LogUtils.DEBUG("BasicInfo Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        iEditDetails.updateBusinessDetails(response.optString("message"))
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, { error: VolleyError -> LogUtils.DEBUG("BasicInfo Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "BasicInfo")
    }

    fun validateAddressInputs(address1: String,address2: String,landmark: String, pincode: String, locality: String, city: String, state: String, country: String) {
        when {
            address1.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_address))
            }
            pincode.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_valid_pincode))
            }
            locality.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_locality))
            }
            city.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_city_name))
            }
            state.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_state))
            }
            country.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_country))
            }
            else -> {
                updateAddressInfoApi(address1,address2,landmark,pincode,locality,city,state,country)
            }
        }
    }

    private fun updateAddressInfoApi(address1: String, address2: String, landmark: String, pincode: String, locality: String, city: String, state: String, country: String) {
        val url = AppConstant.URL_EDIT_ADDRESS_DETAILS
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mContext))
        jsonObject.put("owner_id",Utils.getUserID(mContext))
        jsonObject.put("address_1",address1)
        jsonObject.put("address_2",address2)
        jsonObject.put("landmark",landmark)
        jsonObject.put("pin_code",pincode)
        jsonObject.put("area_locality",locality)
        jsonObject.put("city_town",city)
        jsonObject.put("state",state)
        jsonObject.put("country",country)

        LogUtils.DEBUG("URL : $url\nRequest Body :: $jsonObject")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, jsonObject, { response: JSONObject? ->
            LogUtils.DEBUG("updateAddressInfoApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        iEditDetails.updateAddressDetails(response.optString("message"))
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, { error: VolleyError -> LogUtils.DEBUG("updateAddressInfoApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "updateAddressInfoApi")
    }

    fun validateContactInputs(mobileNo: String, alternateMobile: String, telephone: String, emailId: String, website: String) {
        when {
            mobileNo.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.enter_mobile_no))
            }
            emailId.isEmpty() -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.enter_email))
            }
            !Utils.isValidEmail(emailId) -> {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enter_valid_email))
            }
            else -> {
                //Call Api
                updateContactInfoApi(mContext, mobileNo, emailId, alternateMobile, telephone, website)
            }
        }
    }

    private fun updateContactInfoApi(mContext: Context, mobileNo: String, emailId: String, alternateMobile: String, telephone: String, website: String) {
        val url = AppConstant.URL_EDIT_CONTACT_DETAILS
        val jsonObject = JSONObject()

        jsonObject.put("jwt_token", Utils.getAccessToken(mContext))
        jsonObject.put("owner_id",Utils.getUserID(mContext))
        jsonObject.put("mobile_1",mobileNo)
        jsonObject.put("mobile_2",alternateMobile)
        jsonObject.put("telephone",telephone)
        jsonObject.put("email",emailId)
        jsonObject.put("website",website)

        LogUtils.DEBUG("URL : $url\nRequest Body :: $jsonObject")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, jsonObject, { response: JSONObject? ->
            LogUtils.DEBUG("ContactInfo Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        iEditDetails.updateContactDetails(response.optString("message"))
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, { error: VolleyError -> LogUtils.DEBUG("ContactInfo Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "ContactInfo")
    }

}

package com.zaf.econnecto.ui.presenters

import android.app.Activity
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.my_business.AddDealsBg
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.network_call.response_model.my_business.MyBusiness
import com.zaf.econnecto.ui.presenters.operations.IEditDetails
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import org.json.JSONException
import org.json.JSONObject



class EditDetailsPresenter(private val mContext: Context, private val iEditDetails: IEditDetails) : BasePresenter(mContext) {
    private var bizDetailData: MyBusiness? = null
    private var addDealsBg: AddDealsBg? = null


    fun callBasicDetailsApi(imageUpdate: Boolean) {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_BIZ_BASIC_DETAILS
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token", Utils.getAccessToken(mContext))
            jObj.put("owner_id", Utils.getUserID(mContext))
        } catch (e: JSONException) {
            e.printStackTrace();
        }
        LogUtils.DEBUG("URL : $url\nRequest Body ::${jObj.toString()}")

        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, jObj, Response.Listener { response ->
            LogUtils.DEBUG("MyBusiness Response ::$response")
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        val basicDetailsResponse = ParseManager.getInstance().fromJSON(response.toString(), BasicDetailsResponse::class.java)
                        PrefUtil.setBizId(mContext, basicDetailsResponse.data[0].businessId)
                        iEditDetails.updateBusinessDetails("")

                    } else {
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok),
                                response.optJSONArray("message").optString(0)) {
                            (mContext as Activity).onBackPressed() }
                    }
                }
                loader.dismiss()
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error ->
            loader.dismiss()
            LogUtils.DEBUG("MyBusiness Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
    }

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
                updateBasicInfoApi()
            }
        }

        fun callAddressInfoApi() {
            val url = AppConstant.URL_BIZ_BROCHURE + PrefUtil.getBizId(mContext)
            LogUtils.DEBUG("URL : $url")
            val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
                LogUtils.DEBUG("Deal Background Response ::" + response.toString())
                try {
                    if (response != null) {
                        val status = response.optInt("status")
                        if (status == AppConstant.SUCCESS) {
                            addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                        } else {
                            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    LogUtils.ERROR(e.message)
                }
            }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("MyBusiness Error ::" + error.message) })
            AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
        }

        fun callContactInfoApi() {
            val url = AppConstant.URL_BIZ_AMENITIES + PrefUtil.getBizId(mContext)
            LogUtils.DEBUG("URL : $url\nRequest Body ::$")
            val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, null, Response.Listener { response ->
                LogUtils.DEBUG("UpdateBusiness Response ::$response")
                try {
                    if (response != null) {
                        val status = response.optInt("status")
                        if (status == AppConstant.SUCCESS) {
//                        iMyBusiness.updateBizData(address, mobile, email, website, shortDesc, detailDesc)
                        } else {
                            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    LogUtils.ERROR(e.message)
                }
            }, Response.ErrorListener { error ->
                LogUtils.DEBUG("UpdateBusiness Error ::" + error.message)
            })
            AppController.getInstance().addToRequestQueue(objectRequest, "UpdateBusiness")
        }


    }

    private fun updateBasicInfoApi() {
        val url = AppConstant.URL_BIZ_BROCHURE + PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("Deal Background Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        iEditDetails.updateBusinessDetails("")
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("MyBusiness Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
    }

    fun validateAddressInputs(address1: String, pincode: String, locality: String, city: String, state: String, country: String) {
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
                updateAddressInfoApi()
            }
        }
    }

    private fun updateAddressInfoApi() {
        val url = AppConstant.URL_BIZ_BROCHURE + PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("Deal Background Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        iEditDetails.updateAddressDetails("")
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("MyBusiness Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
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
        val url = AppConstant.URL_BIZ_BROCHURE + PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("Deal Background Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        iEditDetails.updateContactDetails("")
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("MyBusiness Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
    }

}

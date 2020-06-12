package com.zaf.econnecto.ui.presenters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.textfield.TextInputEditText
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.AddDealsBg
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.network_call.response_model.my_business.MyBusiness
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData
import com.zaf.econnecto.ui.activities.ViewBusinessActivity
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import org.json.JSONException
import org.json.JSONObject

class MyBusinessPresenterLatest(private val mContext: Context, private val iMyBusiness: IMyBusinessLatest) : BasePresenter(mContext) {
    private val loader: AppLoaderFragment = AppLoaderFragment.getInstance(mContext)
    private var bizDetailData: MyBusiness? = null
    private var addDealsBg: AddDealsBg? = null


    fun callBasicDetailsApi(imageUpdate: Boolean) {
        loader.show()
        val url = AppConstant.URL_BASE_MVP + AppConstant.URL_MY_BUSINESS_BASIC
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token",Utils.getAccessToken(mContext))
            jObj.put("owner_id",Utils.getUserID(mContext))
        } catch (e : JSONException) {
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
                        PrefUtil.setBizId(mContext,basicDetailsResponse.data[0].businessId)
                        iMyBusiness.updateBasicDetails(basicDetailsResponse,imageUpdate)

                    } else {
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0)) { (mContext as Activity).onBackPressed() }
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

    fun showUpdateBizDialog() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setWindowAnimations(R.style.dialogUpDownAnimation)
        dialog.setContentView(R.layout.dialog_update_biz)
        val editAddress = dialog.findViewById<View>(R.id.editAddress) as TextInputEditText
        val editMobile = dialog.findViewById<View>(R.id.editMobile) as TextInputEditText
        val editEmail = dialog.findViewById<View>(R.id.editEmail) as TextInputEditText
        val editBizWebsite = dialog.findViewById<View>(R.id.editBizWebsite) as TextInputEditText
        val editSD = dialog.findViewById<View>(R.id.editShortDescription) as TextInputEditText
        val editDD = dialog.findViewById<View>(R.id.editLongDescription) as TextInputEditText
        val btnOk = dialog.findViewById<View>(R.id.btn_ok) as Button
        val btnCancel = dialog.findViewById<View>(R.id.btnCancel) as Button
        btnCancel.text = mContext.getString(R.string.cancel)
        btnOk.text = mContext.getString(R.string.update)
        btnOk.setOnClickListener {
            dialog.dismiss()
            validateFields(editAddress.text.toString().trim { it <= ' ' }, editMobile.text.toString().trim { it <= ' ' },
                    editEmail.text.toString().trim { it <= ' ' }, editBizWebsite.text.toString().trim { it <= ' ' },
                    editSD.text.toString().trim { it <= ' ' }, editDD.text.toString().trim { it <= ' ' })
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun validateFields(address: String, mobile: String, email: String, website: String, shortDesc: String, detailDesc: String) {
        var address = address
        var mobile = mobile
        var email = email
        var website = website
        var shortDesc = shortDesc
        val myBusinessData = bizDetailData!!.data[0]
        if (address.isEmpty()) {
            address = myBusinessData.address1
        }
        if (mobile.isEmpty()) {
            mobile = myBusinessData.mobile1
        }
        if (email.isEmpty()) {
            email = myBusinessData.email
        }
        if (website.isEmpty()) {
            website = myBusinessData.website
        }
        if (shortDesc.isEmpty()) {
            shortDesc = myBusinessData.shortDescription
        }
        callUpdateBizApi(address, mobile, email, website, shortDesc, detailDesc, myBusinessData)
    }

    private fun callUpdateBizApi(address: String, mobile: String, email: String, website: String, shortDesc: String, detailDesc: String, myBusinessData: MyBusinessData) {
        loader.show()
        val url = AppConstant.URL_BASE + AppConstant.URL_UPDATE_BUSINESS
        val `object` = JSONObject()
        try {
            `object`.put("owner_email", Utils.getUserEmail(mContext))
            `object`.put("short_description", shortDesc)
            //            object.put("business_category",myBusinessData.getBusinessCategory());
            `object`.put("detailed_description", detailDesc)
            //            object.put("year_founded",myBusinessData.getYearFounded());
            `object`.put("awards", "")
            `object`.put("address", address)
            `object`.put("phone1", mobile)
            `object`.put("phone2", myBusinessData.mobile2)
            `object`.put("business_email", email)
            `object`.put("website", website)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        LogUtils.DEBUG("URL : $url\nRequest Body ::$`object`")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, `object`, Response.Listener { response ->
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
                loader.dismiss()
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error ->
            loader.dismiss()
            LogUtils.DEBUG("UpdateBusiness Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "UpdateBusiness")
    }

    fun callDealBgApi() {
        val url = AppConstant.URL_DEAL_BACKGROUND
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
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

    fun callBannerImgApi() {
        val url = AppConstant.URL_VIEW_IMAGES + PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("View Image Response ::$response")
            val data = ParseManager.getInstance().fromJSON(response.toString(), ViewImages::class.java)
            if (data.status == AppConstant.SUCCESS) {
                iMyBusiness.updateBannerImage(data.data)
            } else {
                LogUtils.showToast(mContext, data.message.toString())
            }
        }, Response.ErrorListener { error ->
            LogUtils.DEBUG("Biz Category Error ::" + error.message)

        })
        AppController.getInstance().addToRequestQueue(objectRequest, "Biz Category")
    }

     fun initMap(mContext: MyBusinessActivityLatest, mapFrag: Fragment?) {
        val mapFragment = mContext.supportFragmentManager
                .findFragmentById(R.id.mapFrag) as SupportMapFragment
        mapFragment.getMapAsync(mContext)
        mapFrag!!.requireView().visibility = View.GONE
    }
}
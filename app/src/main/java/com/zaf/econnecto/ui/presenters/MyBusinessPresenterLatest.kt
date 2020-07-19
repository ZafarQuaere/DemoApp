package com.zaf.econnecto.ui.presenters

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.android.gms.maps.SupportMapFragment
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import org.json.JSONException
import org.json.JSONObject



class MyBusinessPresenterLatest(private val mContext: Context, private val iMyBusiness: IMyBusinessLatest) : BasePresenter(mContext) {


    fun callBasicDetailsApi(imageUpdate: Boolean) {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_BASE_MVP + AppConstant.URL_BIZ_BASIC_DETAILS
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token", Utils.getAccessToken(mContext))
            jObj.put("owner_id", Utils.getUserID(mContext))
        } catch (e: JSONException) {
            e.printStackTrace();
        }
        LogUtils.DEBUG("URL : $url\nRequest Body ::${jObj.toString()}")

        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, jObj, Response.Listener { response ->
            LogUtils.DEBUG("callBasicDetailsApi Response ::$response")
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        val basicDetailsResponse = ParseManager.getInstance().fromJSON(response.toString(), BasicDetailsResponse::class.java)
                        PrefUtil.setBasicDetailsData(mContext, response.toString())
                        iMyBusiness.updateBasicDetails(basicDetailsResponse, imageUpdate)
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
            LogUtils.DEBUG("callBasicDetailsApi Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "callBasicDetailsApi")
    }


    fun callAboutApi() {
        val url = AppConstant.URL_BASE_MVP + AppConstant.URL_BIZ_BROCHURE_LIST + "21"
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("callAboutApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("callAboutApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "callAboutApi")
    }

    fun callBrochureApi() {
        val url = AppConstant.URL_BIZ_BROCHURE_LIST + "21" //PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("callBrochureApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("callBrochureApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "callBrochureApi")
    }

    fun callAmenitiesApi() {
        val url = AppConstant.URL_BIZ_ADD_AMENITIES + "21" //PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::$")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, null, Response.Listener { response ->
            LogUtils.DEBUG("callAmenitiesApi Response ::$response")
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
            LogUtils.DEBUG("callAmenitiesApi Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "callAmenitiesApi")
    }

    fun callPaymentApi() {
        val url = AppConstant.URL_BIZ_PAYMENT + "21" //PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("callPaymentApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("callPaymentApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "callPaymentApi")
    }

    fun callPricingApi() {
        val url = AppConstant.URL_BIZ_PRICING + "21" //PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("callPricingApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("callPricingApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "callPricingApi")
    }

    fun callCategoriesApi() {
        val url = AppConstant.URL_BIZ_CATEGORIES + "21" //PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("callCategoriesApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("callCategoriesApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "callCategoriesApi")
    }

    fun callOperationTimeApi() {
        val url = AppConstant.URL_BASE_MVP+AppConstant.URL_BIZ_OPERATING_HOURS + "21" //PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("callOperationTimeApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("callOperationTimeApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "callOperationTimeApi")
    }

    fun callProdServicesApi() {
        val url = AppConstant.URL_BIZ_PROD_SERVICES + "21" //PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("callProdServicesApi Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("callProdServicesApi Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "callProdServicesApi")
    }

    fun callImageApi() {
        val url =  AppConstant.URL_BASE_MVP+AppConstant.URL_BIZ_IMAGES + PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("View Image Response ::$response")
            val status = response.optInt("status")
            if (status == AppConstant.SUCCESS) {
                val data = ParseManager.getInstance().fromJSON(response.toString(), ViewImages::class.java)
                iMyBusiness.updateBannerImage(data.data)
                PrefUtil.saveImageData(mContext, response.toString())
            } else {
                LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
            }
        }, Response.ErrorListener { error ->
            LogUtils.DEBUG("callImageApi Error ::" + error.message)

        })
        AppController.getInstance().addToRequestQueue(objectRequest, "callImageApi")
    }

    fun initMap(mContext: MyBusinessActivityLatest, mapFrag: Fragment?) {
        val mapFragment = mContext.supportFragmentManager
                .findFragmentById(R.id.mapFrag) as SupportMapFragment
        mapFragment.getMapAsync(mContext)
        mapFrag!!.requireView().visibility = View.GONE
    }

}
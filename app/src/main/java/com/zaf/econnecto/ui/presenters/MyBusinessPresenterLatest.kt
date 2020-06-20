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
import com.zaf.econnecto.network_call.response_model.my_business.AddDealsBg
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.network_call.response_model.my_business.MyBusiness
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import org.json.JSONException
import org.json.JSONObject



class MyBusinessPresenterLatest(private val mContext: Context, private val iMyBusiness: IMyBusinessLatest) : BasePresenter(mContext) {
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
            LogUtils.DEBUG("MyBusiness Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
    }

    fun callAboutApi() {
        val url = AppConstant.URL_BIZ_BROCHURE + PrefUtil.getBizId(mContext)
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

    fun callBrochureApi() {
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

    fun callAmenitiesApi() {
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

    fun callPaymentApi() {
        val url = AppConstant.URL_BIZ_PAYMENT + PrefUtil.getBizId(mContext)
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

    fun callPricingApi() {
        val url = AppConstant.URL_BIZ_PRICING + PrefUtil.getBizId(mContext)
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

    fun callCategoriesApi() {
        val url = AppConstant.URL_BIZ_CATEGORIES + PrefUtil.getBizId(mContext)
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

    fun callOperationTimeApi() {
        val url = AppConstant.URL_BIZ_OPERATING_HOURS + PrefUtil.getBizId(mContext)
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

    fun callProdServicesApi() {
        val url = AppConstant.URL_BIZ_PROD_SERVICES + PrefUtil.getBizId(mContext)
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

    fun callImageApi() {
        val url = AppConstant.URL_BIZ_IMAGES + PrefUtil.getBizId(mContext)
        LogUtils.DEBUG("URL : $url")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("View Image Response ::$response")
            val data = ParseManager.getInstance().fromJSON(response.toString(), ViewImages::class.java)
            if (data.status == AppConstant.SUCCESS) {
                iMyBusiness.updateBannerImage(data.data)
                PrefUtil.saveImageData(mContext, response.toString())
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
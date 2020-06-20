package com.zaf.econnecto.ui.presenters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.google.android.gms.maps.SupportMapFragment
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.home.BizCategoryData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.ViewBusinessActivity
import com.zaf.econnecto.ui.presenters.operations.IViewBizns
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import org.json.JSONException
import org.json.JSONObject

class ViewBusinessPresenter(context: Context?, iViewBizns: IViewBizns) : BasePresenter(context) {
    private var mContext: Context = context!!
    private var mIViewBizns : IViewBizns = iViewBizns


    fun callCategoryApi() {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_BIZ_CATEGORY // + AppConstant.listUrl+ 2;
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("Biz Category Response ::$response")
            val data = ParseManager.getInstance().fromJSON(response.toString(), BizCategoryData::class.java)
            if (data.status == AppConstant.SUCCESS) {
                mIViewBizns.updateCategory(data.data)
            } else {
                mIViewBizns.updateCategory(data.data)
            }
            loader.dismiss()
        }, Response.ErrorListener { error ->
            LogUtils.DEBUG("Biz Category Error ::" + error.message)
            loader.dismiss()
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "Biz Category")
    }

    fun initMap(mContext: ViewBusinessActivity, mapFrag: Fragment?) {
        val mapFragment = mContext.supportFragmentManager
                .findFragmentById(R.id.mapFrag) as SupportMapFragment
        mapFragment.getMapAsync(mContext)
        mapFrag!!.requireView().visibility = View.GONE
    }


    fun updateActionbar(activity: ViewBusinessActivity) {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbarBd)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toolbar.setNavigationOnClickListener { //finish();
            activity.onBackPressed()
        }
    }

    fun callBannerImgApi() {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_BIZ_IMAGES + PrefUtil.getBizId(mContext)

        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("View Image Response ::$response")
            val data = ParseManager.getInstance().fromJSON(response.toString(), ViewImages::class.java)
            if (data.status == AppConstant.SUCCESS) {
                mIViewBizns.updateBannerImage(data.data)
            } else {
                mIViewBizns.updateBannerImage(data.data)
            }
            loader.dismiss()
        }, Response.ErrorListener { error ->
            LogUtils.DEBUG("Biz Category Error ::" + error.message)
            loader.dismiss()
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "Biz Category")
    }

    fun callBasicDetailsApi(imageUpdate: Boolean) {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_BIZ_BASIC_DETAILS
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token", Utils.getAccessToken(mContext))
            jObj.put("owner_id", Utils.getUserID(mContext))
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
//                        iMyBusiness.updateBasicDetails(basicDetailsResponse,imageUpdate)

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
}
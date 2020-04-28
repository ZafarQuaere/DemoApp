package com.zaf.econnecto.ui.presenters

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
import com.zaf.econnecto.ui.activities.ViewBusinessActivity
import com.zaf.econnecto.ui.presenters.operations.IViewBizns
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppController
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.parser.ParseManager

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
        mapFrag!!.view!!.visibility = View.GONE
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

}
package com.zaf.econnecto.ui.fragments.details_frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.adapters.StaggeredImageAdapter
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppController
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil

/**
 * A simple [Fragment] subclass.
 */
 class FourthFragment : Fragment(), IMyBusinessLatest {
    private var recyclerCategory: RecyclerView? = null
    private var layoutManager: StaggeredGridLayoutManager? = null
    var mImageData: MutableList<out ViewImageData>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_fourth, container, false)
//        callBannerImgApi()
        initUI(view)
        return view
    }

   fun initUI(view: View) {
       recyclerCategory  = view.findViewById<RecyclerView>(R.id.recyclerCategory)
       recyclerCategory!!.setHasFixedSize(true)
       layoutManager = StaggeredGridLayoutManager( 2, LinearLayout.VERTICAL)
       recyclerCategory!!.setLayoutManager(layoutManager)
       recyclerCategory!!.setItemAnimator(DefaultItemAnimator())
        updateAdapter()
    }

   /* fun callBannerImgApi() {
        val url = AppConstant.URL_VIEW_IMAGES + PrefUtil.getBizId(requireActivity())
        LogUtils.DEBUG("URL : $url")
        val objectRequest = MyJsonObjectRequest(requireActivity(), Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("View Image Response ::$response")
            val data = ParseManager.getInstance().fromJSON(response.toString(), ViewImages::class.java)
            if (data.status == AppConstant.SUCCESS) {
                   updateAdapter(data.data)
            } else {
                LogUtils.showToast(requireActivity(), data.message.toString())
            }
        }, Response.ErrorListener { error ->
            LogUtils.DEBUG("Biz Category Error ::" + error.message)

        })
        AppController.getInstance().addToRequestQueue(objectRequest, "Biz Category")
    }*/

    private fun updateAdapter() {
//        recyclerCategory  = requireView().findViewById<View>(R.id.recyclerCategory) as RecyclerView
//        recyclerCategory!!.setHasFixedSize(true)
//        layoutManager = StaggeredGridLayoutManager( 2, LinearLayout.VERTICAL)
//        recyclerCategory!!.setLayoutManager(layoutManager)
//        recyclerCategory!!.setItemAnimator(DefaultItemAnimator())
        if (PrefUtil.getImageData(requireActivity()) != null) {
            val adapter = StaggeredImageAdapter(requireActivity(), PrefUtil.getImageData(requireActivity()))
            recyclerCategory!!.adapter = adapter
        } else {
            LogUtils.showToast(requireActivity(),"ImageData is null")
        }
    }

    override fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse?, imageUpdate: Boolean) {

    }

    override fun updateBannerImage(imageData: MutableList<ViewImageData>) {
        LogUtils.showToast(requireActivity(),"ImageData is null ${imageData.toString()}")
        mImageData = imageData
//        updateAdapter(data)
    }
}

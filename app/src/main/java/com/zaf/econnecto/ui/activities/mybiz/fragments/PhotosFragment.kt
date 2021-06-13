package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessViewModel
import com.zaf.econnecto.ui.activities.mybiz.PhotosViewModel
import com.zaf.econnecto.ui.adapters.StaggeredImageAdapter
import com.zaf.econnecto.ui.interfaces.DeleteImageListener
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response


class PhotosFragment : Fragment() {

    lateinit var  mContext: Context
    private lateinit var photosVm: PhotosViewModel
    private lateinit var mbVm: MyBusinessViewModel
    lateinit var imageList : MutableList<ViewImageData>
    lateinit var adapter: StaggeredImageAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        photosVm = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        activity.let {  mbVm = it?.let { it1 -> ViewModelProviders.of(it1).get(MyBusinessViewModel::class.java) }!! }
            callPhotosApi()
    }

    private fun callPhotosApi() {
        activity?.let { PrefUtil.getBizId(it) }?.let { photosVm.bizImageList(activity as Activity?, it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        registerListener()
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    private fun registerListener() {
        photosVm.mbImageList.observe(viewLifecycleOwner, Observer { photosResponseData -> updateUI(photosResponseData) })
        mbVm.imageList.observe(requireActivity(), Observer { imageData -> updateImageList(imageData) })
        mbVm.imagePosition.observe(requireActivity(), Observer { position -> updatePosition(position) })
    }

    private fun updateImageList(imageData: ViewImageData) {
        imageList.remove(imageData)
        adapter.notifyDataSetChanged()
    }

    private fun updatePosition(position: Int) {
        recycler_photos.removeViewAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun updateUI(photosResponseData: Response<JsonObject>?) {
        val body = JSONObject(Gson().toJson(photosResponseData?.body()))
        LogUtils.DEBUG("bizImageList Response:->> $body")
        var status = body.optInt("status")
        if (status == AppConstant.SUCCESS) {
            val data = ParseManager.getInstance().fromJSON(body.toString(), ViewImages::class.java)
            imageList = data.data
            updatePhotosUI(imageList)
//                    PrefUtil.saveImageData(mActivity, response.toString())
        } else {
            val jsonArray = body.optJSONArray("message")
            val message = jsonArray!!.get(0) as String
            LogUtils.showErrorDialog(activity, activity?.getString(R.string.ok), message)
        }
    }

    private fun updatePhotosUI(data: MutableList<ViewImageData>) {
        if (data.isNotEmpty()) {
            textAddPhotos.visibility = View.GONE
            recycler_photos.visibility = View.VISIBLE
            val layoutManager = GridLayoutManager(mContext, 2)
            recycler_photos.layoutManager = layoutManager
            recycler_photos.itemAnimator = DefaultItemAnimator()
            adapter = StaggeredImageAdapter(mContext, data, true, object: DeleteImageListener{
                override fun onDeleteClick(s: ViewImageData?, position: Int) {
                    lifecycleScope.launch {
                        mbVm.callDeleteImageApi(mContext,s, position)
                    }
                }

            })
            recycler_photos.adapter = adapter
        } else {
            textAddPhotos.visibility = View.VISIBLE
            recycler_photos.visibility = View.GONE
        }
    }
}
package com.zaf.econnecto.ui.activities.mybiz.fragments

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
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessViewModel
import com.zaf.econnecto.ui.adapters.StaggeredImageAdapter
import com.zaf.econnecto.ui.interfaces.DeleteImageListener
import com.zaf.econnecto.utils.AppConstant
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.coroutines.launch


class PhotosFragment : Fragment() {

    lateinit var mContext: Context
    private lateinit var mbVm: MyBusinessViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mbVm = ViewModelProviders.of(requireActivity()).get(MyBusinessViewModel::class.java)
    }

    private fun callPhotosApi() {
        (activity as MyBusinessActivityLatest).callImageListApi()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        registerListener()
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }


    private fun registerListener() {
        mbVm.imageList.observe(viewLifecycleOwner, Observer { imageData -> updatePhotosUI(imageData) })
        mbVm.isImageDeleted.observe(viewLifecycleOwner, Observer { isImageDeleted ->
            if (AppConstant.ADD_EDIT_PHOTO) {
                AppConstant.ADD_EDIT_PHOTO = false
                updateImageDeleted(isImageDeleted)
            }
        })
    }

    private fun updateImageDeleted(imageDeleted: Boolean) {
        if (imageDeleted)
            callPhotosApi()
    }

    private fun updatePhotosUI(data: MutableList<ViewImageData>) {
        if (data.isNotEmpty()) {
            textAddPhotos.visibility = View.GONE
            recycler_photos.visibility = View.VISIBLE
            val layoutManager = GridLayoutManager(mContext, 2)
            recycler_photos.layoutManager = layoutManager
            recycler_photos.itemAnimator = DefaultItemAnimator()
            val adapter = StaggeredImageAdapter(mContext, data, true, object : DeleteImageListener {
                override fun onDeleteClick(s: ViewImageData?, position: Int) {
                    lifecycleScope.launch {
                        if (s != null) {
                            (activity as MyBusinessActivityLatest).callDeleteImageApi(s, position)
                        }
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
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
import com.zaf.econnecto.ui.adapters.PhotosAdapter
import com.zaf.econnecto.ui.adapters.StaggeredImageAdapter
import com.zaf.econnecto.ui.interfaces.DeleteImageListener
import com.zaf.econnecto.ui.interfaces.DialogButtonClick
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
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

    private fun updatePhotosUI(data: MutableList<ViewImageData>?) {
        if (data != null && data.isNotEmpty()) {
            textAddPhotos.visibility = View.GONE
            recycler_photos.visibility = View.VISIBLE
            recycler_photos.setHasFixedSize(true)
            val layoutManager = GridLayoutManager(mContext, 2)
            recycler_photos.layoutManager = layoutManager
//            recycler_photos.itemAnimator = DefaultItemAnimator()
//            val adapter = PhotosAdapter(mContext,data,null)
            val adapter = StaggeredImageAdapter(mContext, data, true, object : DeleteImageListener {
                override fun onDeleteClick(imageData: ViewImageData?, position: Int) {
                    if (imageData != null) {
                        LogUtils.showDialogDoubleButton(mContext,
                            getString(R.string.cancel),
                            getString(R.string.ok),
                            getString(R.string.do_you_really_want_to_delete),
                            object : DialogButtonClick {
                                override fun onOkClick() {
                                    lifecycleScope.launch {
                                        (activity as MyBusinessActivityLatest).callDeleteImageApi(imageData, position)
                                    }
                                }
                                override fun onCancelClick() {}
                            })
                    }
                }
            })
            recycler_photos.adapter = adapter
        } else {
            textAddPhotos.visibility = View.VISIBLE
            recycler_photos.visibility = View.GONE
            textAddPhotos.setOnClickListener {
                view?.let { it1 -> (activity as MyBusinessActivityLatest).uploadPhoto(it1) }
            }
        }
    }
}
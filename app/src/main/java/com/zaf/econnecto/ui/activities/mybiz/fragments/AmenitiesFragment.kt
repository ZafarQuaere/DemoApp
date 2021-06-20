package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.AmenitiesRecyclerAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_amenities.*
import kotlinx.android.synthetic.main.fragment_amenities.view.*

class AmenitiesFragment : Fragment() {

    private lateinit var mbVm: MyBusinessViewModel
    private lateinit var amenityAdapter: AmenitiesRecyclerAdapter

    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mbVm = ViewModelProviders.of(requireActivity()).get(MyBusinessViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_amenities, container, false)
        view.textNoAmenity.setOnClickListener {
            startActivityForResult(Intent(activity, AmenitiesActivity::class.java), MyBusinessActivityLatest.UPDATE_AMENITIES)
        }
        registerListener()
        return view
    }

    private fun registerListener() {
        mbVm.bizAmenityList.observe(viewLifecycleOwner, Observer { data: Amenities ->
            updateAmenityList(data)
        })
        mbVm.isAmenityDeleted.observe(viewLifecycleOwner, Observer { isAmenityDeleted: Boolean ->
            if (AppConstant.ADD_EDIT_AMENITY) {
                AppConstant.ADD_EDIT_AMENITY = false
                updateRemoveAmenity(isAmenityDeleted)
            }
        })
    }


    private fun updateAmenityList(data: Amenities) {
        if (data.status == AppConstant.SUCCESS) {
            textNoAmenity.visibility = View.GONE
            recyclerAmenities.visibility = View.VISIBLE
            editAmenity.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerAmenities.layoutManager = layoutManager
            recyclerAmenities.itemAnimator = DefaultItemAnimator()
            val amenitiesData = data.data
            if (data.data.isNotEmpty()) {
                amenityAdapter = activity?.let { AmenitiesRecyclerAdapter(it, amenitiesData, mbVm) }!!
                recyclerAmenities.adapter = amenityAdapter
                editAmenity.setOnClickListener {
                    startActivityForResult(Intent(activity, AmenitiesActivity::class.java), MyBusinessActivityLatest.UPDATE_AMENITIES)
                }
            } else {
                textNoAmenity.visibility = View.VISIBLE
                recyclerAmenities.visibility = View.GONE
                editAmenity.visibility = View.GONE
            }
        } else {
            textNoAmenity.visibility = View.VISIBLE
            recyclerAmenities.visibility = View.GONE
            editAmenity.visibility = View.GONE
        }
    }

    private fun updateRemoveAmenity(isAmenityDeleted: Boolean) {
        if (isAmenityDeleted)
            callAmenityListApi()
    }

    private fun callAmenityListApi() {
        (activity as MyBusinessActivityLatest).callAmenityListApi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_AMENITIES && AppConstant.ADD_EDIT_AMENITY) {
            LogUtils.DEBUG("Coming from amenities Fragment")
            AppConstant.ADD_EDIT_AMENITY = false
            callAmenityListApi()
        }
    }
}
package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.app.Activity
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
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.Amenities
import com.zaf.econnecto.ui.activities.mybiz.AmenitiesActivity
import com.zaf.econnecto.ui.activities.mybiz.AmenitiesViewModel
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.adapters.AmenitiesRecyclerAdapter
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_amenities.*
import kotlinx.android.synthetic.main.fragment_amenities.view.*
import org.json.JSONObject
import retrofit2.Response

class AmenitiesFragment : Fragment() {

    private lateinit var amenitiesVm: AmenitiesViewModel
    private lateinit var amenityAdapter: AmenitiesRecyclerAdapter

    private lateinit var mContext: Context

    companion object {
        var editAmenities = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        amenitiesVm = ViewModelProviders.of(this).get(AmenitiesViewModel::class.java)
        activity?.let { PrefUtil.getBizId(it) }?.let { amenitiesVm.bizAmenityList(activity as Activity?, null, it) }
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
        amenitiesVm.allAmenityList.observe(viewLifecycleOwner, Observer { data: Amenities ->
            updateAmenityList(data)
        })
        amenitiesVm.removeAmenity.observe(viewLifecycleOwner, Observer { jsonObj: Response<JsonObject> ->
            if (editAmenities) {
                editAmenities = false
                updateRemoveAmenity(jsonObj)
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
                amenityAdapter = activity?.let { AmenitiesRecyclerAdapter(it, amenitiesData, amenitiesVm) }!!
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

    private fun updateRemoveAmenity(jsonObj: Response<JsonObject>) {
            val body = JSONObject(Gson().toJson(jsonObj.body()))
            LogUtils.DEBUG("RemoveAmenity Response:->> $body")
            val status = body.optInt("status")
            if (status == AppConstant.SUCCESS) {
                callAmenityListApi()
            } else {
                LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
            }
    }

    private fun callAmenityListApi() {
        activity?.let { PrefUtil.getBizId(it) }?.let { amenitiesVm.bizAmenityList(activity as Activity?, null, it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_AMENITIES) {
            LogUtils.DEBUG("Coming from amenities Fragment")
            callAmenityListApi()
        }
    }

}
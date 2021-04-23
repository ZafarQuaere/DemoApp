package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.app.Activity
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
import org.json.JSONObject
import retrofit2.Response

class AmenitiesFragment : Fragment() {
    private lateinit var amenitiesVm: AmenitiesViewModel
    private lateinit var amenityAdapter: AmenitiesRecyclerAdapter
    var amenitiesData: Amenities? = null
    var amenityRemoveData: Response<JsonObject>? = null
    var amenityAddData: Response<JsonObject>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amenitiesVm = ViewModelProviders.of(this).get(AmenitiesViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registerListener()
        if (amenitiesData == null)
            activity?.let { PrefUtil.getBizId(it) }?.let { amenitiesVm.bizAmenityList(activity as Activity?, null, it) }
        return inflater.inflate(R.layout.fragment_amenities, container, false)
    }

    private fun registerListener() {
        amenitiesVm.allAmenityList.observe(viewLifecycleOwner, Observer { data: Amenities ->
            updateAmenityList(data)
            amenitiesData = data
        })
        amenitiesVm.removeAmenity.observe(viewLifecycleOwner, Observer { jsonObj: Response<JsonObject> ->
            updateRemoveAmenity(jsonObj)
            amenityRemoveData = jsonObj
        })
        amenitiesVm.addAmenity.observe(viewLifecycleOwner, Observer { jsonObj: Response<JsonObject> ->
            updateAddAmenity(jsonObj)
            amenityAddData = jsonObj
        })
    }


    private fun updateAmenityList(data: Amenities) {
        if (/*data != AppConstant.FAILURE && */data.status == AppConstant.SUCCESS) {
            textNoAmenity.visibility = View.GONE
            recyclerAmenities.visibility = View.VISIBLE
            editAmenity.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerAmenities.layoutManager = layoutManager
            recyclerAmenities.itemAnimator = DefaultItemAnimator()
            val amenitiesData = data.data
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
    }

    private fun updateRemoveAmenity(jsonObj: Response<JsonObject>) {
        if (amenityRemoveData == null) {
            val body = JSONObject(Gson().toJson(jsonObj.body()))
            LogUtils.DEBUG("RemoveAmenity Response:->> $body")
            val status = body.optInt("status")
            if (status == AppConstant.SUCCESS) {
                activity?.let { PrefUtil.getBizId(it) }?.let { amenitiesVm.bizAmenityList(activity as Activity?, null, it) }
            } else {
                LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
            }
        }
    }

    private fun updateAddAmenity(jsonObj: Response<JsonObject>) {
        if (amenityAddData == null) {
            val body = JSONObject(Gson().toJson(jsonObj.body()))
            LogUtils.DEBUG("addAmenityApi Response:->> $body")
            val status = body.optInt("status")
            if (status == AppConstant.SUCCESS) {
                activity?.let { PrefUtil.getBizId(it) }?.let { amenitiesVm.bizAmenityList(activity as Activity?, null, it) }
            } else {
                LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_AMENITIES) {
            LogUtils.DEBUG("Coming from amenities Fragment")
            activity?.let { PrefUtil.getBizId(it) }?.let { amenitiesVm.bizAmenityList(activity as Activity?, null, it) }
        }
    }

}
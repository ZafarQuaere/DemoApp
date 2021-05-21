package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.activities.mybiz.fragments.AmenitiesFragment
import com.zaf.econnecto.ui.interfaces.AmenityAddedListener
import com.zaf.econnecto.ui.interfaces.IGeneralAmenityList
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.AppDialogLoader
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import com.zaf.econnecto.utils.parser.ParseManager
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AmenitiesViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mActivity: Activity
    var allAmenityList =  MutableLiveData<Amenities>()
    var removeAmenity =  MutableLiveData<Response<JsonObject>>()
    var addAmenity =  MutableLiveData<Response<JsonObject>>()


    fun bizAmenityList(activity: Activity?, listener: IMyBusinessLatest?, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizAmenityList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Amenities> {
            override fun onFailure(call: Call<Amenities>, t: Throwable) {
                loader.dismiss()
                // here i need to check the error through observer
                LogUtils.DEBUG("bizAmenityList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<Amenities>, response: Response<Amenities>) {
                LogUtils.DEBUG("bizAmenityList response: " + ParseManager.getInstance().toJSON(response.body()))
                loader.dismiss()
                allAmenityList.value = response.body()
            }
        })
    }


    fun bizAllAmenityList(activity: Activity?, listener: IGeneralAmenityList) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val eConnectoServices = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = eConnectoServices.allAmenityList()
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<AllAmenityList> {
            override fun onFailure(call: Call<AllAmenityList>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok),
                        mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.message)
            }

            override fun onResponse(call: Call<AllAmenityList>, response: Response<AllAmenityList>) {
                LogUtils.DEBUG("AllPaymentMethods response: " + ParseManager.getInstance().toJSON(response.body()))
                loader.dismiss()
                if (response.isSuccessful) {
                    val paymentMethods = response.body()
                    if (paymentMethods?.status == AppConstant.SUCCESS) {
                        listener.updateAmenityList(paymentMethods!!.data)
                    } else {
                        listener.updateAmenityList(null)
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), paymentMethods!!.message)
                    }
                }
            }
        })
    }

    fun addAmenityApi(activity: Activity?, listener: AmenityAddedListener?, amenityItem: GeneralAmenities?) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("amenity_id", amenityItem?.amenity_id)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addAmenity(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("addAmenityApi Response:->> $body")
                val status = body.optInt("status")
                loader.dismiss()
                addAmenity.value = response
                if (status == AppConstant.SUCCESS) {
                    listener?.updateAmenities()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity).onBackPressed() }
                }
            }
        })
    }

    fun removeAmenity(activity: Activity?, amenity_id: String, listener: IMyBusinessLatest?, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("amenity_id", amenity_id)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removeAmenity(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                AmenitiesFragment.editAmenities = true
                removeAmenity.value = response
            }
        })
    }
}
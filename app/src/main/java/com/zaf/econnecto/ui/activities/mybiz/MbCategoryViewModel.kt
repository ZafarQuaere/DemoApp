package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.model.CategoryListData
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.AllCategoriesListener
import com.zaf.econnecto.ui.interfaces.CategoryAddedListener

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

class MbCategoryViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mActivity: Activity
    var mbCategoryList =  MutableLiveData<UserCategories>()
    var removeCategory =  MutableLiveData<Response<JsonObject>>()
    var addCategory =  MutableLiveData<Response<JsonObject>>()

    fun bizAllCategories(activity: Activity?, listener: AllCategoriesListener) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizAllCategories()
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<BizCategories> {
            override fun onFailure(call: Call<BizCategories>, t: Throwable) {
                loader.dismiss()
                listener.updateCategoriesUI(null)
                LogUtils.DEBUG("bizAllCategories() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<BizCategories>, response: Response<BizCategories>) {
                LogUtils.DEBUG("bizAllCategories Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    val categories: BizCategories = response.body()!!
                    if (categories.status == AppConstant.SUCCESS) {
                        listener.updateCategoriesUI(categories.data)
                    } else {
                        listener.updateCategoriesUI(null)
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), categories.message)
                    }
                }
                loader.dismiss()
            }
        })
    }

    fun addCategoryApi(activity: Activity?, listener: CategoryAddedListener?, amenityItem: CategoryListData?) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("amenity_id", amenityItem?.categoryId)
        jsonObject.put("amenity_id", amenityItem?.parentCategoryId)
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
                if (status == AppConstant.SUCCESS) {
                    listener?.updateCategory()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity).onBackPressed() }
                }
            }
        })
    }

    fun bizCategoryList(activity: Activity?, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizCategoryList("21"/*bizId*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<UserCategories> {
            override fun onFailure(call: Call<UserCategories>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizCategoryList() Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<UserCategories>, response: Response<UserCategories>) {
                LogUtils.DEBUG("bizCategoryList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                loader.dismiss()
                val userCategories: UserCategories = response.body()!!
                mbCategoryList.value = userCategories
               /* if (userCategories.status == AppConstant.SUCCESS) {
                    listener.updateCategories(userCategories.data)
                } else {
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), userCategories.message[0])
                }*/
            }
        })
    }
}
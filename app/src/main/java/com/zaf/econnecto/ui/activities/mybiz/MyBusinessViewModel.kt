package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zaf.econnecto.R
import com.zaf.econnecto.model.CategoryListData
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.activities.mybiz.fragments.AmenitiesFragment
import com.zaf.econnecto.ui.activities.mybiz.fragments.CategoriesFragment
import com.zaf.econnecto.ui.activities.mybiz.fragments.PaymentFragment
import com.zaf.econnecto.ui.activities.mybiz.fragments.PricingFragment
import com.zaf.econnecto.ui.interfaces.*
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.ui.presenters.operations.IProductNService
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import com.zaf.econnecto.utils.storage.PrefUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBusinessViewModel : BaseViewModel() {

    lateinit var mActivity: Activity
    var basicDetailsData = MutableLiveData<MutableList<BasicDetailsData>>()
    lateinit var basicDetailsResponse: LiveData<BasicDetailsResponse>

    fun callMyBizBasicDetails(activity: Activity?, imageUpdate: Boolean, listener: IMyBusinessLatest?, ownerId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", ownerId)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.bizBasicDetails(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: ${jsonObject.toString()}")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("callBasicDetailsApi Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("callBasicDetailsApi Response:->> $body")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    val basicDetailsResponse = ParseManager.getInstance().fromJSON(body.toString(), BasicDetailsResponse::class.java)
//                   basicDetailsData = basicDetailsResponse.data.toMutableList()
                    PrefUtil.setBasicDetailsData(mActivity, body.toString())
                    listener?.updateBasicDetails(basicDetailsResponse, imageUpdate)
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { /*(mActivity).onBackPressed()*/ }
                }
            }
        })
    }

    fun bizImageList(activity: Activity?, listener: IMyBizImage, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizImageList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizImageList() Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizImageList Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    val data = ParseManager.getInstance().fromJSON(body.toString(), ViewImages::class.java)
                    imageList.value = data.data
                    listener.updateBannerImage(data.data)
//                    PrefUtil.saveImageData(mActivity, response.toString())
                } else {
                    val jsonArray = body.optJSONArray("message")
                    val message = jsonArray!!.get(0) as String
                    LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), message)
                }
                loader.dismiss()
            }
        })
    }

    suspend fun callDeleteImageApi(mContext: Context, imageData: ViewImageData?, position: Int) {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_DELETE_IMAGE
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token", Utils.getAccessToken(mContext))
            jObj.put("owner_id", Utils.getUserID(mContext))
            jObj.put("img_id", imageData?.imgId)
        } catch (e: JSONException) {
            e.printStackTrace();
        }
        LogUtils.DEBUG("URL : $url\nRequest Body ::${jObj.toString()}")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, jObj, { response: JSONObject? ->
            LogUtils.DEBUG("DeletePhoto Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0))
                        isImageDeleted.value = true
                        ImageUpdateModelListener.getInstance().changeState(true)
                        AppConstant.ADD_EDIT_PHOTO = true
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok),
                                response.optJSONArray("message").optString(0))
                    }
                    loader.dismiss()
                }
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, { error: VolleyError ->
            LogUtils.DEBUG("DeletePhoto Error ::" + error.message)
            loader.dismiss()
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "DeletePhoto")
    }

    /*
            Amenities Api calls
     */
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
                bizAmenityList.value = response.body()
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
                isAmenityDeleted.value = false
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("RemoveAmenity Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
//                    AmenitiesFragment.addEditAmenity = true
                    AppConstant.ADD_EDIT_AMENITY = true
                    isAmenityDeleted.value = true
                } else {
                    isAmenityDeleted.value = false
                    LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
                }
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
                        allAmenityList.value = paymentMethods?.data
//                        listener.updateAmenityList(paymentMethods!!.data)
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
                if (status == AppConstant.SUCCESS) {
                    listener?.updateAmenities()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) {  }
                }
            }
        })
    }

    fun bizOperatingHours(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizOperatingHours("21"/*bizId*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<OPHours> {
            override fun onResponse(call: Call<OPHours>, response: Response<OPHours>) {
                loader.dismiss()
                LogUtils.DEBUG("bizOperatingHours response: " + ParseManager.getInstance().toJSON(response.body()))
                if (response.isSuccessful) {
                    val opHours = response.body()
                    if (opHours!!.status == AppConstant.SUCCESS) {
                        listener.updateOperatingHours(opHours.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), opHours.message[0])
                    }
                }
            }

            override fun onFailure(call: Call<OPHours>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizOperatingHours error: " + t.localizedMessage)
            }

        })
    }

    fun bizBrochureList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizBrochureList("21"/*bizId*/)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Brochure> {
            override fun onFailure(call: Call<Brochure>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizBrochureList() Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<Brochure>, response: Response<Brochure>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("bizBrochureList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    val brochure: Brochure = response.body()!!
                    if (brochure.status == AppConstant.SUCCESS) {
                        listener.updateBrochureSection(brochure.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), brochure.message[0])
                    }
                }
                loader.dismiss()
            }
        })
    }

    fun bizProductServicesList(activity: Activity?, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizProductServicesList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<ProductNService> {
            override fun onFailure(call: Call<ProductNService>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizProductServicesList() Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<ProductNService>, response: Response<ProductNService>) {
                LogUtils.DEBUG("bizProductServicesList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    val PnService: ProductNService = response.body()!!
                    if (PnService.status == AppConstant.SUCCESS) {
                        listener.updateProductServiceSection(PnService.data)
                    } else {
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), PnService.message[0])
                    }
                }
                loader.dismiss()
            }
        })
    }

    fun addProductServicesApi(activity: Activity?, imageUpdate: Boolean, listener: IProductNService?, prodNService: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_name", prodNService)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addProductServices(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("removeProductServiceApi Response:->> ${body.toString()}")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    listener?.updateProductServices()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity).onBackPressed() }
                }
            }
        })
    }

    fun removeProductOrService(activity: Activity?, prodId: String, listener: IMyBusinessLatest, bizId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_id", Integer.parseInt(prodId))

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removeProductServices(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("removeProductServiceApi Response:->> $body")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    bizProductServicesList(mActivity, listener,bizId)
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) { (mActivity).onBackPressed() }
                }
            }
        })
    }

    /*
           Category Api calls
    */
    fun bizCategoryList(activity: Activity?, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizCategoryList(bizId)
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

    fun removeCategory(activity: Activity?, category_id: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("category_id", category_id)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removeCategory(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("RemoveCategory Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    AppConstant.ADD_EDIT_CATEGORY = true
                    isCategoryDeleted.value = true
                } else {
                    isCategoryDeleted.value = false
                    LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
                }
            }
        })
    }

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
        jsonObject.put("category_id", amenityItem?.categoryId)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.addCategory(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("addCategory Api Response:->> $body")
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

    /*
           Payment Api calls
    */

    fun bizPaymentMethodList(activity: Activity?, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPaymentList(bizId)
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")
        requestCall.enqueue(object : Callback<PaymentMethods> {
            override fun onFailure(call: Call<PaymentMethods>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizPaymentMethodList Failure: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<PaymentMethods>, response: Response<PaymentMethods>) {
                LogUtils.DEBUG("bizPaymentMethodList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    mbPayOptionList.value =  response.body()
                }

                loader.dismiss()
            }
        })
    }

    fun removePayType(activity: Activity?, payMethodId: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("p_method_id", payMethodId)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removePayType(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("RemoveCategory Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    AppConstant.ADD_EDIT_PAYMENTS = true
                    isPayOptionDeleted.value = true
                } else {
                    isPayOptionDeleted.value = false
                    LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
                }
            }
        })
    }

    fun addPaymentMethodsApi(activity: Activity?, listener: PaymentMethodAddListener?, paymentData: GeneralPaymentMethods) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("p_method_id", paymentData.p_method_id)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addPaymentMethods(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")
        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("addPaymentMethodsApi failure:->> ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("addPaymentMethodsApi Response:->> $body")
                loader.dismiss()
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    listener?.updatePaymentMethod()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) {}
                }
            }
        })
    }

    fun bizAllPaymentTypes(activity: Activity?, listener: IPaymentOptionList) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val eConnectoServices = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = eConnectoServices.allPaymentMethods()
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<AllPaymentMethods> {
            override fun onFailure(call: Call<AllPaymentMethods>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<AllPaymentMethods>, response: Response<AllPaymentMethods>) {
                LogUtils.DEBUG("AllPaymentMethods response: " + ParseManager.getInstance().toJSON(response.body()))
                loader.dismiss()
                if (response.isSuccessful) {
                    val paymentMethods = response.body()
                    if (paymentMethods?.status == AppConstant.SUCCESS) {
                        listener.updatePaymentListUI(paymentMethods!!.data)
                    } else {
                        listener.updatePaymentListUI(null)
                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), paymentMethods!!.message)
                    }
                }
            }
        })
    }

    /*
    ======================================================
             Pricing Api calls
    ======================================================
      */
    fun bizPricingList(activity: Activity?, bizId: String) {
        if (activity != null)
            mActivity = activity
        var loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)
        val requestCall = categoryService.bizPricingList(bizId)
//        val requestCall = categoryService.bizPricingList(PrefUtil.getBizId(mActivity))
        LogUtils.DEBUG("Url: ${requestCall.request().url()} ")

        requestCall.enqueue(object : Callback<Pricing> {
            override fun onFailure(call: Call<Pricing>, t: Throwable) {
                loader.dismiss()
                LogUtils.DEBUG("bizPricingList Failure: ${t.localizedMessage}")

            }

            override fun onResponse(call: Call<Pricing>, response: Response<Pricing>) {
                LogUtils.DEBUG("bizPricingList Response:->> ${ParseManager.getInstance().toJSON(response.body())}")
                if (response != null && response.isSuccessful) {
                    mbPricingList.value = response.body()
                    /*val pricing: Pricing = response.body()!!
                    if (pricing.status == AppConstant.SUCCESS) {
                        listener.updatePricingSection(pricing.data)
                    } else {
                        listener.updatePricingSection(null)
//                        LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), pricing.message[0])
                    }*/
                }
                loader.dismiss()
            }
        })
    }

    fun removePricing(activity: Context, payMethodId: String) {
        val loader = AppDialogLoader.getLoader(activity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(activity))
        jsonObject.put("owner_id", Utils.getUserID(activity))
        jsonObject.put("prod_serv_id", payMethodId)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.removePricing(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loader.dismiss()
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("RemoveCategory Response:->> $body")
                val status = body.optInt("status")
                if (status == AppConstant.SUCCESS) {
                    AppConstant.ADD_EDIT_PRICING = true
                    isPricingDeleted.value = true
                } else {
                    isPricingDeleted.value = false
                    LogUtils.showDialogSingleActionButton(activity, activity?.getString(R.string.ok), body.optJSONArray("message").optString(0)) { }
                }
            }
        })
    }

    fun callAddPricingApi(activity: Activity?, listener: PricingAddedListener, desc: String, price: String, unit: String) {
        if (activity != null)
            mActivity = activity
        val loader = AppDialogLoader.getLoader(mActivity)
        loader.show()
        val jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(mActivity))
        jsonObject.put("owner_id", Utils.getUserID(mActivity))
        jsonObject.put("prod_serv_name", desc)
        jsonObject.put("prod_serv_price", price)
        jsonObject.put("unit", unit)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val categoryService = ServiceBuilder.buildConnectoService(EConnectoServices::class.java)

        val requestCall = categoryService.addPricing(requestBody)
        LogUtils.DEBUG("Url: ${requestCall.request().url()}  \nBody: $jsonObject")

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loader.dismiss()
                LogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.ok), mActivity.getString(R.string.something_wrong_from_server_plz_try_again) + "\n" + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val body = JSONObject(Gson().toJson(response.body()))
                LogUtils.DEBUG("AddPricingApi Response:->> ${body.toString()}")
                val status = body.optInt("status")
                loader.dismiss()
                if (status == AppConstant.SUCCESS) {
                    listener.updatePricing()
                } else {
                    LogUtils.showDialogSingleActionButton(mActivity, mActivity.getString(R.string.ok), body.optJSONArray("message").optString(0)) {  }
                }
            }
        })
    }
}


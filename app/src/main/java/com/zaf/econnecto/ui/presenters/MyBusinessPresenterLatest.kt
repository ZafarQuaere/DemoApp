package com.zaf.econnecto.ui.presenters

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.skydoves.colorpickerpreference.ColorEnvelope
import com.skydoves.colorpickerpreference.ColorPickerDialog
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.VolleyMultipartRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.AddDealsBg
import com.zaf.econnecto.network_call.response_model.my_business.MyBusiness
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData
import com.zaf.econnecto.ui.activities.MyBusinessActivity
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.parser.ParseManager
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MyBusinessPresenterLatest(private val mContext: Context, private val iMyBusiness: IMyBusinessLatest) : BasePresenter(mContext) {
    private val loader: AppLoaderFragment
    private var bizDetailData: MyBusiness? = null
    private var addDealsBg: AddDealsBg? = null
    init {
        loader = AppLoaderFragment.getInstance(mContext)
    }
    fun callMyBizApi() {
        loader.show()
        val url = AppConstant.URL_BASE_MVP + AppConstant.URL_MY_BUSINESS + Utils.getUserID(mContext)
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        /* JSONObject object = new JSONObject();
        try {
            object.put("user_email",Utils.getUserEmail(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, null, Response.Listener { response ->
            LogUtils.DEBUG("MyBusiness Response ::$response")
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        bizDetailData = ParseManager.getInstance().fromJSON(response.toString(), MyBusiness::class.java)
                        //iMyBusiness.updateUI(bizDetailData.getData()[0])
//                        iMyBusiness.updateUI(bizDetailData!!.data[0])
                    } else {
                        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), response.optJSONArray("message").optString(0)) { (mContext as Activity).onBackPressed() }
                    }
                }
                loader.dismiss()
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error ->
            loader.dismiss()
            LogUtils.DEBUG("MyBusiness Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
    }

    fun showUpdateBizDialog() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setWindowAnimations(R.style.dialogUpDownAnimation)
        dialog.setContentView(R.layout.dialog_update_biz)
        val editAddress = dialog.findViewById<View>(R.id.editAddress) as TextInputEditText
        val editMobile = dialog.findViewById<View>(R.id.editMobile) as TextInputEditText
        val editEmail = dialog.findViewById<View>(R.id.editEmail) as TextInputEditText
        val editBizWebsite = dialog.findViewById<View>(R.id.editBizWebsite) as TextInputEditText
        val editSD = dialog.findViewById<View>(R.id.editShortDescription) as TextInputEditText
        val editDD = dialog.findViewById<View>(R.id.editLongDescription) as TextInputEditText
        val btnOk = dialog.findViewById<View>(R.id.btn_ok) as Button
        val btnCancel = dialog.findViewById<View>(R.id.btnCancel) as Button
        btnCancel.text = mContext.getString(R.string.cancel)
        btnOk.text = mContext.getString(R.string.update)
        btnOk.setOnClickListener {
            dialog.dismiss()
            validateFields(editAddress.text.toString().trim { it <= ' ' }, editMobile.text.toString().trim { it <= ' ' },
                    editEmail.text.toString().trim { it <= ' ' }, editBizWebsite.text.toString().trim { it <= ' ' },
                    editSD.text.toString().trim { it <= ' ' }, editDD.text.toString().trim { it <= ' ' })
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun validateFields(address: String, mobile: String, email: String, website: String, shortDesc: String, detailDesc: String) {
        var address = address
        var mobile = mobile
        var email = email
        var website = website
        var shortDesc = shortDesc
        val myBusinessData = bizDetailData!!.data[0]
        if (address.isEmpty()) {
            address = myBusinessData.address1
        }
        if (mobile.isEmpty()) {
            mobile = myBusinessData.mobile1
        }
        if (email.isEmpty()) {
            email = myBusinessData.email
        }
        if (website.isEmpty()) {
            website = myBusinessData.website
        }
        if (shortDesc.isEmpty()) {
            shortDesc = myBusinessData.shortDescription
        }
        callUpdateBizApi(address, mobile, email, website, shortDesc, detailDesc, myBusinessData)
    }

    private fun callUpdateBizApi(address: String, mobile: String, email: String, website: String, shortDesc: String, detailDesc: String, myBusinessData: MyBusinessData) {
        loader.show()
        val url = AppConstant.URL_BASE + AppConstant.URL_UPDATE_BUSINESS
        val `object` = JSONObject()
        try {
            `object`.put("owner_email", Utils.getUserEmail(mContext))
            `object`.put("short_description", shortDesc)
            //            object.put("business_category",myBusinessData.getBusinessCategory());
            `object`.put("detailed_description", detailDesc)
            //            object.put("year_founded",myBusinessData.getYearFounded());
            `object`.put("awards", "")
            `object`.put("address", address)
            `object`.put("phone1", mobile)
            `object`.put("phone2", myBusinessData.mobile2)
            `object`.put("business_email", email)
            `object`.put("website", website)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        LogUtils.DEBUG("URL : $url\nRequest Body ::$`object`")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, `object`, Response.Listener { response ->
            LogUtils.DEBUG("UpdateBusiness Response ::$response")
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
//                        iMyBusiness.updateBizData(address, mobile, email, website, shortDesc, detailDesc)
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
                loader.dismiss()
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error ->
            loader.dismiss()
            LogUtils.DEBUG("UpdateBusiness Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "UpdateBusiness")
    }

    fun callDealBgApi() {
        val url = AppConstant.URL_DEAL_BACKGROUND
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("Deal Background Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg::class.java)
//                        iMyBusiness.updateDealBackground(addDealsBg!!.getData())
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError -> LogUtils.DEBUG("MyBusiness Error ::" + error.message) })
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness")
    }

    fun uploadBitmap(bitmapUpload: Bitmap?, imageType: Int, isImage: String, categoryId: String) {
        loader.show()
        var uploadUrl = ""
        var upload_type = ""
        if (imageType == MyBusinessActivity.IMG_BANNER_RESULT) {
            uploadUrl = AppConstant.URL_UPLOAD_BUSINESS_BANNER_PIC
            upload_type = "banner_image"
        } else if (imageType == MyBusinessActivity.IMG_HOT_DEALS) {
            uploadUrl = AppConstant.URL_ADD_DEAL
            upload_type = "deal_image"
        } else {
            uploadUrl = AppConstant.URL_UPLOAD_BUSINESS_PROFILE_PIC
            upload_type = "business_profile_image"
        }
        LogUtils.DEBUG("URL : $uploadUrl")
        val finalUpload_type = upload_type
        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(Method.POST, uploadUrl,
                Response.Listener { response: NetworkResponse ->
                    val s = String(response.data)
                    LogUtils.DEBUG("Upload Pic Response : $s")
                    try {
                        val obj = JSONObject(String(response.data))
                        val status = obj.optInt("status")
                        if (status == AppConstant.SUCCESS) {
                            if (imageType == MyBusinessActivity.IMG_BANNER_RESULT) {
//                                iMyBusiness.updateImage(imageType, bitmapUpload)
                            } else {
//                                iMyBusiness.updateImage(imageType, bitmapUpload)
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    loader.dismiss()
                },
                Response.ErrorListener { error: VolleyError ->
                    LogUtils.showToast(mContext, error.message)
                    LogUtils.ERROR("Upload profile pic Error $error")
                    loader.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (imageType == MyBusinessActivity.IMG_HOT_DEALS) {
                    params["owner_email"] = Utils.getUserEmail(mContext)
                    params["category_id"] = categoryId
                    params["is_image"] = isImage
                } else {
                    params["user_email"] = Utils.getUserEmail(mContext)
                }
                return params
            }

            override fun getByteData(): Map<String, DataPart> {
                val params: MutableMap<String, DataPart> = HashMap()
                val imagename = System.currentTimeMillis()
                params[finalUpload_type] = DataPart("$imagename.png", BitmapUtils.getByteArrayFromBitmap(bitmapUpload))
                return params
            }
        }
        volleyMultipartRequest.retryPolicy = DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        //adding the request to volley
        Volley.newRequestQueue(mContext).add(volleyMultipartRequest)
    }

    fun submitHotDealsApi() {
        loader.show()
        val url = AppConstant.URL_ADD_DEAL
        LogUtils.DEBUG("URL : $url\nRequest Body ::")
        val `object` = JSONObject()
        try {
            `object`.put("owner_email", Utils.getUserEmail(mContext))
            `object`.put("category_id", "5")
            `object`.put("is_image", 1)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.POST, url, `object`, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("Submit HotDeals Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        bizDetailData = ParseManager.getInstance().fromJSON(response.toString(), MyBusiness::class.java)
//                        iMyBusiness.updateUI(bizDetailData!!.getData()[0])
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"))
                    }
                }
                loader.dismiss()
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener { error: VolleyError ->
            loader.dismiss()
            LogUtils.DEBUG("SubmitHotDeals Error ::" + error.message)
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "SubmitHotDeals")
    }

    fun showColorPickerDialog(rlyBG: RelativeLayout) {
        val builder = ColorPickerDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        builder.setTitle("ColorPicker Dialog")
        builder.setPreferenceName("MyColorPickerDialog")
        builder.setPositiveButton(mContext.getString(R.string.confirm)) { envelope: ColorEnvelope -> rlyBG.setBackgroundColor(envelope.color) }
        builder.setNegativeButton(mContext.getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        val colorPickerView = builder.colorPickerView
        colorPickerView.setPaletteDrawable(mContext.resources.getDrawable(R.drawable.palettebar))
        colorPickerView.flagView = CustomFlag(mContext, R.layout.layout_color_flag)
        builder.show()
    }

    fun callBannerImgApi() {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_VIEW_IMAGES // + AppConstant.listUrl+ 2;

        val objectRequest = MyJsonObjectRequest(mContext, Request.Method.GET, url, null, Response.Listener { response ->
            LogUtils.DEBUG("View Image Response ::$response")
            val data = ParseManager.getInstance().fromJSON(response.toString(), ViewImages::class.java)
            if (data.status == AppConstant.SUCCESS) {
                iMyBusiness.updateBannerImage(data.data)
            } else {
                iMyBusiness.updateBannerImage(data.data)
            }
            loader.dismiss()
        }, Response.ErrorListener { error ->
            LogUtils.DEBUG("Biz Category Error ::" + error.message)
            loader.dismiss()
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "Biz Category")
    }


}
package com.zaf.econnecto.ui.presenters

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.VolleyMultipartRequest
import com.zaf.econnecto.ui.interfaces.DialogSingleButtonListener
import com.zaf.econnecto.ui.presenters.operations.IEditImage
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.KotUtil.Companion.compressImage
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

class EditImagePresenter(var mContext: Context, var iEditImage: IEditImage) : BasePresenter(mContext) {

    private val loader: AppLoaderFragment = AppLoaderFragment.getInstance(mContext)

    suspend fun uploadBitmap(bitmapUpload: Bitmap?, file: File?) {
        loader.show()
        LogUtils.DEBUG("Upload URL : " + AppConstant.URL_ADD_IMAGE)

        //Compressing image file
        var compressFile = compressImage(mContext, file!!)
        LogUtils.DEBUG(">>>> Size after compress : ${KotUtil.getReadableFileSize(compressFile.length())}")
//        val bitmap = BitmapUtils.resizeBitmapBanner(bitmapUpload, 1000, 350)

        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(Method.POST, AppConstant.URL_ADD_IMAGE,
                Response.Listener { response: NetworkResponse ->
                    val uploadResponse = String(response.data)
                    LogUtils.DEBUG("Add Image Response : $uploadResponse")
                    try {
                        val obj = JSONObject(String(response.data))
                        val status = obj.optInt("status")
                        if (status == AppConstant.SUCCESS) {
                            LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), obj.optJSONArray("message").optString(0),object: DialogSingleButtonListener {
                                override fun okClick() {
                                    iEditImage!!.onUploadSuccess()
                                }
                            })

                        } else {
                            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), obj.optJSONArray("message").optString(0))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    loader.dismiss()
                },
                Response.ErrorListener { error: VolleyError ->
                    Toast.makeText(mContext.applicationContext, error.message, Toast.LENGTH_SHORT).show()
                    LogUtils.VolleyError(error)
                    LogUtils.ERROR("Add Image Error " + error.message)
                    loader.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["jwt_token"] = Utils.getAccessToken(mContext)
                params["owner_id"] = Utils.getUserID(mContext)
                return params
            }

            override fun getByteData(): Map<String, DataPart> {
                val params: MutableMap<String, DataPart> = HashMap()
                val imageName = System.currentTimeMillis()

//                val bitmap = BitmapFactory.decodeFile(compressFile.absolutePath)
                val byteArray = FileUtil.fileToByteArray(compressFile)
                params["img"] = DataPart("$imageName.png", byteArray)
               // params["img"] = DataPart("$imageName.png", BitmapUtils.getByteArrayFromBitmap(bitmap))
                return params
            }
        }
        volleyMultipartRequest.retryPolicy = DefaultRetryPolicy(5000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        Volley.newRequestQueue(mContext).add(volleyMultipartRequest)
    }

}
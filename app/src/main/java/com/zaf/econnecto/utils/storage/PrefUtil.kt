package com.zaf.econnecto.utils.storage

import android.content.Context
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.parser.ParseManager

object PrefUtil {

    fun setBizId(mContext: Context?, data: String) {
        if (mContext == null) return
        val prefs = AppSharedPrefs.getInstance(mContext)
        prefs.put(mContext.getString(R.string.key_biz_id), data)
    }

    fun getBizId(context: Context): String? {
        val prefs = AppSharedPrefs.getInstance(context)
        var data = ""
        data = try {
            prefs[context.getString(R.string.key_biz_id)] as String
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.ERROR(e.message)
            return data
        }
        return data
    }

    fun saveImageData(mContext: Context, data: String) {
        if (mContext == null) return
        val prefs = AppSharedPrefs.getInstance(mContext)
        prefs.put("Key_image_data", data)
    }

    fun getImageData(mContext: Context): MutableList<ViewImageData>? {
        val prefs = AppSharedPrefs.getInstance(mContext)
        var data = ""
        data = try {
            prefs["Key_image_data"] as String
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            LogUtils.ERROR(e.message)
            return null
        }
        val viewImage = ParseManager.getInstance().fromJSON<ViewImages>(data, ViewImages::class.java)
        return viewImage.data
    }

}
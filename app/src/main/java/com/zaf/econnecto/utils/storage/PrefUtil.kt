package com.zaf.econnecto.utils.storage

import android.content.Context
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImages
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.parser.ParseManager

object PrefUtil {

    fun getBizId(context: Context): String {
        val basicDetailsData = getBasicDetailsData(context)
        return basicDetailsData!!.businessId
    }

    fun setBasicDetailsData(mContext: Context, data: String) {
        if (mContext == null) return
        val prefs = AppSharedPrefs.getInstance(mContext)
        prefs.put(mContext.getString(R.string.key_basic_info), data)
    }

    fun getBasicDetailsData(mContext: Context): BasicDetailsData? {
        val prefs = AppSharedPrefs.getInstance(mContext)
        var data = ""
        data = try {
            prefs[mContext.getString(R.string.key_basic_info)] as String
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            LogUtils.ERROR(e.message)
            return null
        }
        val basicDetailsResponse = ParseManager.getInstance().fromJSON(data, BasicDetailsResponse::class.java)
        return basicDetailsResponse.data[0]
    }



    fun setAboutText(mContext: Context, data: String) {
        if (mContext == null) return
        val prefs = AppSharedPrefs.getInstance(mContext)
        prefs.put(mContext.getString(R.string.key_about_us), data)
    }

    fun getAboutText(mContext: Context): String? {
        val prefs = AppSharedPrefs.getInstance(mContext)
        var data = ""
        data = try {
            prefs[mContext.getString(R.string.key_about_us)] as String
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            LogUtils.ERROR(e.message)
            return null
        }
        return data
    }


    fun setWhyUsText(mContext: Context, data: String) {
        if (mContext == null) return
        val prefs = AppSharedPrefs.getInstance(mContext)
        prefs.put(mContext.getString(R.string.key_why_us), data)
    }

    fun getWhyUsText(mContext: Context): String? {
        val prefs = AppSharedPrefs.getInstance(mContext)
        var data = ""
        data = try {
            prefs[mContext.getString(R.string.key_why_us)] as String
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            LogUtils.ERROR(e.message)
            return null
        }
        return data
    }


    /* fun saveImageData(mContext: Context, data: String) {
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
     }*/

}
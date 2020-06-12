package com.zaf.econnecto.utils.storage

import android.content.Context
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils

object PrefUtil {

    fun setBizId(mContext: Context?, data: String?) {
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

}
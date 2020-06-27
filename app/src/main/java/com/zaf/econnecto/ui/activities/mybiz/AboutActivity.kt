package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.layout_about_us.*
import org.json.JSONObject


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_about_us)
        initUI()
    }

    private fun initUI() {
        val data = PrefUtil.getBasicDetailsData(this)
        if (data != null ) {
            editBizDesc.setText(data.aboutDescription)
            editWhyUs.setText(data.aboutDescription)
        }

        textUpdateAbout.setOnClickListener {
            validateInput()
        }

        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateInput() {
        when {
            editBizDesc.text.toString().isEmpty() -> {
                LogUtils.showErrorDialog(this,getString(R.string.ok),getString(R.string.please_enter_a_detail_description))
            }
            editBizDesc.text.toString().length < 15 -> {
                LogUtils.showErrorDialog(this,getString(R.string.ok),getString(R.string.description_is_too_short))
            }
            editWhyUs.text.toString().isEmpty() -> {
                LogUtils.showErrorDialog(this,getString(R.string.ok),getString(R.string.please_enter_why_to_go_with_your_biz))
            }
            editWhyUs.text.toString().length < 15 -> {
                LogUtils.showErrorDialog(this,getString(R.string.ok),getString(R.string.about_is_too_short))
            }
            else -> {
                callAboutApi(editBizDesc.text.toString(),editWhyUs.text.toString())
            }
        }
    }

    private fun callAboutApi(desc: String, about: String) {
        val loader = AppDialogLoader.getLoader(this)
        loader.show()
        val url = AppConstant.URL_EDIT_ABOUT_WHY
        var jsonObject = JSONObject()
        jsonObject.put("jwt_token", Utils.getAccessToken(this))
        jsonObject.put("owner_id",Utils.getUserID(this))
        jsonObject.put("about_description",desc)
        jsonObject.put("about_why_us",about)

        LogUtils.DEBUG("URL : $url\nRequest Body :: ${jsonObject.toString()}")
        val objectRequest = MyJsonObjectRequest(this, Request.Method.POST, url, jsonObject, Response.Listener { response: JSONObject? ->
            LogUtils.DEBUG("About Section Response ::" + response.toString())
            try {
                if (response != null) {
                    val status = response.optInt("status")
                    if (status == AppConstant.SUCCESS) {
                        updateAboutSection()
                    } else {
                        LogUtils.showErrorDialog(this, getString(R.string.ok), response.optJSONArray("message").optString(0))
                    }
                    loader.dismiss()
                }
            } catch (e: Exception) {
                loader.dismiss()
                e.printStackTrace()
                LogUtils.ERROR(e.message)
            }
        }, Response.ErrorListener {
            error: VolleyError -> LogUtils.DEBUG("About Section Error ::" + error.message)
            loader.dismiss()
        })
        AppController.getInstance().addToRequestQueue(objectRequest, "About Section")
    }

    private fun updateAboutSection() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", "About Section Updated")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
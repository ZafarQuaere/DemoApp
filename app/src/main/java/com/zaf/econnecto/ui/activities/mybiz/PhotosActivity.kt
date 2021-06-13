package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.zaf.econnecto.R
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.MyJsonObjectRequest
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.adapters.StaggeredImageAdapter
import com.zaf.econnecto.ui.interfaces.DeleteImageListener
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.layout_photos.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class PhotosActivity : AppCompatActivity(), IMyBizImage {
    private lateinit var recyclerPhotos: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    lateinit var adapter: StaggeredImageAdapter
    lateinit var imageList: MutableList<ViewImageData>
    private val mContext = this
    private lateinit var myBizViewModel : MyBusinessViewModel
    private var imageUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_photos)
        initUI()
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        myBizViewModel.bizImageList(mContext as Activity?,this, PrefUtil.getBizId(mContext as Activity))
    }

    private fun initUI() {
        clickEvents()
    }


    suspend fun callDeleteImageApi(imageData: ViewImageData?, position: Int) {
        val loader = AppDialogLoader.getLoader(mContext)
        loader.show()
        val url = AppConstant.URL_DELETE_IMAGE
        val jObj = JSONObject();
        try {
            jObj.put("jwt_token", Utils.getAccessToken(mContext))
            jObj.put("owner_id", Utils.getUserID(mContext))
            jObj.put("img_id", imageData!!.imgId)
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
                        imageList.remove(imageData)
                        adapter.notifyDataSetChanged()
                        recyclerPhotos.removeViewAt(position)
                        adapter.notifyItemRemoved(position)
                        ImageUpdateModelListener.getInstance().changeState(true)
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


    private fun clickEvents() {
        textSubmit.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result", "data from secondActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun updateBannerImage(data: MutableList<ViewImageData>) {
        recyclerPhotos = findViewById<RecyclerView>(R.id.recycler_photos)
        recyclerPhotos.setHasFixedSize(true)
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        recyclerPhotos.layoutManager = layoutManager
        recyclerPhotos.itemAnimator = DefaultItemAnimator()
        imageList = data
//        imageList = PrefUtil.getImageData(this)!!
        adapter = imageList.let {
            StaggeredImageAdapter(this, it, true, object : DeleteImageListener {
                override fun onDeleteClick(data: ViewImageData?, position: Int) {
                    lifecycleScope.launch {
                        callDeleteImageApi(data, position)
                    }
                }
            })
        }!!
        recyclerPhotos.adapter = adapter
    }
}
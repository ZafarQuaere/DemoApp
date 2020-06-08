package com.zaf.econnecto.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.crop.CroppingHelper
import com.zaf.econnecto.crop.view.GestureCropImageView
import com.zaf.econnecto.crop.view.OverlayView
import com.zaf.econnecto.crop.view.UCropView
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.DealsBgData
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData
import com.zaf.econnecto.ui.adapters.VBHeaderImageRecylcerAdapter
import com.zaf.econnecto.ui.interfaces.AddPhotoDialogListener
import com.zaf.econnecto.ui.presenters.MyBusinessPresenterLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.AppLoaderFragment
import com.zaf.econnecto.utils.BitmapUtils
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.PermissionUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.vb_address_detail.imgEditBusiness

class MyBusinessActivityLatest : BaseActivity<MyBusinessPresenterLatest?>(), IMyBusinessLatest,ImageUpdateModelListener.ImageUpdateListener, View.OnClickListener {

    private var mContext: Context? = null

    private var selectedImageUri: Uri? = null
    private var loader: AppLoaderFragment? = null

    companion object {
        private const val GALLERY_IMAGE_CODE = 100
        private const val CAMERA_IMAGE_CODE = 200
    }

    override fun initPresenter(): MyBusinessPresenterLatest {
        return MyBusinessPresenterLatest(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_business)
        mContext = this
        loader = AppLoaderFragment.getInstance(mContext)
        updateActionbar()
        //presenter!!.callMyBizApi()
        presenter!!.callBannerImgApi()
    }

    private fun updateActionbar() {
        imgEditBusiness.visibility = View.VISIBLE
        imgEditBusiness.isEnabled = false

        val toolbar = findViewById<Toolbar>(R.id.toolbarBd)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toolbar.setNavigationOnClickListener { //finish();
            onBackPressed()
        }
    }


    override fun updateBannerImage(data: MutableList<out ViewImageData>) {
        val recyclerHeader = findViewById<RecyclerView>(R.id.recycler_header)
        recyclerHeader.layoutManager = LinearLayoutManager(this)
        recyclerHeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        val adapter = VBHeaderImageRecylcerAdapter(this, data.toList() as MutableList<ViewImageData>)
        recyclerHeader.adapter = adapter
    }

    override fun onClick(v: View?) {

    }

    fun uploadPhoto(view: View) {
        if (PermissionUtils.checkPermission(mContext)) {
            LogUtils.showAddPhotoDialog(mContext, object : AddPhotoDialogListener{
                override fun selectFromGallery() {
                    chooseFromGallery()
                }
                override fun takePhoto() {
                    captureFromCamera()
                }
            })

        } else {
            PermissionUtils.requestPermission(this@MyBusinessActivityLatest)
        }
    }

    private fun captureFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, CAMERA_IMAGE_CODE)
            }
        }
    }



    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            moveToEditImage(data,requestCode)
//            imgTemp.visibility = View.VISIBLE
           /* if (requestCode == GALLERY_IMAGE_CODE) {
                LogUtils.showToast(mContext, "From Gallery")
                imgTemp.setImageURI(selectedImageUri)

            } else if (requestCode == CAMERA_IMAGE_CODE) {
                //val uri = data.extras.get("data") as Uri // classcast exception bitmap can't be cast to URI
               imgTemp.setImageBitmap(data.extras.get("data") as Bitmap)
               //imgTemp.setImageURI(uri)
                LogUtils.showToast(mContext, "From Camera")
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun moveToEditImage(data: Intent, requestCode: Int) {
        val intent = Intent(this,EditImageActivity::class.java)
        ImageUpdateModelListener.getInstance().setImageUpdateListener(this)
        LogUtils.DEBUG("isImageUpdate CurrentState ${ImageUpdateModelListener.getInstance().changeState(false)} ")
        if (requestCode == GALLERY_IMAGE_CODE) {
            //val byteArray = BitmapUtils.getFileDataFromDrawable(BitmapUtils.getBitmap(mContext,selectedImageUri))
            intent.putExtra("imagePath",BitmapUtils.getImagePath(this,selectedImageUri,null,BitmapUtils.URI_IMAGE))
        } else if (requestCode == CAMERA_IMAGE_CODE) {
           val bitmap = data.extras.get("data") as Bitmap
            intent.putExtra("imagePath",BitmapUtils.getImagePath(this,null,bitmap,BitmapUtils.BITMAP_IMAGE))
        }
        //intent.putExtra("imageUri",selectedImageUri)
        startActivity(intent)
    }

    override fun isImageAdded() {
      var isImageUpdate =  ImageUpdateModelListener.getInstance().state
        LogUtils.DEBUG("isImageUpdate $isImageUpdate ")
        if (isImageUpdate){
            presenter!!.callBannerImgApi()
        }

    }

}
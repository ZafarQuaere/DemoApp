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
import com.zaf.econnecto.R
import com.zaf.econnecto.crop.CroppingHelper
import com.zaf.econnecto.crop.view.GestureCropImageView
import com.zaf.econnecto.crop.view.OverlayView
import com.zaf.econnecto.crop.view.UCropView
import com.zaf.econnecto.network_call.response_model.my_business.DealsBgData
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData
import com.zaf.econnecto.ui.interfaces.AddPhotoDialogListener
import com.zaf.econnecto.ui.presenters.MyBusinessPresenterLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.AppLoaderFragment
import com.zaf.econnecto.utils.BitmapUtils
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.PermissionUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.vb_address_detail.imgEditBusiness

class MyBusinessActivityLatest : BaseActivity<MyBusinessPresenterLatest?>(), IMyBusinessLatest, View.OnClickListener {
    private var mContext: Context? = null
    private var textFollowers: TextView? = null
    private var imgBannerUpload: ImageButton? = null
    private var imgProfileUpload: ImageButton? = null
    private var imgProfile: CircleImageView? = null
    private var imgBanner: ImageView? = null
    private var selectedImageUri: Uri? = null
    private var textPhone: TextView? = null
    private var loader: AppLoaderFragment? = null
    private var textEmail: TextView? = null
    private var textWebsite: TextView? = null
    private var textShortDescription: TextView? = null

    // private TextView textDetailDescription;
    private var textAddress: TextView? = null
    private var crop_layout: LinearLayout? = null
    private var mCroppingHelper: CroppingHelper? = null
    private var cropOptionintent: Intent? = null
    private var mUCropView: UCropView? = null
    private var mGestureCropImageView: GestureCropImageView? = null
    private var mOverlayView: OverlayView? = null
    private var mCompressFormat: Bitmap.CompressFormat? = null
    private var mCompressQuality = 0
    private lateinit var mAllowedGestures: IntArray
    private var btCancelCrop: ImageButton? = null
    private var btApplyCrop: ImageButton? = null
    private var dealsBgData: List<DealsBgData>? = null
    private var textUploadImgLable: TextView? = null
    private var imgBtnHotDeals: ImageButton? = null
    private var imgBackground: ImageView? = null
    private var editEnterDealsInfo: EditText? = null
    private var hotDealsBitmap: Bitmap? = null
    private var btnSubmitHotDeals: Button? = null
    private var textDealsNOffers: TextView? = null
    private var btnSelectBgColor: Button? = null
    private var mCurrentPhotoPath: String? = null

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
    }

    private fun updateActionbar() {
        //TODO this has to be done in update UI
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

    override fun onValidationError(msg: String?) {
    }

    override fun updateUI(bizDetails: MyBusinessData?) {
        // imgEditBusiness.visibility = View.VISIBLE
    }

    override fun updateImage(imageType: Int, bitmapUpload: Bitmap?) {
    }

    override fun updateBizData(address: String?, mobile: String?, email: String?, website: String?, shortDesc: String?, detailDesc: String?) {
    }

    override fun updateDealBackground(dealsBgData: MutableList<DealsBgData>?) {
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
      /*  val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, CAMERA_IMAGE_CODE)*/
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
}
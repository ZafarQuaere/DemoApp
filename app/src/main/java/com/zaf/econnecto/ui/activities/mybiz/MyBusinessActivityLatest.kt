package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
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
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.zaf.econnecto.R
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.request_model.AddressData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.BaseActivity
import com.zaf.econnecto.ui.activities.EditImageActivity
import com.zaf.econnecto.ui.adapters.TabViewPagerAdapter
import com.zaf.econnecto.ui.adapters.VBHeaderImageRecylcerAdapter
import com.zaf.econnecto.ui.fragments.details_frag.*
import com.zaf.econnecto.ui.interfaces.AddPhotoDialogListener
import com.zaf.econnecto.ui.presenters.MyBusinessPresenterLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import kotlinx.android.synthetic.main.activity_my_business_latest.*
import kotlinx.android.synthetic.main.vb_address_detail.*
import kotlinx.android.synthetic.main.vb_communication_menu.*

class MyBusinessActivityLatest : BaseActivity<MyBusinessPresenterLatest?>(), IMyBusinessLatest,ImageUpdateModelListener.ImageUpdateListener,OnMapReadyCallback {

    private var mContext: Context? = null

    private var selectedImageUri: Uri? = null
    private var loader: AppLoaderFragment? = null
    private lateinit var gMap : GoogleMap
    private lateinit var viewPagerTabs : ViewPager
    private lateinit var tabLayout : TabLayout

    companion object {
        private const val GALLERY_IMAGE_CODE = 100
        private const val CAMERA_IMAGE_CODE = 200
        private const val UPDATE_DETAILS_CODE = 111
        private const val UPDATE_OPERATING_HOUR_CODE = 222
    }

    override fun initPresenter(): MyBusinessPresenterLatest {
        return MyBusinessPresenterLatest(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_business_latest)
        mContext = this
        loader = AppLoaderFragment.getInstance(mContext)
        updateActionbar()
        presenter!!.callBasicDetailsApi(true)
//        presenter!!.callBannerImgApi()
        presenter!!.initMap(this,mapFrag)
        updateMyBizUI()
    }

    private fun updateMyBizUI() {
        viewPagerTabs = findViewById<ViewPager>(R.id.viewpagerTabs)
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPagerTabs)
        setupViewPager(viewPagerTabs)

        textFollow.text = getString(R.string.edit_details)
        textOperatingHours.setOnClickListener {
            startActivityForResult(Intent(this,OperatingHour::class.java), UPDATE_OPERATING_HOUR_CODE)
        }
        textFollow.setOnClickListener {
            startActivityForResult(Intent(this, EditDetails::class.java), UPDATE_DETAILS_CODE)
        }
        rlytLocation.setOnClickListener {
//            LogUtils.showToast(this,"clicked location")
            mapFrag.requireView().visibility = if (mapFrag.requireView().visibility == View.VISIBLE)
                View.GONE
            else
                View.VISIBLE

        }
    }

    private fun setupViewPager(viewPagerTabs: ViewPager?) {
        val adapter = TabViewPagerAdapter(this.getSupportFragmentManager(), arrayListOf<String>("First Fragment","Second Fragment","Third Fragment","Fourth Fragment","Fifth Fragment"))
        adapter.addFragment(FirstFragment(), "Menu")
        adapter.addFragment(SecondFragment(), "About")
        adapter.addFragment(ThirdFragment(), "Amenities")
        adapter.addFragment(FourthFragment(), "Photos")
        adapter.addFragment(FifthFragment(), "Payment")
        viewPagerTabs!!.adapter = adapter
    }

    private fun updateActionbar() {

        val toolbar = findViewById<Toolbar>(R.id.toolbarBd)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toolbar.setNavigationOnClickListener { //finish();
            onBackPressed()
        }
    }

    override fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse, imageUpdate: Boolean) {
        //update address related data
        //get the business owner id and call image api
            updateBasicDetails(basicDetailsResponse)
        if (imageUpdate)
             presenter!!.callBannerImgApi()
    }

    @SuppressLint("SetTextI18n")
    private fun updateBasicDetails(data: BasicDetailsResponse) {
       val basicDetailsDta = data.data[0]
        textBusinessName.text = basicDetailsDta.businessName
        textFollowers.text =  "${basicDetailsDta.followersCount} "+getString(R.string.followers)
        textShortDescription.text =  basicDetailsDta.shortDescription
        textEstablishedDate.text =  getString(R.string.established_year)+": ${basicDetailsDta.yearEstablished}"
        textAddress.text =  basicDetailsDta.address1
        textWebsite.text =  if (!basicDetailsDta.website.isNullOrEmpty()) basicDetailsDta.website else ""
        updateMap(basicDetailsDta)

    }

    private fun updateMap(basicDetailsDta: BasicDetailsData) {
        val fullAddress: String =basicDetailsDta.address1 + ", " +basicDetailsDta.cityTown + ", " + basicDetailsDta.state + ", " + basicDetailsDta.pinCode
       val location = KotUtil.getLocationFromAddress(this, fullAddress)!!
        val address = AddressData(basicDetailsDta.address1, basicDetailsDta.state, basicDetailsDta.cityTown , basicDetailsDta.pinCode, location.latitude.toString() + "", "" + location.longitude)
//        location.latitude,location.longitude
        LogUtils.DEBUG(address.toString())
        val ny = LatLng(location!!.latitude, location!!.longitude)
        //val ny = LatLng(-34.0, 151.0)
        val markerOptions = MarkerOptions()
        markerOptions.position(ny)
        gMap.addMarker(markerOptions)
        gMap.moveCamera(CameraUpdateFactory.newLatLng(ny))
    }


    override fun updateBannerImage(data: MutableList<ViewImageData>) {
        val recyclerHeader = findViewById<RecyclerView>(R.id.recycler_header)
        recyclerHeader.layoutManager = LinearLayoutManager(this)
        recyclerHeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        val adapter = VBHeaderImageRecylcerAdapter(this, data.toList() as MutableList<ViewImageData>)
        recyclerHeader.adapter = adapter
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
        super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == RESULT_OK && data != null) {
                when (requestCode) {
                    UPDATE_DETAILS_CODE -> {
                        presenter!!.callBasicDetailsApi(false)
                    }
                    UPDATE_OPERATING_HOUR_CODE -> {
                        LogUtils.showToast(this,"Coming from operating hour")
                    }
                    else -> {
                        selectedImageUri = data.data
                        moveToEditImage(data, requestCode)
                    }
                }
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

    }

    private fun moveToEditImage(data: Intent, requestCode: Int) {
        val intent = Intent(this, EditImageActivity::class.java)
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


    override fun onMapReady(googleMap: GoogleMap?) {
        gMap = googleMap!!
        gMap.setMinZoomPreference(12F)
        gMap.setIndoorEnabled(true)
        val uiSettings: UiSettings = gMap.getUiSettings()
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isMyLocationButtonEnabled = true
        uiSettings.isMapToolbarEnabled = true
        uiSettings.isCompassEnabled = true
        uiSettings.isZoomControlsEnabled = true

//        val fullAddress: String = basicDetailsDta.address1 + ", " + basicDetailsDta.cityTown + ", " + basicDetailsDta.state + ", " + basicDetailsDta.pinCode
//        val location: Address = KotUtil.getLocationFromAddress(this, fullAddress)!!
//        val address = AddressData(basicDetailsDta.address1, basicDetailsDta.state, basicDetailsDta.cityTown , basicDetailsDta.pinCode, location.latitude.toString() + "", "" + location.longitude)
//
////        location.latitude,location.longitude
//        println(address)

//        val ny = LatLng(28.6523644, 77.1907413)
//        val ny = LatLng(location!!.latitude, location!!.longitude)
//        //val ny = LatLng(-34.0, 151.0)
//        val markerOptions = MarkerOptions()
//        markerOptions.position(ny)
//        gMap.addMarker(markerOptions)
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(ny))
    }
}
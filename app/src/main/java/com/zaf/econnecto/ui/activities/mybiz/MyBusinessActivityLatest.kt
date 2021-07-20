@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.zaf.econnecto.BuildConfig
import com.zaf.econnecto.R
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.request_model.AddressData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.BaseActivity
import com.zaf.econnecto.ui.activities.EditImageActivity
import com.zaf.econnecto.ui.activities.mybiz.fragments.*
import com.zaf.econnecto.ui.adapters.*
import com.zaf.econnecto.ui.fragments.details_frag.*
import com.zaf.econnecto.ui.interfaces.AddPhotoDialogListener
import com.zaf.econnecto.ui.interfaces.DeleteProductListener
import com.zaf.econnecto.ui.interfaces.DialogButtonClick
import com.zaf.econnecto.ui.presenters.MyBusinessPresenterLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.activity_my_business_latest.*
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.android.synthetic.main.mb_address_detail.*
import kotlinx.android.synthetic.main.mb_communication_menu.*
import kotlinx.android.synthetic.main.mb_layout_about.*
import kotlinx.android.synthetic.main.mb_layout_amenities.*
import kotlinx.android.synthetic.main.mb_layout_brochure.*
import kotlinx.android.synthetic.main.mb_layout_categories.*
import kotlinx.android.synthetic.main.mb_layout_payment.*
import kotlinx.android.synthetic.main.mb_layout_photos.*
import kotlinx.android.synthetic.main.mb_layout_pricing.*
import kotlinx.android.synthetic.main.mb_layout_product_services.*
import kotlinx.android.synthetic.main.mb_operating_hours.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MyBusinessActivityLatest : BaseActivity<MyBusinessPresenterLatest?>(), IMyBizImage, IMyBusinessLatest, ImageUpdateModelListener.ImageUpdateListener, OnMapReadyCallback {

    private var mContext: Activity? = null

    private var selectedImageUri: Uri? = null
    private var loader: AppLoaderFragment? = null
    private lateinit var gMap: GoogleMap
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var myBizViewModel: MyBusinessViewModel
    private var isBrochure: Boolean = false
    lateinit var recyclerHeader: RecyclerView
    lateinit var rootContent: CoordinatorLayout
    lateinit var bannerImageAdapter: VBHeaderImageRecylcerAdapter
    lateinit var imageList: MutableList<ViewImageData>

    companion object {
        const val GALLERY_IMAGE_CODE = 100
        const val CAMERA_IMAGE_CODE = 200
        const val UPDATE_DETAILS_CODE = 111
        const val UPDATE_OPERATING_HOUR_CODE = 112
        const val UPDATE_PRODUCT_SERVICES = 113
        const val UPDATE_ABOUT_US = 114
        const val UPDATE_AMENITIES = 115
        const val UPDATE_PHOTOS = 116
        const val UPDATE_PAYMENTS = 117
        const val UPDATE_CATEGORY = 118
        const val UPDATE_PRICING = 119
    }

    override fun initPresenter(): MyBusinessPresenterLatest {
        return MyBusinessPresenterLatest(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_business_latest)
        initUI()
        mContext = this
        presenter!!.initMap(this, mapFrag)
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        myBizViewModel.callMyBizBasicDetails(this, true, this, Utils.getUserID(mContext))
        loader = AppLoaderFragment.getInstance(mContext)
        updateActionbar()
        updateMyBizUI()
    }

    private fun initUI() {
        recyclerHeader = findViewById<RecyclerView>(R.id.recycler_header)
        rootContent = findViewById<CoordinatorLayout>(R.id.rootContent)
    }


    private fun subscribeViewModels() {
        myBizViewModel.basicDetailsData.observe(this, Observer { data ->
            data.let {
                //TODO from here you can update the basic details data
            }
        })
        myBizViewModel.isImageDeleted.observe(this, Observer { it.let { isImageDeleted -> updateImageList(isImageDeleted) }})
    }

    private fun updateImageList(isImageDeleted: Boolean) {
        if (isImageDeleted)
            callImageListApi()
    }

    private fun updateMyBizUI() {
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        viewPagerTabs = findViewById<ViewPager>(R.id.viewpagerTabs)
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPagerTabs)

        textFollow.text = getString(R.string.edit_details)
        textFollow.setOnClickListener {
            startActivityForResult(Intent(this, EditDetails::class.java), UPDATE_DETAILS_CODE)
        }
        rlytOpHours.setOnClickListener {
            startActivityForResult(Intent(this, OperatingHour::class.java), UPDATE_OPERATING_HOUR_CODE)
        }
        rlytLocation.setOnClickListener {
            mapFrag.requireView().visibility = if (mapFrag.requireView().visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        textAddProductNServices.setOnClickListener {
            startActivityForResult(Intent(this, ProductAndServices::class.java), UPDATE_PRODUCT_SERVICES)
        }

    }

    private fun setupViewPager(viewPagerTabs: ViewPager?) {
        val adapter = TabViewPagerAdapter(this.supportFragmentManager, arrayListOf<String>("Photos Fragment","About Fragment","Amenities Fragment",/*"Brochure Fragment",*/"Categories Fragment","Payment Fragment","Pricing Fragment"))
        adapter.addFragment(PhotosFragment(), "Photos")
        adapter.addFragment(AboutFragment(), "About")
        adapter.addFragment(AmenitiesFragment(), "Amenities")
        adapter.addFragment(CategoriesFragment(), "Categories")
        adapter.addFragment(PaymentFragment(), "Payment")
        adapter.addFragment(PricingFragment(), "Pricing")
//        adapter.addFragment(BrochureFragment(), "Brochure")
        viewPagerTabs!!.adapter = adapter
    }


    private fun updateActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbarBd)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse, imageUpdate: Boolean) {
        //update address related data
        //get the business owner id and call image api
        updateBasicDetails(basicDetailsResponse)
        setupViewPager(viewPagerTabs)
        if (imageUpdate) {
            callImageListApi()
            myBizViewModel.bizProductServicesList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
            callAmenityListApi()
            callBizCategoryListApi()
            callPayOptionListApi()
            callPricingListApi()
            myBizViewModel.bizOperatingHours(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
        }
    }

    fun callPricingListApi() {
        myBizViewModel.bizPricingList(mContext as Activity?, PrefUtil.getBizId(mContext as Activity))
    }

    fun callPayOptionListApi() {
        myBizViewModel.bizPaymentMethodList(mContext as Activity?, PrefUtil.getBizId(mContext as Activity))
    }

    fun callBizCategoryListApi() {
        myBizViewModel.bizCategoryList(mContext as Activity?, PrefUtil.getBizId(mContext as Activity))
    }

    @SuppressLint("SetTextI18n")
    private fun updateBasicDetails(data: BasicDetailsResponse) {
        val basicDetailsDta = data.data[0]
        mContext?.let { PrefUtil.setAboutText(it,basicDetailsDta.aboutDescription) }
        mContext?.let { PrefUtil.setWhyUsText(it,basicDetailsDta.aboutWhyUs) }
        textBusinessName.text = basicDetailsDta.businessName
        textFollowers.text = "${basicDetailsDta.followersCount} " + getString(R.string.followers)
        textShortDescription.text = basicDetailsDta.shortDescription
        textEstablishedDate.text = getString(R.string.established_year) + ": ${basicDetailsDta.yearEstablished}"
        textAddress.text = basicDetailsDta.address1
        textWebsite.text = if (!basicDetailsDta.website.isNullOrEmpty()) basicDetailsDta.website else ""
//        updateAboutSection(basicDetailsDta)
        updateMap(basicDetailsDta)

        rlytCall.setOnClickListener {
            Utils.callPhone(mContext, basicDetailsDta.mobile1)
        }

        rlytWhatsApp.setOnClickListener {
            Utils.openWhatsApp(mContext, basicDetailsDta.mobile1)
        }
        rlytMail.setOnClickListener {
            basicDetailsDta.email?.let { Utils.openMail(mContext, it) }
        }
        rlytShareBiz.setOnClickListener {
            mapFrag.requireView().visibility = View.GONE
            Handler().postDelayed({
                shareScreenContent()
            },100)
        }
    }

    private fun shareScreenContent() {
        val bitmap: Bitmap? = ScreenshotUtils.getScreenShot(rootContent)
        if (bitmap != null) {
            val saveFile = ScreenshotUtils.getMainDirectoryName(this) //get the path to save screenshot
            val file = ScreenshotUtils.store(bitmap, "econnecto_screenshot"  + ".jpg", saveFile!!) //save the screenshot to selected path
            shareScreenshot(file) //finally share screenshot
            mapFrag.requireView().visibility = View.VISIBLE
        } else {
            //If bitmap is null show toast message
            LogUtils.showToast(mContext,"Failed to capture screen");
        }
    }

    private fun shareScreenshot(file: File?) {
        val uri = file?.let { FileProvider.getUriForFile(baseContext,applicationContext.packageName+".provider", it) } //Convert file path into Uri for sharing
        val intent = Intent()
        var shareMessage: String = getString(R.string.share_app_content)
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        intent.putExtra(Intent.EXTRA_STREAM, uri) //pass uri here
        startActivity(Intent.createChooser(intent, "Share Business"))
    }

    private fun updateMap(basicDetailsDta: BasicDetailsData) {
        if (basicDetailsDta != null) {
            val fullAddress: String = basicDetailsDta.address1 + ", " + basicDetailsDta.cityTown + ", " + basicDetailsDta.state + ", " + basicDetailsDta.pinCode
            val location : Address? = KotUtil.getLocationFromAddress(this, fullAddress)
            val address = AddressData(basicDetailsDta.address1, basicDetailsDta.state, basicDetailsDta.cityTown, basicDetailsDta.pinCode, location?.latitude.toString() + "", "" + location?.longitude)
//          location.latitude,location.longitude
            LogUtils.DEBUG(address.toString())
            val storeLocation = location?.longitude?.let { LatLng(location?.latitude, it) }
            //val ny = LatLng(-34.0, 151.0)
            val markerOptions = MarkerOptions()
            storeLocation?.let { markerOptions.position(it) }
            if (gMap != null) {
                gMap.addMarker(markerOptions)
                gMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation))
            }
        }

    }


    override fun updateBannerImage(data: MutableList<ViewImageData>) {
        recyclerHeader.layoutManager = LinearLayoutManager(this)
        recyclerHeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageList = data
        bannerImageAdapter = VBHeaderImageRecylcerAdapter(this, imageList)
        recyclerHeader.adapter = bannerImageAdapter
    }


    fun uploadPhoto(view: View) {
        if (PermissionUtils.checkPermission(mContext)) {
            isBrochure = false
            LogUtils.showAddPhotoDialog(mContext, object : AddPhotoDialogListener {

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

    fun uploadBrochure(view: View) {
        if (PermissionUtils.checkPermission(mContext)) {
            isBrochure = true
            chooseFromGallery()
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
                    myBizViewModel.callMyBizBasicDetails(this, false, this, Utils.getUserID(mContext))
                }
                UPDATE_OPERATING_HOUR_CODE -> {
                    LogUtils.DEBUG("Coming from operating hour")
                }
                UPDATE_PRODUCT_SERVICES -> {
                    //call product and service api to update UI
                    myBizViewModel.bizProductServicesList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
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
            intent.putExtra("imagePath", BitmapUtils.getImagePath(this, selectedImageUri, null, BitmapUtils.URI_IMAGE))
        } else if (requestCode == CAMERA_IMAGE_CODE) {
            val bitmap = data.extras.get("data") as Bitmap
            intent.putExtra("imagePath", BitmapUtils.getImagePath(this, null, bitmap, BitmapUtils.BITMAP_IMAGE))
        }
        startActivity(intent)
    }

    override fun isImageAdded() {
        var isImageUpdate = ImageUpdateModelListener.getInstance().state
        LogUtils.DEBUG("isImageUpdate $isImageUpdate ")
        if (isImageUpdate) {
            myBizViewModel.bizImageList(mContext as Activity?, this, PrefUtil.getBizId(mContext as Activity))
        }

    }

    override fun onResume() {
        super.onResume()
        subscribeViewModels()
        val isImageUpdate = ImageUpdateModelListener.getInstance().state
        LogUtils.DEBUG("isImageUpdate $isImageUpdate ")
        if (isImageUpdate) {
            callImageListApi()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        gMap = googleMap!!
        gMap.setMinZoomPreference(12F)
        gMap.isIndoorEnabled = true
        val uiSettings: UiSettings = gMap.getUiSettings()
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isMyLocationButtonEnabled = true
        uiSettings.isMapToolbarEnabled = true
        uiSettings.isCompassEnabled = true
        uiSettings.isZoomControlsEnabled = true
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun updateOperatingHours(data: List<OPHoursData>?) {
        if (data != null) {
           val storeStatus =  data[data.size-1].current_status
            iconOpenClose.background = if (storeStatus == "Closed") getDrawable(R.drawable.ic_circle_red) else getDrawable(R.drawable.ic_circle_green)
            textOperatingHours.text = if (storeStatus == "Closed") "Closed" else getOPTiming(data)
        }
    }

    private fun getOPTiming(data: List<OPHoursData>): String {
        val sdf = SimpleDateFormat("EEE")
        val d = Date()
        val dayOfTheWeek: String = sdf.format(d)
        println("Day of the week : $dayOfTheWeek")
        val timing :String =  KotUtil.getOpenCloseTime(dayOfTheWeek,data)
        return "Open $timing"
    }

    override fun updateProductServiceSection(data: List<ProductNServiceData>?) {
        if (data == null || data.isEmpty()) {
            textPnDHeader.text = getString(R.string.product_n_services)
            textAddProductNServices.visibility = View.VISIBLE
            listViewProductServices.visibility = View.GONE
            textAdd.visibility = View.GONE
        } else {
            updateProductServiceUI(data)
        }
    }

    private fun updateProductServiceUI(data: List<ProductNServiceData>) {
        if (data.isNotEmpty()) {
            val listViewProductServices = findViewById<RecyclerView>(R.id.listViewProductServices)
            textPnDHeader.text = getString(R.string.deals_in)
            textAddProductNServices.visibility = View.GONE
            textAdd.visibility = View.VISIBLE
            listViewProductServices.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(mContext)
            listViewProductServices.layoutManager = layoutManager
            listViewProductServices.itemAnimator = DefaultItemAnimator()

            val adapter = BizProdNServiceListAdapter(this, data, object : DeleteProductListener {
                override fun deleteProd(prodData: ProductNServiceData) {
                    LogUtils.showDialogDoubleButton(mContext,
                        getString(R.string.cancel),
                        getString(R.string.ok),
                        getString(R.string.do_you_really_want_to_delete),
                        object : DialogButtonClick {
                            override fun onOkClick() {
                                myBizViewModel.removeProductOrService(mContext, prodData.prod_serv_id, this@MyBusinessActivityLatest, PrefUtil.getBizId(mContext as Activity))
                            }
                            override fun onCancelClick() {}
                        })
                }
            })
            listViewProductServices.adapter = adapter
            textAdd.setOnClickListener {
                startActivityForResult(Intent(this, ProductAndServices::class.java), UPDATE_PRODUCT_SERVICES)
            }
        }

    }

    override fun updateBrochureSection(data: List<BrochureData>?) {
    }

    override fun updateAmenitiesSection(data: List<AmenityData>?) {
        //This will be removed later
    }

    private fun updateBrochureUI() {}

    override fun updatePaymentSection(data: List<PaymentMethodData>?) {}

    override fun updatePricingSection(data: List<PricingData>?) {}

    override fun updateCategories(data: List<UserCategoryData>?) {}

    fun callImageListApi() {
        myBizViewModel.bizImageList(mContext as Activity?, this, PrefUtil.getBizId(mContext as Activity))
    }

    suspend fun callDeleteImageApi(imageData: ViewImageData, position: Int) {
        myBizViewModel.callDeleteImageApi(this,imageData,position)
    }

    fun callAmenityListApi() {
        myBizViewModel.bizAmenityList(mContext as Activity?, null,PrefUtil.getBizId(mContext as Activity))
    }
}
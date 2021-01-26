@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.zaf.econnecto.ui.activities.mybiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
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
import com.zaf.econnecto.R
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.network_call.request_model.AddressData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.BaseActivity
import com.zaf.econnecto.ui.activities.EditImageActivity
import com.zaf.econnecto.ui.adapters.*
import com.zaf.econnecto.ui.interfaces.AddPhotoDialogListener
import com.zaf.econnecto.ui.interfaces.DeleteCategoryListener
import com.zaf.econnecto.ui.interfaces.DeleteProductListener
import com.zaf.econnecto.ui.presenters.MyBusinessPresenterLatest
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.*
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.activity_my_business_latest.*
import kotlinx.android.synthetic.main.vb_address_detail.*
import kotlinx.android.synthetic.main.vb_communication_menu.*
import kotlinx.android.synthetic.main.vb_layout_about.*
import kotlinx.android.synthetic.main.vb_layout_amenities.*
import kotlinx.android.synthetic.main.vb_layout_brochure.*
import kotlinx.android.synthetic.main.vb_layout_categories.*
import kotlinx.android.synthetic.main.vb_layout_payment.*
import kotlinx.android.synthetic.main.vb_layout_photos.*
import kotlinx.android.synthetic.main.vb_layout_pricing.*
import kotlinx.android.synthetic.main.vb_layout_product_services.*
import kotlinx.android.synthetic.main.vb_operating_hours.*

class MyBusinessActivityLatest : BaseActivity<MyBusinessPresenterLatest?>(), IMyBizImage, IMyBusinessLatest, ImageUpdateModelListener.ImageUpdateListener, OnMapReadyCallback {

    private lateinit var basicDetailsDta: BasicDetailsData
    private var mContext: Activity? = null

    private var selectedImageUri: Uri? = null
    private var loader: AppLoaderFragment? = null
    private lateinit var gMap: GoogleMap
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var myBizViewModel: MyBusinessViewModel
    private var isBrochure: Boolean = false

    companion object {
        private const val GALLERY_IMAGE_CODE = 100
        private const val CAMERA_IMAGE_CODE = 200
        private const val UPDATE_DETAILS_CODE = 111
        private const val UPDATE_OPERATING_HOUR_CODE = 112
        private const val UPDATE_PRODUCT_SERVICES = 113
        private const val UPDATE_ABOUT_US = 114
        private const val UPDATE_AMENITIES = 115
        private const val UPDATE_PHOTOS = 116
        private const val UPDATE_PAYMENTS = 117
    }

    override fun initPresenter(): MyBusinessPresenterLatest {
        return MyBusinessPresenterLatest(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_business_latest)
        presenter!!.initMap(this, mapFrag)
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        mContext = this
        loader = AppLoaderFragment.getInstance(mContext)
        updateActionbar()
        subscribeViewModels()
        myBizViewModel.callBasicDetailsApi(this, true, this, Utils.getUserID(mContext))
        updateMyBizUI()
    }

    private fun subscribeViewModels() {
        myBizViewModel.basicDetailsData.observe(this, Observer { data ->
            data.let {
                //TODO from here you can update the basic details data
            }
        })
    }

    private fun updateMyBizUI() {
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        addTabsWithoutVP()

        textFollow.text = getString(R.string.edit_details)
        textFollow.setOnClickListener {
            startActivityForResult(Intent(this, EditDetails::class.java), UPDATE_DETAILS_CODE)
        }
        iconEditOPHour.setOnClickListener {
            startActivityForResult(Intent(this, OperatingHour::class.java), UPDATE_OPERATING_HOUR_CODE)
        }
        rlytLocation.setOnClickListener {
            mapFrag.requireView().visibility = if (mapFrag.requireView().visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        textAddProductNServices.setOnClickListener {
            startActivityForResult(Intent(this, ProductAndServices::class.java), UPDATE_PRODUCT_SERVICES)
        }
        textAboutDescLabel.setOnClickListener {
            startActivityForResult(Intent(this, AboutActivity::class.java), UPDATE_ABOUT_US)
        }
        textAboutWhyUsLabel.setOnClickListener {
            startActivityForResult(Intent(this, AboutActivity::class.java), UPDATE_ABOUT_US)
        }
        textAboutEdit.setOnClickListener {
            startActivityForResult(Intent(this, AboutActivity::class.java), UPDATE_ABOUT_US)
        }

        textAddAmenities.setOnClickListener {
            startActivityForResult(Intent(this, AmenitiesActivity::class.java), UPDATE_AMENITIES)
        }

        textAddPayments.setOnClickListener {
            startActivityForResult(Intent(this, PaymentsOptions::class.java), UPDATE_PAYMENTS)
        }

    }

    private fun addTabsWithoutVP() {
        tabLayout.addTab(tabLayout.newTab().setText("Brochure"))
        tabLayout.addTab(tabLayout.newTab().setText("About"))
        tabLayout.addTab(tabLayout.newTab().setText("Photos"))
        tabLayout.addTab(tabLayout.newTab().setText("Amenities"))
        tabLayout.addTab(tabLayout.newTab().setText("Payment"))
        tabLayout.addTab(tabLayout.newTab().setText("Pricing"))
        tabLayout.addTab(tabLayout.newTab().setText("Categories"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                scrollUpto(tab!!.text as String?)
            }

        })
    }

    private fun scrollUpto(text: String?) {
        when (text) {
            "About" -> {
                scroll.scrollTo(0, textAboutWhyUsLabel.top + 100)
                textAboutWhyUsLabel.requestFocus()
            }
            "Brochure" -> {
                scroll.scrollTo(0, textUploadBrochure.top - 50)
                textUploadBrochure.requestFocus()
            }
            "Photos" -> {
//                scrollToRow(scroll,layoutPhotos,textAddPhotos)
                scroll.scrollTo(0, textAddPhotos.top + 1000)
                textAddPhotos.requestFocus()
            }
            "Amenities" -> {
                scroll.scrollTo(0, textAddAmenities.top + 2000)
                textAddAmenities.requestFocus()
            }
            "Payment" -> {
                scroll.scrollTo(0, textAddPayments.top + 2500)
                textAddPayments.requestFocus()
            }

            "Pricing" -> {
                scroll.scrollTo(0, textAddPricing.top + 2900)
                textAddPricing.requestFocus()
            }
            "Categories" -> {
                scroll.scrollTo(0, textAddCategory.bottom + 3100)
                textAddCategory.requestFocus()
            }
//            "Payment" ->  scroll.scrollTo(0,textAddPayments.top +2600) //scrollToRow(scroll,layoutPayment,textAddPayments)
            else -> scroll.scrollTo(0, textAboutWhyUsLabel.top + 2600)
        }

    }

    fun scrollToRow(nsv: NestedScrollView, layout: LinearLayout, tv: TextView) {
//        val delay : Long = 100
//        nsv.postDelayed(object: Runnable{
//            override fun run() {
//                val rect = Rect()
//                tv.getHitRect(rect)
//                nsv.requestChildRectangleOnScreen(layout,rect,false)
//            }
//
//        },delay)

//        val targetScroll: Int = nsv.getScrollY() + 1000
        val targetScroll: Int = tv.getScrollY()
        nsv.scrollTo(0, targetScroll)
        nsv.setSmoothScrollingEnabled(true)
        ViewCompat.setNestedScrollingEnabled(nsv, false)
        val currentScrollY: Int = nsv.getScrollY()
        ViewCompat.postOnAnimationDelayed(nsv, object : Runnable {
            var currentY = currentScrollY
            override fun run() {
                if (currentScrollY == nsv.getScrollY()) {
                    ViewCompat.setNestedScrollingEnabled(nsv, true)
                    return
                }
                currentY = nsv.getScrollY()
                ViewCompat.postOnAnimation(nsv, this)
            }
        }, 10)
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
        if (imageUpdate) {
            myBizViewModel.bizImageList(mContext as Activity?, this, PrefUtil.getBizId(mContext as Activity))
            myBizViewModel.bizOperatingHours(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
            myBizViewModel.bizAmenityList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
            myBizViewModel.bizProductServicesList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
            myBizViewModel.bizBrochureList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
            myBizViewModel.bizPaymentMethodList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
            myBizViewModel.bizPricingList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
            myBizViewModel.bizCategoryList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateBasicDetails(data: BasicDetailsResponse) {
        val basicDetailsDta = data.data[0]
        textBusinessName.text = basicDetailsDta.businessName
        textFollowers.text = "${basicDetailsDta.followersCount} " + getString(R.string.followers)
        textShortDescription.text = basicDetailsDta.shortDescription
        textEstablishedDate.text = getString(R.string.established_year) + ": ${basicDetailsDta.yearEstablished}"
        textAddress.text = basicDetailsDta.address1
        textWebsite.text = if (!basicDetailsDta.website.isNullOrEmpty()) basicDetailsDta.website else ""
        updateAboutSection(basicDetailsDta)
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
        rlytExpandAddress.setOnClickListener {
            LogUtils.showToast(mContext, "expand address")
        }
    }

    private fun updateMap(basicDetailsDta: BasicDetailsData) {
        if (basicDetailsDta != null) {
            val fullAddress: String = basicDetailsDta.address1 + ", " + basicDetailsDta.cityTown + ", " + basicDetailsDta.state + ", " + basicDetailsDta.pinCode
            val location = KotUtil.getLocationFromAddress(this, fullAddress)!!
            val address = AddressData(basicDetailsDta.address1, basicDetailsDta.state, basicDetailsDta.cityTown, basicDetailsDta.pinCode, location.latitude.toString() + "", "" + location.longitude)
//        location.latitude,location.longitude
            LogUtils.DEBUG(address.toString())
            val storeLocation = LatLng(location!!.latitude, location!!.longitude)
            //val ny = LatLng(-34.0, 151.0)
            val markerOptions = MarkerOptions()
            markerOptions.position(storeLocation)
            if (gMap != null) {
                gMap.addMarker(markerOptions)
                gMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation))
            }
        }

    }


    override fun updateBannerImage(data: MutableList<ViewImageData>) {
        val recyclerHeader = findViewById<RecyclerView>(R.id.recycler_header)
        recyclerHeader.layoutManager = LinearLayoutManager(this)
        recyclerHeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = VBHeaderImageRecylcerAdapter(this, data.toList() as MutableList<ViewImageData>)
        recyclerHeader.adapter = adapter
        updatePhotoSection(data)
    }

    private fun updatePhotoSection(data: MutableList<ViewImageData>) {
        if (data != null) {
            textAddPhotos.visibility = View.GONE
            recycler_photos.visibility = View.VISIBLE
            textPhotoLabel.visibility = View.VISIBLE
            textSeeMorePhotos.bringToFront()
            val layoutManager = GridLayoutManager(mContext, 2)
            recycler_photos!!.layoutManager = layoutManager
            recycler_photos!!.itemAnimator = DefaultItemAnimator()

            val list1 = mutableListOf<ViewImageData>(data[0], data[1], data[2], data[3])
//            val adapter = StaggeredImageAdapter(this, list1, false, null)
            val adapter = VBHeaderImageRecylcerAdapter(this, list1)
            recycler_photos!!.adapter = adapter
            textSeeMorePhotos.setOnClickListener {
                LogUtils.showToast(mContext, "see more..")
                startActivityForResult(Intent(this, PhotosActivity::class.java), UPDATE_PHOTOS)
            }
        } else {
            textAddPhotos.visibility = View.VISIBLE
            recycler_photos.visibility = View.GONE
        }
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
                    myBizViewModel.callBasicDetailsApi(this, false, this, Utils.getUserID(mContext))
                }
                UPDATE_OPERATING_HOUR_CODE -> {
                    LogUtils.DEBUG("Coming from operating hour")
                }
                UPDATE_PRODUCT_SERVICES -> {
                    //call product and service api to update UI
                    myBizViewModel.bizProductServicesList(mContext as Activity?, this,PrefUtil.getBizId(mContext as Activity))
                }
                UPDATE_ABOUT_US -> {
                    LogUtils.DEBUG("Coming from about services")
                    myBizViewModel.callBasicDetailsApi(this, false, this, Utils.getUserID(mContext))
                }
                UPDATE_AMENITIES -> {
                    LogUtils.DEBUG("Coming from amenities")
                    myBizViewModel.bizAmenityList(mContext, this,PrefUtil.getBizId(mContext as Activity))
                }
                UPDATE_PAYMENTS -> {
                    LogUtils.DEBUG("Coming from PaymentOptions")
                    myBizViewModel.bizPaymentMethodList(mContext, this, PrefUtil.getBizId(mContext as Activity))
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
            presenter!!.callImageApi()
        }

    }

    override fun onResume() {
        super.onResume()
        val isImageUpdate = ImageUpdateModelListener.getInstance().state
        LogUtils.DEBUG("isImageUpdate $isImageUpdate ")
        if (isImageUpdate) {
            presenter!!.callImageApi()
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
    override fun updateOperatingHours(data: OPHoursData) {
       if (data != null) {
           iconOpenClose.background = if (data.CurrentStatus == "Closed") getDrawable(R.drawable.ic_circle_red) else getDrawable(R.drawable.ic_circle_green)
           textOperatingHours.text = getOPTiming(data)
       }
    }

    private fun getOPTiming(data: OPHoursData): String {
       //Todo

        return "Operating Hours"
    }

    override fun updateProductServiceSection(data: List<ProductNServiceData>) {
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
                    myBizViewModel.removeProductOrService(mContext, prodData.prod_serv_id, this@MyBusinessActivityLatest, PrefUtil.getBizId(mContext as Activity))
                }
            })
            listViewProductServices.adapter = adapter
            textAdd.setOnClickListener {
                startActivityForResult(Intent(this, ProductAndServices::class.java), UPDATE_PRODUCT_SERVICES)
            }
        }

    }

    override fun updateBrochureSection(data: List<BrochureData>) {

    }

    private fun updateAboutSection(basicDetailsDta: BasicDetailsData) {
        if (basicDetailsDta.aboutWhyUs != null && !basicDetailsDta.aboutWhyUs.equals("")) {
            lytAboutEmpty.visibility = View.GONE
            lytAboutData.visibility = View.VISIBLE
            textAboutDesc.text = basicDetailsDta.aboutDescription
            textAboutWhyUs.text = basicDetailsDta.aboutWhyUs
        } else {
            lytAboutData.visibility = View.GONE
            lytAboutEmpty.visibility = View.VISIBLE
        }
    }

    override fun updateAmenitiesSection(data: List<AmenityData>?) {
        if (data == null) {
            textAddAmenities.visibility = View.VISIBLE
            lytAmenity.visibility = View.GONE
        } else {
            updateAmenitiesUI(data)
        }
    }

    private fun updateAmenitiesUI(data: List<AmenityData>) {
        textAddAmenities.visibility = View.GONE
        lytAmenity.visibility = View.VISIBLE
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        recyclerAmenities.layoutManager = layoutManager
        recyclerAmenities.itemAnimator = DefaultItemAnimator()
        val amenityAdapter = AmenitiesMyBizStaggeredAdapter(this, data)
        recyclerAmenities.adapter = amenityAdapter
        textAmenityEdit.setOnClickListener {
            startActivityForResult(Intent(this, AmenitiesActivity::class.java), UPDATE_AMENITIES)
        }

    }

    override fun updatePaymentSection(data: List<PaymentMethodData>) {
        if (data == null || data.isEmpty()) {
            textAddPayments.visibility = View.VISIBLE
            lytPayments.visibility = View.GONE
        } else {
            updatePaymentsUI(data)
        }
    }

    private fun updatePaymentsUI(data: List<PaymentMethodData>) {
        textAddPayments.visibility = View.GONE
        lytPayments.visibility = View.VISIBLE
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        recyclerPayments.layoutManager = layoutManager
        recyclerPayments.itemAnimator = DefaultItemAnimator()
        val adapter = PaymentsMyBizStaggeredAdapter(this, data)
        recyclerPayments.adapter = adapter
        textPaymentEdit.setOnClickListener {
            startActivityForResult(Intent(this, PaymentsOptions::class.java), UPDATE_PAYMENTS)
        }
    }

    override fun updatePricingSection(data: List<PricingData>) {
        if (data == null || data.isEmpty()) {
            textAddPricing.visibility = View.VISIBLE
            lytPricing.visibility = View.GONE
        } else {
            updatePricingUI(data)
        }
    }

    private fun updatePricingUI(data: List<PricingData>) {
        textAddPricing.visibility = View.GONE
        lytPricing.visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(mContext)
        recyclerPricing.layoutManager = layoutManager
        recyclerPricing.itemAnimator = DefaultItemAnimator()
        val adapter = PricingMyBizStaggeredAdapter(this, data)
        recyclerPricing.adapter = adapter
    }

    override fun updateCategories(data: List<CategoryData>) {
        updateCategoryList(data)
    }

    private fun updateCategoryList(data: List<CategoryData>) {
        if (data.isNotEmpty()) {
            val catListView = findViewById<ListView>(R.id.myBizCategoryList)
            catListView.visibility = View.VISIBLE
            val adapter = BizCategoryListAdapter(this, data, object : DeleteCategoryListener {
                override fun deleteCategory(categorydata: CategoryData) {
                    LogUtils.showToast(mContext, "delete ${categorydata.category_name} and ${categorydata.category_id}now call delete api")
                    myBizViewModel.deleteCategoriesApi(mContext as Activity?, categorydata.category_id,  this as IMyBusinessLatest, PrefUtil.getBizId(mContext as Activity))
                }
            })
            catListView.adapter = adapter
        }
    }
}
package com.zaf.econnecto.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zaf.econnecto.BuildConfig
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.request_model.AddressData
import com.zaf.econnecto.network_call.response_model.home.CategoryData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.*
import com.zaf.econnecto.ui.presenters.ViewBusinessPresenter
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.ui.presenters.operations.IViewBizns
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.ScreenshotUtils
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.mb_address_detail.*
import kotlinx.android.synthetic.main.mb_communication_menu.*
import kotlinx.android.synthetic.main.mb_layout_about.*
import kotlinx.android.synthetic.main.mb_layout_amenities.*
import kotlinx.android.synthetic.main.mb_layout_brochure.*
import kotlinx.android.synthetic.main.mb_layout_categories.*
import kotlinx.android.synthetic.main.mb_layout_payment.*
import kotlinx.android.synthetic.main.mb_layout_pricing.*
import kotlinx.android.synthetic.main.mb_layout_product_services.*
import kotlinx.android.synthetic.main.mb_operating_hours.*
import java.io.File


class ViewBusinessActivity : BaseActivity<ViewBusinessPresenter>(), IViewBizns, IMyBusinessLatest, IMyBizImage, OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private var mContext: Context = this
    private lateinit var otherBizViewModel: OthersBusinessViewModel
    private lateinit var myBizVm: MyBusinessViewModel
    private lateinit var ownerId: String
    private lateinit var businessId: String
    lateinit var rootContent: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_view_business)
        otherBizViewModel = ViewModelProviders.of(this).get(OthersBusinessViewModel::class.java)
        myBizVm = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        presenter.initMap(this, mapFrag)
        initUI()
        updateActionbar()
    }

    override fun initPresenter(): ViewBusinessPresenter {
        return ViewBusinessPresenter(this, this)
    }

    private fun initUI() {
        ownerId = intent.getStringExtra(getString(R.string.key_owner_id))
        businessId = intent.getStringExtra(getString(R.string.key_biz_id))
        rootContent = findViewById<CoordinatorLayout>(R.id.rootContent)
        mapFrag.requireView().visibility = View.VISIBLE
        onClickEvents()
        callApis(ownerId, businessId)
    }

    private fun callApis(ownerId: String, businessId: String) {
        otherBizViewModel.otherBizBasicDetails(this, this, businessId)
        otherBizViewModel.bizImageList(mContext as Activity?, this, businessId)
        otherBizViewModel.bizOperatingHours(mContext as Activity?, this, businessId)
        otherBizViewModel.bizAmenityList(mContext as Activity?, this, businessId)
        myBizVm.bizProductServicesList(mContext as Activity?, this, businessId)
        otherBizViewModel.bizBrochureList(mContext as Activity?, this, businessId)
        otherBizViewModel.bizPaymentMethodList(mContext as Activity?, this, businessId)
        otherBizViewModel.bizPricingList(mContext as Activity?, this, businessId)
        otherBizViewModel.bizCategoryList(mContext as Activity?, this, businessId)
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


    private fun onClickEvents() {
        rlytLocation.setOnClickListener {
            mapFrag.requireView().visibility = if (mapFrag.requireView().visibility == View.VISIBLE)
                View.GONE
            else
                View.VISIBLE
        }

        rlytCall.setOnClickListener {
            Utils.callPhone(mContext, "7834908329")
        }
        rlytWhatsApp.setOnClickListener {
            Utils.openWhatsApp(mContext, "+91 7677616600")
        }

        rlytMail.setOnClickListener {
            Utils.openMsgInbox(mContext, "7834908329")
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

    override fun updateCategory(data: List<CategoryData>) {
//        val recycler_hotdeals = findViewById<RecyclerView>(R.id.recycler_hotdeals)
//        recycler_hotdeals.layoutManager = LinearLayoutManager(this)
//        recycler_hotdeals.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
//        val adapter = VBHeaderImageRecylcerAdapter(this, data, null)
//        recycler_hotdeals.adapter = adapter
    }

    override fun updateBannerImage(data: MutableList<ViewImageData>) {
        val recyclerHeader = findViewById<RecyclerView>(R.id.recycler_header)
        recyclerHeader.layoutManager = LinearLayoutManager(this)
        recyclerHeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = VBHeaderImageRecylcerAdapter(this, data)
        recyclerHeader.adapter = adapter
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

    override fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse, imageUpdate: Boolean) {
        if (basicDetailsResponse.data[0] != null) {
            presenter.initMap(this, mapFrag)
            updateMap(basicDetailsResponse.data[0])
            val basicDetailsData = basicDetailsResponse.data[0]
            textBusinessName.text = basicDetailsData.businessName
            textFollowers.text = "${basicDetailsData.followersCount} " + getString(R.string.followers)
            textShortDescription.text = basicDetailsData.shortDescription
            textEstablishedDate.text = getString(R.string.established_year) + ": ${basicDetailsData.yearEstablished}"
            textAddress.text = basicDetailsData.address1
            textWebsite.text = if (!basicDetailsData.website.isNullOrEmpty()) basicDetailsData.website else ""
            updateFollowUI(basicDetailsData.isFollowing)
            updateAboutSection(basicDetailsData)
        }
    }

    private fun updateFollowUI(following: Int) {
        if (following == 0) {
            textFollow.text = mContext.getString(R.string.follow)
//            textFollow.setBackgroundResource(R.drawable.add_biz_button)
//            textFollow.setTextColor(R.color.colorWhite)
        } else {
            textFollow.text = mContext.getString(R.string.following);
//            textFollow.setBackgroundResource(R.drawable.add_biz_button)
//            textFollow.setTextColor(R.color.colorWhite)
        }
    }

    private fun updateAboutSection(basicDetailsDta: BasicDetailsData?) {
        textAboutEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0) // to remove the edit drawable from end
        if (basicDetailsDta?.aboutWhyUs != null && !basicDetailsDta.aboutWhyUs.equals("")) {
            lytAboutEmpty.visibility = View.GONE
            lytAboutData.visibility = View.VISIBLE
            textAboutDesc.text = basicDetailsDta.aboutDescription
            textAboutWhyUs.text = basicDetailsDta.aboutWhyUs
        } else {
            lytAboutData.visibility = View.GONE
            lytAboutEmpty.visibility = View.VISIBLE
            textAboutDescLabel.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
            textAboutWhyUsLabel.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
            KotUtil.setNoDataUI(this,textAboutDescLabel)
            KotUtil.setNoDataUI(this,textAboutWhyUsLabel)
        }
    }

    override fun updateOperatingHours(data: List<OPHoursData>?) {
        iconEditOPHour.visibility = View.GONE
    }

    override fun updateProductServiceSection(data: List<ProductNServiceData>?) {
        if (data == null || data.isEmpty()) {
            textPnDHeader.text = getString(R.string.product_n_services)
            textAddProductNServices.visibility = View.VISIBLE
            textAddProductNServices.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
            KotUtil.setNoDataUI(this,textAddProductNServices)
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
                textAdd.visibility = View.GONE
              listViewProductServices.visibility = View.VISIBLE
              val layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
              listViewProductServices.layoutManager = layoutManager
              listViewProductServices.itemAnimator = DefaultItemAnimator()

              val adapter = DealsInStaggeredAdapter(this, data)
              listViewProductServices.adapter = adapter
        }

    }

    override fun updateBrochureSection(data: List<BrochureData>?) {
        if (data == null || data.isEmpty()) {
            textUploadBrochure.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
            KotUtil.setNoDataUI(this,textUploadBrochure)
        } else {
            updateBrochureUI()
        }
    }

    private fun updateBrochureUI() {
        TODO("Not yet implemented")
    }

    override fun updateAmenitiesSection(data: List<AmenityData>?) {
        textAmenityEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0) // to remove the edit drawable from end
        if (data == null) {
            textAddAmenities.visibility = View.VISIBLE
            textAddAmenities.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
            KotUtil.setNoDataUI(this,textAddAmenities)
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
    }

    override fun updatePaymentSection(data: List<PaymentMethodData>?) {
        textPaymentEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0) // to remove the edit drawable from end
        if (data == null || data.isEmpty()) {
            textAddPayments.visibility = View.VISIBLE
            textAddPayments.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            KotUtil.setNoDataUI(this,textAddPayments)
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

    }

    override fun updatePricingSection(data: List<PricingData>?) {
        textPricingEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        if (data == null || data.isEmpty()) {
            textAddPricing.visibility = View.VISIBLE
            textAddPricing.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            KotUtil.setNoDataUI(this,textAddPricing)
            lytPricing.visibility = View.GONE
        } else {
            updatePricingUI(data)
        }
    }

    private fun updatePricingUI(data: List<PricingData>) {
        textAddPricing.visibility = View.GONE
        lytPricing.visibility = View.VISIBLE
        val layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
        recyclerPricing.layoutManager = layoutManager
        recyclerPricing.itemAnimator = DefaultItemAnimator()
        val adapter = BizPricingAdapter(this, data)
        recyclerPricing.adapter = adapter
    }

    override fun updateCategories(data: List<UserCategoryData>?) {
        if (data == null || data.isEmpty()) {
            textAddCategory.visibility = View.VISIBLE
            textAddAmenities.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            KotUtil.setNoDataUI(this, textAddCategory)
        } else
            updateCategoryList(data)
    }

    private fun updateCategoryList(data: List<UserCategoryData>) {
        if (data.isNotEmpty()) {
            val catListView = findViewById<RecyclerView>(R.id.bizCategoryRecycler)
            catListView.visibility = View.VISIBLE
            textAddCategory.visibility = View.GONE
            val layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
            catListView.layoutManager = layoutManager
            catListView.itemAnimator = DefaultItemAnimator()
            val adapter = BizCategoryStaggeredAdapter(this, data)
            catListView.adapter = adapter
        }
    }

    private fun updateMap(basicDetailsDta: BasicDetailsData) {
        val fullAddress: String = basicDetailsDta.address1 + ", " + basicDetailsDta.cityTown + ", " + basicDetailsDta.state + ", " + basicDetailsDta.pinCode
        val location = KotUtil.getLocationFromAddress(this, fullAddress)!!
        val address = AddressData(basicDetailsDta.address1, basicDetailsDta.state, basicDetailsDta.cityTown, basicDetailsDta.pinCode, location.latitude.toString() + "", "" + location.longitude)
        LogUtils.DEBUG(address.toString())
        val storeLocation = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(storeLocation)
        gMap.addMarker(markerOptions)
        gMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation))
    }

}

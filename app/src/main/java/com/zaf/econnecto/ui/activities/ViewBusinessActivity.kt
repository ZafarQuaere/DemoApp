package com.zaf.econnecto.ui.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
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
import com.zaf.econnecto.network_call.request_model.AddressData
import com.zaf.econnecto.network_call.response_model.home.CategoryData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsData
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.activities.mybiz.*
import com.zaf.econnecto.ui.adapters.TabViewPagerAdapter
import com.zaf.econnecto.ui.adapters.VBHeaderImageRecylcerAdapter
import com.zaf.econnecto.ui.fragments.details_frag.*
import com.zaf.econnecto.ui.presenters.ViewBusinessPresenter
import com.zaf.econnecto.ui.presenters.operations.IMyBizImage
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.ui.presenters.operations.IViewBizns
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
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


class ViewBusinessActivity : BaseActivity<ViewBusinessPresenter>(), IViewBizns,IMyBusinessLatest, IMyBizImage, OnMapReadyCallback {

    private lateinit var tabLayout : TabLayout
    private lateinit var gMap : GoogleMap
    private var mContext : Context = this
    private lateinit var myBizViewModel: MyBusinessViewModel
    private lateinit var ownerId : String
    private lateinit var businessId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_view_business)
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        presenter.initMap(this,mapFrag)
        initUI()

    }

    override fun initPresenter(): ViewBusinessPresenter {
        return ViewBusinessPresenter(this,this)
    }

    private fun initUI() {
        ownerId = intent.getStringExtra(getString(R.string.key_owner_id))
        businessId = intent.getStringExtra(getString(R.string.key_biz_id))
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        addTabs()
        mapFrag.requireView().visibility = View.VISIBLE
        onClickEvents()
        callApis(ownerId,businessId)
    }

    private fun callApis(ownerId: String, businessId: String) {
        myBizViewModel.otherBizBasicDetails(this, true, this, ownerId)
        myBizViewModel.bizImageList(mContext as Activity?, this, businessId)
        myBizViewModel.bizOperatingHours(mContext as Activity?, this, businessId)
        myBizViewModel.bizAmenityList(mContext as Activity?, this, businessId)
        myBizViewModel.bizProductServicesList(mContext as Activity?, this, businessId)
        myBizViewModel.bizBrochureList(mContext as Activity?, this, businessId)
        myBizViewModel.bizPaymentMethodList(mContext as Activity?, this, businessId)
        myBizViewModel.bizPricingList(mContext as Activity?, this, businessId)
        myBizViewModel.bizCategoryList(mContext as Activity?, this, businessId)

    }

    private fun addTabs() {
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

    private fun onClickEvents() {
        rlytLocation.setOnClickListener {
            mapFrag.requireView().visibility = if (mapFrag.requireView().visibility == View.VISIBLE)
                View.GONE
            else
                View.VISIBLE
        }

        rlytCall.setOnClickListener {
            Utils.callPhone(mContext,"7834908329")
        }
        rlytWhatsApp.setOnClickListener {
            Utils.openWhatsApp(mContext,"+91 7677616600")
        }

        rlytMail.setOnClickListener {
            Utils.openMsgInbox(mContext,"7834908329")
        }
    }

    private fun setupViewPager(viewPagerTabs: ViewPager) {
        val adapter = TabViewPagerAdapter(this.supportFragmentManager, arrayListOf<String>("First Fragment","Second Fragment","Third Fragment","Fourth Fragment","Fifth Fragment"))
        adapter.addFragment(FirstFragment(), "Menu")
        adapter.addFragment(SecondFragment(), "About")
        adapter.addFragment(ThirdFragment(), "Amenities")
        adapter.addFragment(FourthFragment(), "Photos")
        adapter.addFragment(FifthFragment(), "Payment")
        viewPagerTabs.adapter = adapter
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
        recyclerHeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        val adapter = VBHeaderImageRecylcerAdapter(this, data)
        recyclerHeader.adapter = adapter
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
    }

    override fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse, imageUpdate: Boolean) {
        if (basicDetailsResponse.data[0] != null) {
            presenter.initMap(this,mapFrag)
            updateMap(basicDetailsResponse.data[0])
            val basicData = basicDetailsResponse.data[0]
            textBusinessName.text = basicData.businessName

        } else {

        }



    }

    override fun updateOperatingHours(data: OPHoursData) {
        LogUtils.DEBUG("updateOperatingHours")
    }

    override fun updateProductServiceSection(data: List<ProductNServiceData>) {
        LogUtils.DEBUG("updateProductServiceSection")
    }

    override fun updateBrochureSection(data: List<BrochureData>) {
        LogUtils.DEBUG("updateBrochureSection")
    }

    override fun updateAmenitiesSection(data: List<AmenityData>?) {
        LogUtils.DEBUG("updateAmenitiesSection")
    }

    override fun updatePaymentSection(data: List<PaymentMethodData>) {
        LogUtils.DEBUG("updatePaymentSection")
    }

    override fun updatePricingSection(data: List<PricingData>) {
        LogUtils.DEBUG("updatePricingSection")
    }

    override fun updateCategories(data: List<com.zaf.econnecto.ui.activities.mybiz.UserCategoryData>) {
        LogUtils.DEBUG("updateCategories")
    }

    private fun updateMap(basicDetailsDta: BasicDetailsData) {
        val fullAddress: String = basicDetailsDta.address1 + ", " + basicDetailsDta.cityTown + ", " + basicDetailsDta.state + ", " + basicDetailsDta.pinCode
        val location = KotUtil.getLocationFromAddress(this, fullAddress)!!
        val address = AddressData(basicDetailsDta.address1, basicDetailsDta.state, basicDetailsDta.cityTown, basicDetailsDta.pinCode, location.latitude.toString() + "", "" + location.longitude)
//        location.latitude,location.longitude
        LogUtils.DEBUG(address.toString())
        val storeLocation = LatLng(location.latitude, location.longitude)
        //val ny = LatLng(-34.0, 151.0)
        val markerOptions = MarkerOptions()
        markerOptions.position(storeLocation)
        gMap.addMarker(markerOptions)
        gMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation))
    }

}

package com.zaf.econnecto.ui.activities

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
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
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.vb_address_detail.*
import kotlinx.android.synthetic.main.vb_communication_menu.*
import kotlinx.android.synthetic.main.vb_layout_about.*
import kotlinx.android.synthetic.main.vb_layout_amenities.*
import kotlinx.android.synthetic.main.vb_layout_brochure.*
import kotlinx.android.synthetic.main.vb_layout_categories.*
import kotlinx.android.synthetic.main.vb_layout_payment.*
import kotlinx.android.synthetic.main.vb_layout_pricing.*
import kotlinx.android.synthetic.main.vb_layout_product_services.*
import kotlinx.android.synthetic.main.vb_operating_hours.*


class ViewBusinessActivity : BaseActivity<ViewBusinessPresenter>(), IViewBizns, IMyBusinessLatest, IMyBizImage, OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private var mContext: Context = this
    private lateinit var otherBizViewModel: OthersBusinessViewModel
    private lateinit var ownerId: String
    private lateinit var businessId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_view_business)
        otherBizViewModel = ViewModelProviders.of(this).get(OthersBusinessViewModel::class.java)
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

        mapFrag.requireView().visibility = View.VISIBLE
        onClickEvents()
        callApis(ownerId, businessId)
    }

    private fun callApis(ownerId: String, businessId: String) {
        otherBizViewModel.otherBizBasicDetails(this, this, businessId)
        otherBizViewModel.bizImageList(mContext as Activity?, this, businessId)
        otherBizViewModel.bizOperatingHours(mContext as Activity?, this, businessId)
        otherBizViewModel.bizAmenityList(mContext as Activity?, this, businessId)
        otherBizViewModel.bizProductServicesList(mContext as Activity?, this, businessId)
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

    override fun updateOperatingHours(data: OPHoursData?) {
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
              val layoutManager = LinearLayoutManager(mContext)
              listViewProductServices.layoutManager = layoutManager
              listViewProductServices.itemAnimator = DefaultItemAnimator()

              val adapter = BizProdNServiceListAdapter(this, data, null)
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
        val layoutManager = LinearLayoutManager(mContext)
        recyclerPricing.layoutManager = layoutManager
        recyclerPricing.itemAnimator = DefaultItemAnimator()
        val adapter = PricingMyBizStaggeredAdapter(this, data)
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
            val catListView = findViewById<ListView>(R.id.myBizCategoryList)
            catListView.visibility = View.VISIBLE
            textAddCategory.visibility = View.GONE
            val adapter = UserCategoryListAdapter(this, data, null)
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

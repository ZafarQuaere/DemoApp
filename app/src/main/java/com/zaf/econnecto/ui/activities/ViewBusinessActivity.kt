package com.zaf.econnecto.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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
import com.zaf.econnecto.network_call.response_model.home.CategoryData
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData
import com.zaf.econnecto.ui.adapters.TabViewPagerAdapter
import com.zaf.econnecto.ui.adapters.VBHeaderImageRecylcerAdapter
import com.zaf.econnecto.ui.fragments.details_frag.*
import com.zaf.econnecto.ui.presenters.ViewBusinessPresenter
import com.zaf.econnecto.ui.presenters.operations.IViewBizns
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.vb_communication_menu.*


class ViewBusinessActivity : BaseActivity<ViewBusinessPresenter>(), IViewBizns,OnMapReadyCallback {

    private lateinit var viewPagerTabs : ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var gMap : GoogleMap
    private var mContext : Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_view_business)
        initUI()
        presenter.callCategoryApi()
        presenter.callBannerImgApi()
        presenter.updateActionbar(this)

        presenter.initMap(this,mapFrag)
        /*val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapFrag) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFrag.view!!.visibility = View.GONE*/
    }

    override fun initPresenter(): ViewBusinessPresenter {
        return ViewBusinessPresenter(this,this)
    }

    private fun initUI() {
        viewPagerTabs = findViewById<ViewPager>(R.id.viewpagerTabs)
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPagerTabs)
        setupViewPager(viewPagerTabs)
        onClickEvents()

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

//        val fullAddress: String = address + ", " + city + ", " + state + ", " + pincode
//        val location: Address = KotUtil.getLocationFromAddress(this, "fullAddress")!!
//        val adress = AddressData(address, state, city, pincode, location.latitude.toString() + "", "" + location.longitude)
//        location.latitude,location.longitude

        val ny = LatLng(28.6523644, 77.1907413)
        //val ny = LatLng(-34.0, 151.0)
        val markerOptions = MarkerOptions()
        markerOptions.position(ny)
        gMap.addMarker(markerOptions)
        gMap.moveCamera(CameraUpdateFactory.newLatLng(ny))

    }

}

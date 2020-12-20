package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse
import com.zaf.econnecto.ui.adapters.AmenitiesAddEditStaggeredAdapter
import com.zaf.econnecto.ui.presenters.operations.IMyBusinessLatest
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.layout_amenities.*
import java.util.*


 class AmenitiesActivity : AppCompatActivity(),IMyBusinessLatest {
    private lateinit var adapterAddEdit: AmenitiesAddEditStaggeredAdapter
    lateinit var recyclerAmenity: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    lateinit var emptyTextView: TextView
    var mContext: Activity = this
    private lateinit var myBizViewModel : MyBusinessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_amenities)
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        myBizViewModel.bizAmenityList(this,this)
        initUI()
    }

    private fun initUI() {
        recyclerAmenity = findViewById<RecyclerView>(R.id.recycler_amenities)
        recyclerAmenity.setHasFixedSize(true)
        emptyTextView = findViewById(R.id.emptyTextView)

        clickEvents()
    }

     override fun updateBasicDetails(basicDetailsResponse: BasicDetailsResponse, imageUpdate: Boolean) {
         TODO("Not yet implemented")
     }

     override fun updateOperatingHours(data: OPHoursData) {
         TODO("Not yet implemented")
     }

     override fun updateProductServiceSection(data: List<ProductNServiceData>) {
         TODO("Not yet implemented")
     }

     override fun updateBrochureSection(data: List<BrochureData>) {
         TODO("Not yet implemented")
     }

     override fun updateAmenitiesSection(data: List<AmenityData>?) {
        if (data == null) {

        } else {
            staggeredGridView(data)
//            updateAmenitiesUI(data)
        }
    }

     override fun updatePaymentSection(data: List<PaymentMethodData>) {
         TODO("Not yet implemented")
     }

     override fun updatePricingSection(data: List<PricingData>) {
         TODO("Not yet implemented")
     }

     override fun updateCategories(data: List<CategoryData>) {
         TODO("Not yet implemented")
     }

     private fun staggeredGridView(data: List<AmenityData>) {
        val layoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)
        recyclerAmenity.layoutManager = layoutManager
        recyclerAmenity.itemAnimator = DefaultItemAnimator()

        val amenityList = mutableListOf<AmenitySelectedData>()
        for (i in data.indices){
            val amnData = AmenitySelectedData(false,data[i].amenity_id,data[i].amenity_name)
            amenityList.add(i,amnData)
        }
       /* val data = AmenitySelectedData(false,"123","ABC ")
        amenityList.add(0, data)
        val data1 = AmenitySelectedData(false,"1234","ABCDEF ")
        amenityList.add(1, data1)
        val data2 = AmenitySelectedData(false,"1235","GHIJK ")
        amenityList.add(2, data2)
        val data3 = AmenitySelectedData(false,"1236","LMNOP ")
        amenityList.add(3, data3)
        val data4 = AmenitySelectedData(false,"1237","XYZ ")
        amenityList.add(4, data4)*/

//        val list = mutableListOf<String>("ABC", "DEF", "GHIJKL", "MNOP", "QRSTU", "VWXYZ", "ABCD")
        adapterAddEdit = AmenitiesAddEditStaggeredAdapter(this, amenityList)
        recyclerAmenity.adapter = adapterAddEdit
    }


/*
    private fun gridView() {
        layoutManager = GridLayoutManager(this, 3)
        recyclerAmenity.setLayoutManager(layoutManager)
        recyclerAmenity.setItemAnimator(DefaultItemAnimator())
        val list = mutableListOf<String>("ABC", "DEF", "GHIJKL", "MNOP", "QRSTU", "VWXYZ", "ABCD")
         adapter = AmenitiesRecyclerAdapter(this, list)
        recyclerAmenity.adapter = adapter
    }
*/

    private fun clickEvents() {
        textSubmit.setOnClickListener {
          val selectedItem = adapterAddEdit.getSelectedItems()
            LogUtils.DEBUG("Selected Items : "+ Arrays.asList(selectedItem))
            LogUtils.showToast(mContext,"Submitted")
             val returnIntent = Intent()
            returnIntent.putExtra("result", "data from secondActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        textBack.setOnClickListener {
            onBackPressed()
        }
    }
}
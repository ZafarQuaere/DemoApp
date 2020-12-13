package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.adapters.AmenitiesRecyclerAdapter
import com.zaf.econnecto.ui.adapters.AmenitiesStaggeredAdapter
import com.zaf.econnecto.ui.adapters.StaggeredImageAdapter
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.layout_amenities.*


class AmenitiesActivity : AppCompatActivity() {
    private lateinit var adapter: AmenitiesStaggeredAdapter
    lateinit var recyclerAmenity: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    lateinit var emptyTextView: TextView
    var mContext: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_amenities)
        initUI()
    }

    private fun initUI() {
        recyclerAmenity = findViewById<RecyclerView>(R.id.recycler_amenities)
        recyclerAmenity.setHasFixedSize(true)
        emptyTextView = findViewById(R.id.emptyTextView)
//        gridView()
        staggeredGridView()
        clickEvents()
    }

    private fun staggeredGridView() {
        val layoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)
        recyclerAmenity.layoutManager = layoutManager
        recyclerAmenity.itemAnimator = DefaultItemAnimator()

        val amenityList = emptyList<AmenityDummyData>().toMutableList()
        val data = AmenityDummyData(false,"123","ABC ")
        amenityList[0] = data
        val data1 = AmenityDummyData(false,"1234","ABCDEF ")
        amenityList[1] = data1
        val data2 = AmenityDummyData(false,"1235","GHIJK ")
        amenityList[2] = data2
        val data3 = AmenityDummyData(false,"1236","LMNOP ")
        amenityList[3] = data3
        val data4 = AmenityDummyData(false,"1237","XYZ ")
        amenityList[4] = data4

//        val list = mutableListOf<String>("ABC", "DEF", "GHIJKL", "MNOP", "QRSTU", "VWXYZ", "ABCD")
        val adapter = AmenitiesStaggeredAdapter(this, amenityList)
        recyclerAmenity.adapter = adapter
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
//            adapter.getSelectedItems()
            LogUtils.showToast(mContext,"submit clicked")
           /* val returnIntent = Intent()
            returnIntent.putExtra("result", "data from secondActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()*/
        }

        textBack.setOnClickListener {
            onBackPressed()
        }
    }
}
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
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.layout_amenities.*


class AmenitiesActivity : AppCompatActivity() {
    lateinit var recyclerAmenity: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    lateinit var emptyTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_amenities)
        initUI()
    }

    private fun initUI() {
        recyclerAmenity = findViewById<RecyclerView>(R.id.recycler_amenities)
        recyclerAmenity!!.setHasFixedSize(true)
        emptyTextView = findViewById(R.id.emptyTextView)
//        gridView()
        staggeredGridView()
       clickEvents()
    }

    private fun staggeredGridView() {
       val layoutManager = StaggeredGridLayoutManager( 3, LinearLayout.VERTICAL)
        recyclerAmenity!!.setLayoutManager(layoutManager)
        recyclerAmenity!!.setItemAnimator(DefaultItemAnimator())
        val list = mutableListOf<String>("ABC","DEF","GHIJKL","MNOP","QRSTU","VWXYZ","ABCD")
        val adapter = AmenitiesStaggeredAdapter(this, list)
        recyclerAmenity!!.adapter = adapter
    }

    private fun gridView() {
        layoutManager = GridLayoutManager(this, 3)
        recyclerAmenity.setLayoutManager(layoutManager)
        recyclerAmenity.setItemAnimator(DefaultItemAnimator())
        val list = mutableListOf<String>("ABC","DEF","GHIJKL","MNOP","QRSTU","VWXYZ","ABCD")
        val adapter = AmenitiesRecyclerAdapter(this,list)
        recyclerAmenity.adapter = adapter
    }

    private fun clickEvents() {
        textSubmit.setOnClickListener {
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